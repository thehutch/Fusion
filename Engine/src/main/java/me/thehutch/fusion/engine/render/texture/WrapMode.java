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

import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;

/**
 * @author thehutch
 */
public enum WrapMode {
	REPEAT(GL_REPEAT),
	CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE),
	CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER),
	MIRRORED_REPEAT(GL_MIRRORED_REPEAT);
	/**
	 * The OpenGL constant.
	 */
	private final int glConstant;

	private WrapMode(int glConstant) {
		this.glConstant = glConstant;
	}

	/**
	 * Gets the OpenGL constant for the wrap mode.
	 *
	 * @return The OpenGL Constant
	 */
	public int getGLConstant() {
		return glConstant;
	}
}
