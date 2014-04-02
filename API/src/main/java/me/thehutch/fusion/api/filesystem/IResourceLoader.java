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

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author thehutch
 * @param <T>
 */
public interface IResourceLoader<T> {
	/**
	 * Loads a resource at the given path.
	 *
	 * @param path The path to the resource
	 *
	 * @return The loaded resource
	 *
	 * @throws java.io.IOException Thrown if an IO error occurs during loading
	 */
	public T load(Path path) throws IOException;
}