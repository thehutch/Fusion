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
public interface ITexture {
	/**
	 * Get the width of the texture.
	 *
	 * @return Width of the texture
	 */
	public int getWidth();

	/**
	 * Get the height of the texture.
	 *
	 * @return Height of the texture
	 */
	public int getHeight();

	/**
	 * Get the format of the texture.
	 *
	 * @return The format
	 */
	public Format getFormat();

	/**
	 * Get the internal format of the texture.
	 *
	 * @return The internal format
	 */
	public InternalFormat getInternalFormat();

	public static enum Format {
		RED(0x1903, 1, true, false, false, false, false, false),
		RGB(0x1907, 3, true, true, true, false, false, false),
		RGBA(0x1908, 4, true, true, true, true, false, false),
		DEPTH(0x1902, 1, false, false, false, false, true, false),
		RG(0x8227, 2, true, true, false, false, false, false),
		DEPTH_STENCIL(0x84F9, 1, false, false, false, false, false, true);
		private final int glConstant;
		private final int components;
		private final boolean hasRed;
		private final boolean hasGreen;
		private final boolean hasBlue;
		private final boolean hasAlpha;
		private final boolean hasDepth;
		private final boolean hasStencil;

		private Format(int glConstant, int components, boolean hasRed, boolean hasGreen, boolean hasBlue, boolean hasAlpha, boolean hasDepth, boolean hasStencil) {
			this.glConstant = glConstant;
			this.components = components;
			this.hasRed = hasRed;
			this.hasGreen = hasGreen;
			this.hasBlue = hasBlue;
			this.hasAlpha = hasAlpha;
			this.hasDepth = hasDepth;
			this.hasStencil = hasStencil;
		}

		public int getGLConstant() {
			return glConstant;
		}

		public int getNumComponents() {
			return components;
		}

		public boolean hasRed() {
			return hasRed;
		}

		public boolean hasGreen() {
			return hasGreen;
		}

		public boolean hasBlue() {
			return hasBlue;
		}

		public boolean hasAlpha() {
			return hasAlpha;
		}

		public boolean hasDepth() {
			return hasDepth;
		}

		public boolean hasStencil() {
			return hasStencil;
		}
	}

	public static enum InternalFormat {
		RGB8(0x8051, Format.RGB, DataType.UNSIGNED_BYTE),
		RGBA8(0x8058, Format.RGBA, DataType.UNSIGNED_BYTE),
		RGB16(32852, Format.RGB, DataType.UNSIGNED_SHORT),
		RGBA16(0x805B, Format.RGBA, DataType.UNSIGNED_SHORT),
		DEPTH_COMPONENT16(0x81A5, Format.DEPTH, DataType.UNSIGNED_SHORT),
		DEPTH_COMPONENT24(0x81A6, Format.DEPTH, DataType.UNSIGNED_INT),
		DEPTH_COMPONENT32(0x81A7, Format.DEPTH, DataType.UNSIGNED_INT),
		R8(0x8229, Format.RED, DataType.UNSIGNED_BYTE),
		R16(0x822A, Format.RED, DataType.UNSIGNED_SHORT),
		RG8(0x822B, Format.RG, DataType.UNSIGNED_BYTE),
		RG16(0x822C, Format.RG, DataType.UNSIGNED_SHORT),
		R16F(0x822D, Format.RED, DataType.HALF_FLOAT),
		R32F(0x822E, Format.RED, DataType.FLOAT),
		RG16F(0x822F, Format.RG, DataType.HALF_FLOAT),
		RG32F(0x8230, Format.RGB, DataType.FLOAT),
		RGBA32F(0x8814, Format.RGBA, DataType.FLOAT),
		RGB32F(0x8815, Format.RGB, DataType.FLOAT),
		RGBA16F(0x881A, Format.RGBA, DataType.HALF_FLOAT),
		RGB16F(0x881B, Format.RGB, DataType.HALF_FLOAT);
		private final int glConstant;
		private final int bytes;
		private final Format format;
		private final DataType componentType;

