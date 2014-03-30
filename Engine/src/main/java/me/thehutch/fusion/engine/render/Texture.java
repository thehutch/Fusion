/*
 * This file is part of Engine.
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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.thehutch.fusion.engine.render;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import me.thehutch.fusion.api.graphics.DataType;
import me.thehutch.fusion.api.graphics.ITexture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 *
 * @author thehutch
 */
public class Texture implements ITexture {
	private final Format format;
	private final int width;
	private final int height;
	private final int id;

	public Texture(InputStream stream, Format format) {
		this.format = format;
		try {
			final BufferedImage image = ImageIO.read(stream);
			this.width = image.getWidth();
			this.height = image.getHeight();

			final int type = image.getType();
			final int[] pixels;
			if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
				pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
			} else {
				pixels = new int[width * height];
				image.getRGB(0, 0, width, height, pixels, 0, width);
			}

			final ByteBuffer data = BufferUtils.createByteBuffer(width * height * format.getNumComponents());
			for (int h = height - 1; h >= 0; --h) {
				for (int w = 0; w < width; ++w) {
					final int pixel = pixels[w + h * width];
					if (format.hasRed()) {
						data.put((byte) ((pixel >> 16) & 0xFF));
					}
					if (format.hasGreen()) {
						data.put((byte) ((pixel >> 8) & 0xFF));
					}
					if (format.hasBlue()) {
						data.put((byte) (pixel & 0xFF));
					}
					if (format.hasAlpha()) {
						data.put((byte) ((pixel >> 24) & 0xFF));
					}
				}
			}
			data.flip();

			// Generate a texture handle
			this.id = GL11.glGenTextures();
			// Bind the texture
			GL11.glBindTexture(GL_TEXTURE_2D, id);

			// Set the texture parameters
			GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

			GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

			GL11.glTexImage2D(GL_TEXTURE_2D, 0, format.getGLConstant(), width, height, 0, format.getGLConstant(), DataType.UNSIGNED_BYTE.getGLConstant(), data);

			// Unbind the texture
			GL11.glBindTexture(GL_TEXTURE_2D, 0);

		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to read texture image data", ex);
		}
	}

	public void bind(int unit) {
		if (unit >= 0) {
			GL13.glActiveTexture(GL_TEXTURE0 + unit);
		}
		GL11.glBindTexture(GL_TEXTURE_2D, id);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Format getFormat() {
		return format;
	}

	@Override
	public InternalFormat getInternalFormat() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
