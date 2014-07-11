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

import java.nio.file.Path;
import me.thehutch.fusion.api.util.Disposable;

/**
 * @author thehutch
 *
 * @param <T> The type of the resource this loader loads
 */
public interface IResourceManager<T> extends Disposable {
	/**
	 * Retrieves the resource from the resource manager.
	 * If it has not been loaded then it will be loaded if the load parameter is true.
	 *
	 * @param path The path to the resource
	 * @param load True to load the resource if not already
	 *
	 * @return The resource, or null if it is not already loaded and
	 *         load is false, or null if it could not be loaded
	 */
	public T get(Path path, boolean load);

	/**
	 * Checks if the resource is already loaded into the file system.
	 *
	 * @param path The path to the resource
	 *
	 * @return True if the resource is already loaded
	 */
	public boolean isLoaded(Path path);

	/**
	 * Loads a resource at the given path.
	 *
	 * @param path The path to the resource
	 *
	 * @return The loaded resource
	 */
	public T load(Path path);

	/**
	 * Unloads the resource at the given path.
	 *
	 * @param path The path to the resource
	 */
	public void unload(Path path);
}
