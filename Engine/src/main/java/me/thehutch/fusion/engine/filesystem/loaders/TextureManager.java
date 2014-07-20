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
import me.thehutch.fusion.engine.filesystem.loaders.ImageLoader.ImageData;
import me.thehutch.fusion.engine.render.opengl.Texture;
import me.thehutch.fusion.engine.render.texture.CompareFunc;
import me.thehutch.fusion.engine.render.texture.FilterMode;
import me.thehutch.fusion.engine.render.texture.WrapMode;
import me.thehutch.fusion.engine.util.RenderUtil;

/**
 * @author thehutch
 */
public class TextureManager implements IResourceManager<Texture> {
	private static final String ANISOTROPIC_FILTERING_ATTRIBUTE = "ANISOTROPIC_FILTERING";
	private static final String COMPARE_FUNC_ATTRIBUTE = "COMPARE_FUNC";
	private static final String IMAGE_DATA_ATTRIBUTE = "IMAGE_DATA";
	private static final String MIN_FILTER_ATTRIBUTE = "MIN_FILTER";
	private static final String MAG_FILTER_ATTRIBUTE = "MAG_FILTER";
	private static final String S_WRAP_ATTRIBUTE = "WRAP_S";
	private static final String T_WRAP_ATTRIBUTE = "WRAP_T";

	private final TMap<Path, Texture> textures = new THashMap<>();
	private final Client engine;

	public TextureManager(Client engine) {
		this.engine = engine;
	}

	@Override
	public Texture get(Path path, boolean load) {
		final Texture texture = textures.get(path);
		if (texture == null && load) {
			return load(path);
		}
		return texture;
	}

	@Override
	public boolean isLoaded(Path path) {
		return get(path, false) != null;
	}

	@Override
	public Texture load(Path path) {
		// Open the texture file and read its attributes
		try (final Stream<String> stream = Files.lines(path)) {

			final TMap<String, String> values = new THashMap<>(17, 0.9f);

			stream.forEachOrdered((String line) -> {
				if (line.startsWith(ANISOTROPIC_FILTERING_ATTRIBUTE)) {
					values.put(ANISOTROPIC_FILTERING_ATTRIBUTE, getValue(line, false));
				} else if (line.startsWith(COMPARE_FUNC_ATTRIBUTE)) {
					values.put(COMPARE_FUNC_ATTRIBUTE, getValue(line, true));
				} else if (line.startsWith(IMAGE_DATA_ATTRIBUTE)) {
					values.put(IMAGE_DATA_ATTRIBUTE, getValue(line, false));
				} else if (line.startsWith(MIN_FILTER_ATTRIBUTE)) {
					values.put(MIN_FILTER_ATTRIBUTE, getValue(line, true));
				} else if (line.startsWith(MAG_FILTER_ATTRIBUTE)) {
					values.put(MAG_FILTER_ATTRIBUTE, getValue(line, true));
				} else if (line.startsWith(S_WRAP_ATTRIBUTE)) {
					values.put(S_WRAP_ATTRIBUTE, getValue(line, true));
				} else if (line.startsWith(T_WRAP_ATTRIBUTE)) {
					values.put(T_WRAP_ATTRIBUTE, getValue(line, true));
				}
			});

			// Create a texture
			final Texture texture = engine.getContext().newTexture();
			texture.create();
			texture.bind();

			// Set the anisotropic filtering
			final float anisotropicFiltering = Float.parseFloat(values.get(ANISOTROPIC_FILTERING_ATTRIBUTE));
			texture.setAnisotropicFiltering(anisotropicFiltering);

			// Set the min and max filters
			final FilterMode minFilter = FilterMode.valueOf(values.get(MIN_FILTER_ATTRIBUTE));
			final FilterMode magFilter = FilterMode.valueOf(values.get(MAG_FILTER_ATTRIBUTE));
			texture.setFiltering(minFilter, magFilter);

			// Set the S and T wrap modes
			final WrapMode wrapS = WrapMode.valueOf(values.get(S_WRAP_ATTRIBUTE));
			final WrapMode wrapT = WrapMode.valueOf(values.get(T_WRAP_ATTRIBUTE));
			texture.setWrapMode(wrapS, wrapT);

			// Set the compare function
			final CompareFunc compareFunc = CompareFunc.valueOf(values.get(COMPARE_FUNC_ATTRIBUTE));
			texture.setCompareFunc(compareFunc);

			// Set the texture image data
			final ImageData imageData = engine.getFileSystem().getResource(FileSystem.DATA_DIRECTORY.resolve(values.get(IMAGE_DATA_ATTRIBUTE)));
			texture.setPixelData(imageData, minFilter.requiresMipmaps());

			// Unbind the texture
			texture.unbind();

			// Check for errors
			RenderUtil.checkGLError();

			// Return a successful texture load
			return texture;
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load texture: " + path, ex);
		}
	}

	@Override
	public void unload(Path path) {
		final Texture texture = textures.remove(path);
		if (texture != null) {
			texture.dispose();
		}
	}

	@Override
	public void dispose() {
		this.textures.forEachValue((Texture texture) -> {
			texture.dispose();
			return true;
		});
		this.textures.clear();
	}

	private static String getValue(String line, boolean toUpper) {
		final String value = line.substring(line.indexOf(':', 0) + 1).trim();
		return toUpper ? value.toUpperCase() : value;
	}
}
