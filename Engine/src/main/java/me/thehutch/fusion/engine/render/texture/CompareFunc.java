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

import static org.lwjgl.opengl.GL11.GL_ALWAYS;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_GEQUAL;
import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_NEVER;
import static org.lwjgl.opengl.GL11.GL_NOTEQUAL;

/**
 * @author thehutch
 */
public enum CompareFunc {
	LEQUAL(GL_LEQUAL),
	GEQUAL(GL_GEQUAL),
	LESS(GL_LESS),
	GREATER(GL_GREATER),
	EQUAL(GL_EQUAL),
	NOTEQUAL(GL_NOTEQUAL),
	ALWAYS(GL_ALWAYS),
	NEVER(GL_NEVER);
	/**
	 * The OpenGL constant.
	 */
	private final int glConstant;

	private CompareFunc(int glConstants) {
		this.glConstant = glConstants;
	}

	/**
	 * Gets the OpenGL constant for the compare function.
	 *
	 * @return The OpenGL constant
	 */
	public int getGLConstant() {
		return glConstant;
	}
}
