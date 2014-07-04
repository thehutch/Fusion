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

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.nio.file.Path;
import me.thehutch.fusion.api.util.Disposable;

/**
 * @author thehutch
 *
 * @param <T> The type of the resource this loader loads
 */
public abstract class ResourceLoader<T> implements Disposable {
	protected final TMap<Path, T> resources = new THashMap<>();

	/**
	 * Retrieves the resource located at the given path either from the resource cache
	 * or if it has not been loaded then it will be loaded.
	 *
	 * @param path The path to the resource
	 *
	 * @return The resource
	 */
	public final T get(Path path) {
		final T resource = resources.get(path);
		if (resource == null) {
			return load(path);
		}
		return resource;
	}

	/**
	 * Checks if the resource is loaded into the file system.
	 *
	 * @param path The path to the resource
	 *
	 * @return True if the resource is already loaded
	 */
	public final boolean isLoaded(Path path) {
		return resources.containsKey(path);
	}

	/**
	 * Loads a resource at the given path.
	 *
	 * @param path The path to the resource
	 *
	 * @return The loaded resource
	 */
	public abstract T load(Path path);

	/**
	 * Unloads the resource at the given path.
	 *
	 * @param path The path to the resource
	 */
	public abstract void unload(Path path);
}
