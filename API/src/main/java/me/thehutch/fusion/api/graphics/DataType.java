/*
 * This file is part of API.
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
package me.thehutch.fusion.api.graphics;

/**
 * @author thehutch
 */
public enum DataType {
	BYTE(0x1400, 1, true),
	UNSIGNED_BYTE(0x1401, 1, true),
	SHORT(0x1402, 2, true),
	UNSIGNED_SHORT(0x1403, 2, true),
	INT(0x1404, 4, true),
	UNSIGNED_INT(0x1405, 4, true),
	HALF_FLOAT(0x140B, 2, false),
	FLOAT(0x1406, 4, false),
	DOUBLE(0x140A, 8, false);
	private final int glConstant;
	private final int byteSize;
	private final boolean isInteger;

	private DataType(int glConstant, int byteSize, boolean isInteger) {
		this.glConstant = glConstant;
		this.byteSize = byteSize;
		this.isInteger = isInteger;
	}

	/**
	 * Gets the OpenGL constant of this data type.
	 *
	 * @return The OpenGL constant
	 */
	public int getGLConstant() {
		return glConstant;
	}

	/**
	 * Get the number of bytes used in this data type.
	 *
	 * @return The number of bytes
	 */
	public int getByteSize() {
		return byteSize;
	}

	/**
	 * Returns true if the data type is an integer.
	 *
	 * @return True if the data type is an integer
	 */
	public boolean isInteger() {
		return isInteger;
	}
}
