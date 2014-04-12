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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.imageio.ImageIO;
import me.thehutch.fusion.api.filesystem.ResourceLoader;
import me.thehutch.fusion.engine.render.Texture;
import org.lwjgl.BufferUtils;

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
			final boolean hasAlpha = image.getColorModel().hasAlpha();

			// Get the pixel information
			final int type = image.getType();
			final int[] pixels;
			if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
				pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
			} else {
				pixels = new int[width * height];
				image.getRGB(0, 0, width, height, pixels, 0, width);
			}

			// Convert the pixels array into a byte buffer
			final ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * image.getColorModel().getNumComponents());
			if (hasAlpha) {
				loadRGBA(buffer, pixels, width, height);
			} else {
				loadRGB(buffer, pixels, width, height);
			}
			buffer.flip();

			// Create the texture
			final Texture texture = new Texture(buffer, width, height, hasAlpha);
			// Add the texture to the cache
			this.resources.put(path, texture);
			return texture;
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load texture: " + path, ex);
		}
	}

	private static void loadRGB(ByteBuffer buffer, int[] pixels, int width, int height) {
		int h, w;
		for (h = height - 1; h >= 0; --h) {
			for (w = 0; w < width; ++w) {
				final int pixel = pixels[w + h * width];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
			}
		}
	}

	private static void loadRGBA(ByteBuffer buffer, int[] pixels, int width, int height) {
		int h, w;
		for (h = height - 1; h >= 0; --h) {
			for (w = 0; w < width; ++w) {
				final int pixel = pixels[w + h * width];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
	}
}
