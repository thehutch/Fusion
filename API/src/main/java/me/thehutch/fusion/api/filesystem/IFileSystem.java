/*
 * This file is part of API, licensed under the Apache 2.0 License.
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
	 * Retrieves the resource from the file system. If it has not been
	 * loaded before then the resource will be loaded into memory.
	 *
	 * @param <R>  The type of resource to load
	 * @param path The path to the resource
	 *
	 * @return The loaded resource
	 */
	public <R> R getResource(Path path);

	/**
	 * Retrieves the resource from the file system. If it has not been
	 * loaded then it will be loaded if the load parameter is true.
	 *
	 * @param <R>  The type of resource to load
	 * @param path The path to the resource
	 * @param load True to load the resource if not already
	 *
	 * @return The resource, null if it is not already loaded and
	 *         load is false, or null if it could not be loaded
	 */
	public <R> R getResource(Path path, boolean load);

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
	 * Registers the resource manager with the file system to manage any
	 * resources loaded using the given extensions.
	 *
	 * @param manager    The resource manager
	 * @param extensions The file extensions which require this loader
	 */
	public void registerResourceManager(IResourceManager<?> manager, String... extensions);
}
