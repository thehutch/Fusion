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
package me.thehutch.fusion.engine.render.texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;

/**
 * @author thehutch
 */
public enum FilterMode {
	NEAREST(GL_NEAREST, false),
	LINEAR(GL_LINEAR, false),
	NEAREST_MIPMAP_NEAREST(GL_NEAREST_MIPMAP_NEAREST, true),
	LINEAR_MIPMAP_NEAREST(GL_LINEAR_MIPMAP_NEAREST, true),
	NEAREST_MIPMAP_LINEAR(GL_NEAREST_MIPMAP_LINEAR, true),
	LINEAR_MIPMAP_LINEAR(GL_LINEAR_MIPMAP_LINEAR, true);
	/**
	 * The OpenGL constant.
	 */
	private final int glConstant;
	/**
	 * Requires mipmaps
	 */
	private final boolean requiresMipmaps;

	private FilterMode(int glConstant, boolean requiresMipmaps) {
		this.glConstant = glConstant;
		this.requiresMipmaps = requiresMipmaps;
	}

	/**
	 * Gets the OpenGL constant for the filter mode.
	 *
	 * @return The OpenGL Constant
	 */
	public int getGLConstant() {
		return glConstant;
	}

	/**
	 * Returns true if the filtering mode requires mipmaps.
	 *
	 * @return Whether or not mipmaps are required
	 */
	public boolean requiresMipmaps() {
		return requiresMipmaps;
	}
}
