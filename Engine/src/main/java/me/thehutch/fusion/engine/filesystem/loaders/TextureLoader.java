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
package me.thehutch.fusion.engine.filesystem.loaders;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL12.GL_BGR;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.imageio.ImageIO;
import me.thehutch.fusion.api.filesystem.ResourceLoader;
import me.thehutch.fusion.engine.render.Texture;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * @author thehutch
 */
public class TextureLoader extends ResourceLoader<Texture> {

	@Override
	public void dispose() {
		this.resources.clear();
	}

	@Override
	protected Texture load(Path path) {
		// Open a stream to the resource
		try (final InputStream stream = Files.newInputStream(path, StandardOpenOption.READ)) {
			// Read the image into a buffered image
			final BufferedImage image = ImageIO.read(stream);

			// Obtain the image dimensions
			final int width = image.getWidth();
			final int height = image.getHeight();
			final int bpp = image.getColorModel().getNumComponents();
			final boolean hasAlpha = image.getColorModel().hasAlpha();

			// Get the pixels from the buffered image
			final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

			// Flip the pixels
			final byte[] flippedPixels = new byte[pixels.length];
			for (int h1 = 0, h2 = height - 1; h2 >= 0; ++h1, --h2) {
				System.arraycopy(pixels, h2 * width * bpp, flippedPixels, h1 * width * bpp, width * bpp);
			}

			// Convert the flipped pixels array into a byte buffer
			final ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
			buffer.put(flippedPixels);
			buffer.flip();

			// Determine the texture formats
			final int internalFormat;
			final int format;
			if (hasAlpha) {
				internalFormat = GL_RGBA8;
				format = GL_RGBA;
			} else {
				internalFormat = GL_RGB8;
				format = GL_BGR;
			}

			// Create the texture
			final Texture texture = new Texture();
			texture.setWrapMode(GL_REPEAT, GL_REPEAT);
			texture.setFiltering(GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR);
			texture.setAnisotropicFiltering(GL11.glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
			texture.setTextureData(buffer, width, height, internalFormat, format, GL_UNSIGNED_BYTE);
			texture.unbind();
			// Add the texture to the cache
			this.resources.put(path, texture);

			// Check for errors
			RenderUtil.checkGLError();
			return texture;
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load texture: " + path, ex);
		}
	}
}
