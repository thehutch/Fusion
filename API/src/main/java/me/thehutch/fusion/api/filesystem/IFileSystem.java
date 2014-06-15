/*
 * This file is part of API.
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
package me.thehutch.fusion.api.filesystem;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * @author thehutch
 */
public interface IFileSystem {
	/**
	 * Retrieves the resource from the file system, if it has not been loaded before then
	 * the resource will be loaded into memory.
	 *
	 * @param <R>  The type of resource to load
	 * @param path The path to the resource
	 *
	 * @return The loaded resource
	 */
	public <R> R getResource(Path path);

	/**
	 * Retrieves an input stream to the resource.
	 *
	 * @param path The path to the resource
	 *
	 * @return The resource input stream
	 */
	public InputStream getResourceStream(Path path);

	/**
	 * Unloads the resource at the given path from the file system.
	 *
	 * @param path The path to the resource
	 */
	public void unloadResource(Path path);

	/**
	 * Registers the resource loader with the file system to load any resources using the given extensions.
	 *
	 * @param loader     The resource loader
	 * @param extensions The file extensions which require this loader
	 */
	public void registerLoader(ResourceLoader<?> loader, String... extensions);

	/**
	 * Returns the file extension associated with a path.
	 *
	 * @param path The path to get the extension of
	 *
	 * @return The file extension, or null if there is no file extension
	 */
	public static String getPathExtension(Path path) {
		final String pathAsString = path.toString();
		final int extPos = pathAsString.lastIndexOf('.');
		return extPos == -1 ? null : pathAsString.substring(extPos + 1);
	}
}
