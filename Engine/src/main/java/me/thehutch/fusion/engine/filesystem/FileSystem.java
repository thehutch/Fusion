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

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import me.thehutch.fusion.api.filesystem.IFileSystem;
import me.thehutch.fusion.api.filesystem.IResourceManager;
import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.Engine;
import me.thehutch.fusion.engine.render.Renderer;

public class FileSystem implements IFileSystem, Disposable {
	public static final Path BASE_DIRECTORY = Paths.get(System.getProperty("user.dir"));
	public static final Path DATA_DIRECTORY = BASE_DIRECTORY.resolve("data");
	private final TMap<String, IResourceManager<?>> managers = new THashMap<>();

	public FileSystem() {
		// Extract the engine meshes
		extractDirectory("meshes", Renderer.MESH_DIRECTORY);
		// Extract the engine shaders
		extractDirectory("shaders", Renderer.SHADER_DIRECTORY);
		// Extract the engine textures
		extractDirectory("textures", Renderer.TEXTURE_DIRECTORY);
		// Extract the engine program
		extractDirectory("programs", Renderer.PROGRAM_DIRECTORY);
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
	 * Extracts the source directory out of the engine jar and copies it to the destination directory.
	 * If the destination directory does not exist then it is created.
	 *
	 * @param source The source directory relative to the Engine classpath
	 * @param dest   The destination directory relative to the user directory
	 */
	public static void extractDirectory(String source, Path dest) {
		try {
			// Create the destination directory
			Files.createDirectories(dest);

			// Get the path to the source directory
			final Path sourcePath = Paths.get(Engine.class.getResource('/' + source).toURI());

			// Obtain a directory stream to extract each file
			try (final DirectoryStream<Path> stream = Files.newDirectoryStream(sourcePath)) {
				stream.forEach((Path path) -> {
					try {
						Files.copy(path, dest.resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				});
			}
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Returns the file extension associated with a path.
	 *
	 * @param path The path to get the extension of
	 *
	 * @return The file extension, or null if there is no file extension
	 */
	private String getPathExtension(Path path) {
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
}