		private InternalFormat(int glConstant, Format format, DataType componentType) {
			this.glConstant = glConstant;
			this.format = format;
			this.componentType = componentType;
			this.bytes = format.getNumComponents() * componentType.getByteSize();
		}

		/**
		 * Gets the OpenGL constant for this internal format.
		 *
		 * @return The OpenGL Constant
		 */
		public int getGLConstant() {
			return glConstant;
		}

		/**
		 * Returns the format associated to this internal format.
		 *
		 * @return The associated format
		 */
		public Format getFormat() {
			return format;
		}

		/**
		 * Returns the number of components in the format.
		 *
		 * @return The number of components
		 */
		public int getNumComponents() {
			return format.getNumComponents();
		}

		/**
		 * Returns the data type of the components.
		 *
		 * @return The component type
		 */
		public DataType getComponentType() {
			return componentType;
		}

		/**
		 * Returns the number of bytes used by a single pixel in the format.
		 *
		 * @return The number of bytes for a pixel
		 */
		public int getBytes() {
			return bytes;
		}

		/**
		 * Returns the number of bytes used by a single pixel component in the format.
		 *
		 * @return The number of bytes for a pixel component
		 */
		public int getBytesPerComponent() {
			return componentType.getByteSize();
		}

		/**
		 * Returns true if this format has a red component.
		 *
		 * @return True if a red component is present
		 */
		public boolean hasRed() {
			return format.hasRed();
		}

		/**
		 * Returns true if this format has a green component.
		 *
		 * @return True if a green component is present
		 */
		public boolean hasGreen() {
			return format.hasGreen();
		}

		/**
		 * Returns true if this format has a blue component.
		 *
		 * @return True if a blue component is present
		 */
		public boolean hasBlue() {
			return format.hasBlue();
		}

		/**
		 * Returns true if this format has an alpha component.
		 *
		 * @return True if an alpha component is present
		 */
		public boolean hasAlpha() {
			return format.hasAlpha();
		}

		/**
		 * Returns true if this format has a depth component.
		 *
		 * @return True if a depth component is present
		 */
		public boolean hasDepth() {
			return format.hasDepth();
		}
	}

	public static enum WrapMode {
		REPEAT(0x2901),
		CLAMP_TO_EDGE(0x812F),
		CLAMP_TO_BORDER(0x812D),
		MIRRORED_REPEAT(0x8370);
		private final int glConstant;

		private WrapMode(int glConstant) {
			this.glConstant = glConstant;
		}

		/**
		 * Gets the OpenGL constant for this texture wrap.
		 *
		 * @return The OpenGL constant
		 */
		public int getGLConstant() {
			return glConstant;
		}
	}

	public static enum FilterMode {
		LINEAR(0x2601, false),
		NEAREST(0x2600, false),
		NEAREST_MIPMAP_NEAREST(0x2700, true),
		LINEAR_MIPMAP_NEAREST(0x2701, true),
		NEAREST_MIPMAP_LINEAR(0x2702, true),
		LINEAR_MIPMAP_LINEAR(0x2703, true);
		private final int glConstant;
		private final boolean mimpaps;

		private FilterMode(int glConstant, boolean mimpaps) {
			this.glConstant = glConstant;
			this.mimpaps = mimpaps;
		}

		/**
		 * Gets the OpenGL constant for this texture filter.
		 *
		 * @return The OpenGL Constant
		 */
		public int getGLConstant() {
			return glConstant;
		}

		/**
		 * Returns true if the filtering mode requires generation of mipmaps.
		 *
		 * @return Whether or not mipmaps are required
		 */
		public boolean needsMipMaps() {
			return mimpaps;
		}
	}

	public static enum CompareMode {
		LEQUAL(0x203),
		GEQUAL(0x206),
		LESS(0x201),
		GREATER(0x204),
		EQUAL(0x202),
		NOTEQUAL(0x205),
		ALWAYS(0x206),
		NEVER(0x200);
		private final int glConstant;

		private CompareMode(int glConstant) {
			this.glConstant = glConstant;
		}

		/**
		 * Gets the OpenGL constant for this texture filter.
		 *
		 * @return The OpenGL constant
		 */
		public int getGLConstant() {
			return glConstant;
		}
	}
}
