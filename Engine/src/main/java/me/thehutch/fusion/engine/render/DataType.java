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

import static org.lwjgl.opengl.GL11.GL_BYTE;
import static org.lwjgl.opengl.GL11.GL_DOUBLE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_SHORT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;

/**
 * @author thehutch
 */
public enum DataType {
	BYTE(GL_BYTE, 1),
	UNSIGNED_BYTE(GL_UNSIGNED_BYTE, 1),
	SHORT(GL_SHORT, 2),
	UNSIGNED_SHORT(GL_UNSIGNED_SHORT, 2),
	INT(GL_INT, 4),
	UNSIGNED_INT(GL_UNSIGNED_INT, 4),
	FLOAT(GL_FLOAT, 4),
	DOUBLE(GL_DOUBLE, 8);
	/**
	 * The OpenGL constant.
	 */
	private final int glConstant;
	/**
	 * The size in bytes.
	 */
	private final int byteSize;

	private DataType(int glConstant, int byteSize) {
		this.glConstant = glConstant;
		this.byteSize = byteSize;
	}

	/**
	 * Gets the OpenGL constant for this data type.
	 *
	 * @return The OpenGL constant
	 */
	public int getGLConstant() {
		return glConstant;
	}

	/**
	 * Gets the size of the data type in bytes.
	 *
	 * @return The size of the data type
	 */
	public int getByteSize() {
		return byteSize;
	}
}
