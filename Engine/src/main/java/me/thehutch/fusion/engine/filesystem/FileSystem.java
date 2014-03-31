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
import java.io.InputStream;
import me.thehutch.fusion.api.filesystem.IFileSystem;
import me.thehutch.fusion.api.filesystem.IResourceLoader;

/**
 * @author thehutch
 */
public class FileSystem implements IFileSystem {
	private final TMap<String, IResourceLoader<?>> loaders = new THashMap<>();
	private final TMap<String, Object> loadedResources = new THashMap<>();

	@Override
	public <R> R getResource(String scheme, String path) {
		R resource = (R) loadedResources.get(path);
		if (resource != null) {
			return resource;
		}
		throw new UnsupportedOperationException("getResource in class FileSystem is not supported yet.");
	}

	@Override
	public InputStream getResourceStream(String scheme, String path) {
		throw new UnsupportedOperationException("getResourceStream in class FileSystem is not supported yet.");
	}

	@Override
	public void registerLoader(String scheme, IResourceLoader<?> loader) {
		this.loaders.put(scheme, loader);
	}
}
