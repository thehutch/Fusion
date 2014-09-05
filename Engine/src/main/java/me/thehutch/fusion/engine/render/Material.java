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
package me.thehutch.fusion.engine.render;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import me.thehutch.fusion.engine.render.data.UniformHolder;
import me.thehutch.fusion.engine.render.opengl.Program;
import me.thehutch.fusion.engine.render.opengl.Texture;

/**
 * @author thehutch
 */
public class Material implements Comparable<Material> {
	private static final AtomicInteger COUNTER = new AtomicInteger();
	private final int id = COUNTER.getAndIncrement();
	private final UniformHolder uniforms = new UniformHolder();
	private TIntObjectMap<Texture> textures;
	private Program program;

	public Material(Program program) {
		setProgram(program);
	}

	/**
	 * Returns the material's program.
	 *
	 * @return The program
	 */
	public Program getProgram() {
		return program;
	}

	/**
	 * Sets the material's program.
	 *
	 * @param program The program
	 */
	public void setProgram(Program program) {
		if (program == null) {
			throw new IllegalArgumentException("Program can not be null");
		}
		program.ensureCreated("Program must be created to attach to a material");
		this.program = program;
	}

	/**
	 * Binds the material to the OpenGL context.
	 */
	public void bind() {
		this.program.bind();
		if (textures != null) {
			this.textures.forEachEntry((int key, Texture value) -> {
				// Bind the texture
				value.bind(key);
				// Set the uniform
				program.bindSampler(key);
				return true;
			});
		}
	}

	/**
	 * Unbinds the material from the OpenGL context.
	 */
	public void unbind() {
		if (textures != null) {
			this.textures.forEachValue((Texture texture) -> {
				texture.unbind();
				return true;
			});
		}
	}

	/**
	 * Returns the uniforms used in this material.
	 *
	 * @return The uniforms
	 */
	public UniformHolder getUniforms() {
		return uniforms;
	}

	/**
	 * Uploads all the uniforms of this material to the program.
	 */
	public void uploadUniforms() {
		this.program.upload(uniforms);
	}

	/**
	 * Adds the texture to the material at the given texture unit.
	 *
	 * @param unit    The texture unit
	 * @param texture The texture
	 */
	public void addTexture(int unit, Texture texture) {
		// Make sure the texture is not null
		if (texture == null) {
			throw new IllegalArgumentException("Texture can not be null");
		}
		// Make sure the texture unit is valid
		if (unit < 0) {
			throw new IllegalArgumentException("Texture unit must be greater than or equal to 0");
		}
		texture.ensureCreated("Texture must be created to add to material.");
		// Create the textures map if not already created
		if (textures == null) {
			this.textures = new TIntObjectHashMap<>();
		}
		this.textures.put(unit, texture);
	}

	/**
	 * Removes the texture from material at the given texture unit.
	 *
	 * @param unit The texture unit
	 */
	public void removeTexture(int unit) {
		if (textures != null) {
			this.textures.remove(unit);
		}
	}

	/**
	 * Returns the texture at the given texture unit.
	 *
	 * @param unit The texture unit
	 *
	 * @return The texture, null if not found
	 */
	public Texture getTexture(int unit) {
		return textures != null ? textures.get(unit) : null;
	}

	/**
	 * Checks whether this material has a texture at the given texture unit.
	 *
	 * @param unit The texture unit
	 *
	 * @return True if found
	 */
	public boolean hasTexture(int unit) {
		return textures != null && textures.containsKey(unit);
	}

	@Override
	public int compareTo(Material material) {
		return id - material.id;
	}
}
