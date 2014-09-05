/*
 * This file is part of Engine, licensed under the Apache 2.0 License.
 *
 * Copyright (c) 2014 thehutch.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.thehutch.fusion.engine.filesystem;

import static java.nio.file.FileVisitOption.FOLLOW_LINKS;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;
import java.util.logging.Level;
import me.thehutch.fusion.api.filesystem.IFileSystem;
import me.thehutch.fusion.api.filesystem.IResourceManager;
import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.Engine;
import me.thehutch.fusion.engine.render.Renderer;

public class FileSystem implements IFileSystem, Disposable {
	public static final Path BASE_DIRECTORY = Paths.get(System.getProperty("user.dir"));
	public static final Path DATA_DIRECTORY = BASE_DIRECTORY.resolve("data");
	public static final Path PLUGIN_DIRECTORY = BASE_DIRECTORY.resolve("plugins");
	private final TMap<String, IResourceManager<?>> managers = new THashMap<>();

	public FileSystem() {
		try {
			// Create the data directory
			Files.createDirectories(DATA_DIRECTORY);
			// Create the plugin directory
			Files.createDirectories(PLUGIN_DIRECTORY);
		} catch (IOException ex) {
			throw new SecurityException("Unable to create directory", ex);
		}
		// Extract the engine meshes
		extractDirectory("meshes", Renderer.MESH_DIRECTORY, true);
		// Extract the engine shaders
		extractDirectory("shaders", Renderer.SHADER_DIRECTORY, true);
		// Extract the engine textures
		extractDirectory("textures", Renderer.TEXTURE_DIRECTORY, true);
		// Extract the engine programs
		extractDirectory("programs", Renderer.PROGRAM_DIRECTORY, true);
		// Extract the engine materials
		extractDirectory("materials", Renderer.MATERIAL_DIRECTORY, true);
	}

	@Override
	public <R> R getResource(Path path) {
		return getResource(path, true);
	}

	@Override
	public <R> R getResource(Path path, boolean load) {
		final String extension = getPathExtension(path);
		// Retrieve the loader for the file extension
		return (R) managers.get(extension).get(path, load);
	}

	@Override
	public InputStream getResourceStream(Path path) {
		try {
			return Files.newInputStream(path, StandardOpenOption.READ);
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to obtain input stream for resource: " + path, ex);
		}
	}

	@Override
	public void unloadResource(Path path) {
		this.managers.get(getPathExtension(path)).unload(path);
	}

	@Override
	public void registerResourceManager(IResourceManager<?> manager, String... extensions) {
		for (String extension : extensions) {
			this.managers.put(extension, manager);
		}
	}

	@Override
	public void dispose() {
		this.managers.forEachValue((IResourceManager<?> manager) -> {
			manager.dispose();
			return true;
		});
		this.managers.clear();
	}

	/**
	 * Returns the file extension associated with a path.
	 *
	 * @param path The path to get the extension of
	 *
	 * @return The file extension, or null if there is no file extension
	 */
	private static String getPathExtension(Path path) {
		final String pathAsString = path.toString();
		final int extPos = pathAsString.lastIndexOf('.');
		return extPos == -1 ? null : pathAsString.substring(extPos + 1);
	}

	/**
	 * Retrieves an input stream to a resource located inside the Engine jar.
	 *
	 * @param path The path to the resource relative to the Engine class
	 *
	 * @return The resource input stream
	 */
	public static InputStream getJarResource(String path) {
		return Engine.class.getResourceAsStream('/' + path);
	}

	/**
	 * Converts the path so that it is relative to the base directory.
	 *
	 * @param path The path to convert
	 *
	 * @return The relativised path
	 */
	private static Path relativise(Path path) {
		return FileSystem.BASE_DIRECTORY.relativize(path);
	}

	/**
	 * Copy the source file to the target location. The {@code preserve}
	 * paramter determins if the file attributes should be copied/preserved.
	 *
	 * @param source    The source file
	 * @param target    The target file
	 * @param overwrite Whether to overwrite the file attributes
	 */
	private static void copyFile(Path source, Path target, boolean overwrite) {
		if (Files.notExists(target) || overwrite) {
			try {
				Files.copy(source, target, REPLACE_EXISTING);
			} catch (IOException ex) {
				Engine.getLogger().log(Level.WARNING, "Unable to copy file: " + relativise(source), ex);
			}
		}
	}

	/**
	 * Extracts the source directory out of the engine jar and copies it to the destination directory.
	 * If the destination directory does not exist then it is created.
	 *
	 * @param source The source directory relative to the engine classpath
	 * @param target The target directory relative to the user directory
	 */
	private static void extractDirectory(String source, Path target, boolean overwrite) {
		// Check if the directory already exists
		if (Files.notExists(target)) {
			Engine.getLogger().log(Level.INFO, "'{0}' does not exist. Creating...", target);

			// Attempt to create the destination directory
			try {
				Files.createDirectories(target);
			} catch (FileAlreadyExistsException ex) {
				Engine.getLogger().log(Level.WARNING, "'" + relativise(target) + "' already exists", ex);
			} catch (IOException ex) {
				Engine.getLogger().log(Level.SEVERE, "Unable to create directory: " + relativise(target), ex);
			}
		}

		// Get the name of the engine jar
		final String jarName;
		try {
			jarName = FileSystem.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException ex) {
			throw new IllegalStateException("Unable to get engine jar name!", ex);
		}

		// Get the path to the engine jar
		final Path sourcePath = BASE_DIRECTORY.resolve(jarName).resolve(source);

		// Follow links when copying files
		final EnumSet<FileVisitOption> options = EnumSet.of(FOLLOW_LINKS);
		final CopyFileVisitor visitor = new CopyFileVisitor(sourcePath, target, overwrite);
		try {
			Files.walkFileTree(sourcePath, options, Integer.MAX_VALUE, visitor);
		} catch (IOException ex) {
			Engine.getLogger().log(Level.SEVERE, "Unable to extract directory: " + relativise(sourcePath), ex);
		}
	}

	private static final class CopyFileVisitor implements FileVisitor<Path> {
		private final Path source;
		private final Path target;
		private final boolean overwrite;

		private CopyFileVisitor(Path source, Path target, boolean overwrite) {
			this.source = source;
			this.target = target;
			this.overwrite = overwrite;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			final Path newDir = target.resolve(source.relativize(dir));
			try {
				Files.copy(dir, newDir);
			} catch (FileAlreadyExistsException ex) {
				// Ignore exception
			} catch (IOException ex) {
				Engine.getLogger().log(Level.SEVERE, "Unable to create directory: " + relativise(newDir), ex);
				return SKIP_SUBTREE;
			}
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			copyFile(file, target.resolve(source.relativize(file)), overwrite);
			return CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exception) throws IOException {
			// Update the modification time of directory when done
			if (exception == null && overwrite) {
				final Path newDir = target.resolve(source.relativize(dir));
				try {
					final FileTime time = Files.getLastModifiedTime(dir);
					Files.setLastModifiedTime(newDir, time);
				} catch (IOException ex) {
					Engine.getLogger().log(Level.SEVERE, "Unable to copy all attributes to: " + relativise(newDir), ex);
				}
			}
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exception) throws IOException {
			if (exception instanceof FileSystemLoopException) {
				Engine.getLogger().log(Level.SEVERE, "Cycle detected: " + relativise(file), exception);
			} else {
				Engine.getLogger().log(Level.SEVERE, "Unable to copy: " + relativise(file), exception);
			}
			return CONTINUE;
		}
	}
}
