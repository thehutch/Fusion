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

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL12.GL_BGR;
import static org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL;

/**
 * @author thehutch
 */
public enum Format {
	RED(GL_RED, 1),
	RGB(GL_RGB, 3),
	BGR(GL_BGR, 3),
	RGBA(GL_RGBA, 4),
	DEPTH(GL_DEPTH_COMPONENT, 1),
	DEPTH_STENCIL(GL_DEPTH_STENCIL, 1);
	/**
	 * The OpenGL constant.
	 */
	private final int glConstant;
	/**
	 * The number of components.
	 */
	private final int numComponents;

	private Format(int glConstant, int numComponents) {
		this.glConstant = glConstant;
		this.numComponents = numComponents;
	}

	/**
	 * Gets the OpenGL constant for this format.
	 *
	 * @return The OpenGL constant
	 */
	public int getGLConstant() {
		return glConstant;
	}

	/**
	 * Gets the number of components this format has.
	 *
	 * @return The number of components
	 */
	public int getNumComponents() {
		return numComponents;
	}
}
