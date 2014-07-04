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
package me.thehutch.fusion.engine.filesystem.loaders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import me.thehutch.fusion.api.filesystem.IFileSystem;
import me.thehutch.fusion.api.filesystem.ResourceLoader;
import me.thehutch.fusion.engine.filesystem.FileSystem;
import me.thehutch.fusion.engine.scene.Model.ModelData;
import me.thehutch.fusion.engine.util.WavefrontOBJLoader;

/**
 * @author thehutch
 */
public class ModelLoader extends ResourceLoader<ModelData> {
	private final IFileSystem fileSystem;

	public ModelLoader(IFileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	@Override
	public void dispose() {
		this.resources.values().forEach((ModelData data) -> {
			data.dispose();
		});
	}

	@Override
	public ModelData load(Path path) {
		try (final Stream<String> lines = Files.lines(path)) {
			final ModelData data = new ModelData();
			lines.forEach((String line) -> {
				if (line.startsWith("mesh")) {
					data.mesh = WavefrontOBJLoader.load(getPath(line));
				} else if (line.startsWith("texture")) {
					data.texture = fileSystem.getResource(getPath(line));
				}
			});
			// Add the resource to the resource cache
			this.resources.put(path, data);
			return data;
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load model: " + path, ex);
		}
	}

	@Override
	public void unload(Path path) {
		final ModelData resource = resources.get(path);
		if (resource != null) {
			// Dispose of the resource
			resource.dispose();
			// Remove the resource from the cache
			this.resources.remove(path);
		}
	}

	private static Path getPath(String line) {
		return FileSystem.DATA_DIRECTORY.resolve(line.substring(line.indexOf(':', 0) + 1).trim());
	}
}
