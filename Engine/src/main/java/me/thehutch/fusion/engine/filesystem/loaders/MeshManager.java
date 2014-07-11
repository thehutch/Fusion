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
import java.nio.file.Path;
import me.thehutch.fusion.api.filesystem.IResourceManager;
import me.thehutch.fusion.engine.filesystem.FileSystem;
import me.thehutch.fusion.engine.render.opengl.Mesh;
import me.thehutch.fusion.engine.util.WavefrontOBJLoader;

/**
 * @author thehutch
 */
public class MeshManager implements IResourceManager<Mesh> {
	private final TMap<Path, Mesh> meshes = new THashMap<>();

	public MeshManager() {
	}

	@Override
	public Mesh get(Path path, boolean load) {
		final Mesh mesh = meshes.get(path);
		if (mesh == null && load) {
			return load(path);
		}
		return mesh;
	}

	@Override
	public boolean isLoaded(Path path) {
		return get(path, false) != null;
	}

	@Override
	public Mesh load(Path path) {
		final Mesh mesh = WavefrontOBJLoader.load(path);
		if (mesh != null) {
			this.meshes.put(path, mesh);
		}
		return mesh;
	}

	@Override
	public void unload(Path path) {
		final Mesh mesh = meshes.remove(path);
		if (mesh != null) {
			mesh.dispose();
		}
	}

	@Override
	public void dispose() {
		this.meshes.forEachValue((Mesh mesh) -> {
			mesh.dispose();
			return true;
		});
		this.meshes.clear();
	}

	private static Path getPath(String line) {
		return FileSystem.DATA_DIRECTORY.resolve(line.substring(line.indexOf(':', 0) + 1).trim());
	}
}
