/*
 * This file is part of Engine.
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
import me.thehutch.fusion.api.filesystem.IResourceLoader;
import me.thehutch.fusion.engine.Engine;


public class FileSystem implements IFileSystem {
	public static final Path BASE_DIRECTORY = Paths.get(System.getProperty("user.dir"));
	public static final Path DATA_DIRECTORY = BASE_DIRECTORY.resolve("data");
	private final TMap<String, IResourceLoader<?>> loaders = new THashMap<>();

	public FileSystem() {
		// Extract the engine models
		extractDirectory("models", DATA_DIRECTORY.resolve("models"));
		// Extract the engine shaders
		extractDirectory("shaders", DATA_DIRECTORY.resolve("shaders"));
	}

	@Override
	public <R> R getResource(Path path) {
		// Calculate the file extension
		final String extension = path.toString().substring(path.toString().lastIndexOf('.') + 1);
		try {
			// Retrieve the loader for the file extension
			return (R) loaders.get(extension).load(path);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public InputStream getResourceStream(Path path) {
		try {
			return Files.newInputStream(path, StandardOpenOption.READ);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public void registerLoader(String extension, IResourceLoader<?> loader) {
		this.loaders.put(extension, loader);
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
			final DirectoryStream<Path> stream = Files.newDirectoryStream(sourcePath);
			stream.forEach((Path path) -> {
				try {
					Files.copy(path, dest.resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			});
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
		}
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