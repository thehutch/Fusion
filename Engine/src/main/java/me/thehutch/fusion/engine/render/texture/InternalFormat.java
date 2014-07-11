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

import static me.thehutch.fusion.engine.render.DataType.UNSIGNED_BYTE;
import static me.thehutch.fusion.engine.render.DataType.UNSIGNED_INT;
import static me.thehutch.fusion.engine.render.DataType.UNSIGNED_SHORT;
import static me.thehutch.fusion.engine.render.texture.Format.BGR;
import static me.thehutch.fusion.engine.render.texture.Format.DEPTH;
import static me.thehutch.fusion.engine.render.texture.Format.RED;
import static me.thehutch.fusion.engine.render.texture.Format.RGB;
import static me.thehutch.fusion.engine.render.texture.Format.RGBA;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT16;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.GL_R16;
import static org.lwjgl.opengl.GL30.GL_R8;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.awt.image.BufferedImage;
import me.thehutch.fusion.engine.render.DataType;

/**
 * @author thehutch
 */
public enum InternalFormat {
	R8(GL_R8, RED, UNSIGNED_BYTE),
	R16(GL_R16, RED, UNSIGNED_SHORT),
	RGB8(GL_RGB8, RGB, UNSIGNED_BYTE),
	BGR8(GL_RGB8, BGR, UNSIGNED_BYTE),
	RGBA8(GL_RGBA8, RGBA, UNSIGNED_BYTE),
	DEPTH_COMPONENT16(GL_DEPTH_COMPONENT16, DEPTH, UNSIGNED_SHORT),
	DEPTH_COMPONENT24(GL_DEPTH_COMPONENT24, DEPTH, UNSIGNED_INT),
	DEPTH_COMPONENT32(GL_DEPTH_COMPONENT32, DEPTH, UNSIGNED_INT);
	/**
	 * Formats mapped to BufferedImage types.
	 */
	private static final TIntObjectMap<InternalFormat> FORMATS = new TIntObjectHashMap<>();

	static {
		FORMATS.put(BufferedImage.TYPE_INT_RGB, RGB8);
		FORMATS.put(BufferedImage.TYPE_INT_ARGB, RGBA8);
		FORMATS.put(BufferedImage.TYPE_BYTE_GRAY, R8);
		FORMATS.put(BufferedImage.TYPE_USHORT_GRAY, R16);
		FORMATS.put(BufferedImage.TYPE_3BYTE_BGR, BGR8);
	}

	/**
	 * The OpenGL constant.
	 */
	private final int glConstant;
	/**
	 * The format.
	 */
	private final Format format;
	/**
	 * The data type.
	 */
	private final DataType type;

	private InternalFormat(int glConstant, Format format, DataType type) {
		this.glConstant = glConstant;
		this.format = format;
		this.type = type;
	}

	/**
	 * Gets the OpenGL constant of the internal format.
	 *
	 * @return The OpenGL constant
	 */
	public int getGLConstant() {
		return glConstant;
	}

	/**
	 * Gets the format of the internal format.
	 *
	 * @return The format
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * Gets the data type of the internal format.
	 *
	 * @return The data type
	 */
	public DataType getDataType() {
		return type;
	}

	/**
	 * Converts the BufferedImage image types to an internal format.
	 *
	 * @param type The type to convert
	 *
	 * @return The equivalent internal format
	 */
	public static InternalFormat getFromBufferedImage(int type) {
		return FORMATS.get(type);
	}
}
