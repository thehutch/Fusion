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

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import me.thehutch.fusion.api.filesystem.IResourceManager;
import me.thehutch.fusion.engine.Client;
import me.thehutch.fusion.engine.filesystem.FileSystem;
import me.thehutch.fusion.engine.render.Material;
import me.thehutch.fusion.engine.render.opengl.Program;
import me.thehutch.fusion.engine.render.opengl.Texture;

/**
 * @author thehutch
 */
public class MaterialLoader implements IResourceManager<Material> {
	private final TMap<Path, Material> materials = new THashMap<>();
	private final FileSystem fileSystem;

	public MaterialLoader(Client engine) {
		this.fileSystem = engine.getFileSystem();
	}

	@Override
	public Material get(Path path, boolean load) {
		final Material material = materials.get(path);
		if (material == null && load) {
			return load(path);
		}
		return material;
	}

	@Override
	public boolean isLoaded(Path path) {
		return get(path, false) != null;
	}

	@Override
	public Material load(Path path) {
		try (Stream<String> stream = Files.lines(path)) {
			final TMap<String, String> values = new THashMap<>(17, 0.9f);

			// Load the values
			stream.forEachOrdered((String line) -> {
				if (line.startsWith("program")) {
					values.put("program", getValue(line));
				} else if (line.startsWith("diffuse")) {
					values.put("diffuse", getValue(line));
				}
			});
			// Load the material program
			final Program program = fileSystem.getResource(FileSystem.DATA_DIRECTORY.resolve(values.get("program")));
			// Load the diffuse texture
			final Texture diffuse = fileSystem.getResource(FileSystem.DATA_DIRECTORY.resolve(values.get("diffuse")));

			/*
			 * TODO: Load other textures (normal etc...)
			 */

			// Create the material
			final Material material = new Material(program);
			material.addTexture(0, diffuse);
			return material;
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load material: " + path, ex);
		}
	}

	@Override
	public void unload(Path path) {
		final Material material = materials.remove(path);
		if (material != null) {
			material.unbind();
		}
		this.materials.clear();
	}

	@Override
	public void dispose() {
		this.materials.forEachValue((Material material) -> {
			material.unbind();
			return true;
		});
		this.materials.clear();
	}

	private static String getValue(String line) {
		return line.substring(line.indexOf(':', 0) + 1).trim();
	}
}
