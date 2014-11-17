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
package me.thehutch.fusion.engine.filesystem.loaders;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.imageio.ImageIO;
import me.thehutch.fusion.api.filesystem.IResourceManager;
import me.thehutch.fusion.engine.filesystem.loaders.ImageLoader.ImageData;
import me.thehutch.fusion.engine.render.texture.InternalFormat;
import org.lwjgl.BufferUtils;

/**
 * @author thehutch
 */
public class ImageLoader implements IResourceManager<ImageData> {
	public ImageLoader() {
	}

	@Override
	public ImageData get(Path path, boolean load) {
		return load ? load(path) : null;
	}

	@Override
	public boolean isLoaded(Path path) {
		return get(path, false) != null;
	}

	@Override
	public ImageData load(Path path) {
		// Open a stream to the resource
		try (final InputStream stream = Files.newInputStream(path, StandardOpenOption.READ)) {
			// Read the image into a buffered image
			final BufferedImage image = ImageIO.read(stream);

			// Obtain the image dimensions
			final int width = image.getWidth();
			final int height = image.getHeight();
			final int bpp = image.getColorModel().getNumComponents();

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

			return new ImageData(buffer, InternalFormat.getFromBufferedImage(image.getType()), width, height);
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load image: " + path, ex);
		}
	}

	@Override
	public void unload(Path path) {
	}

	@Override
	public void dispose() {
	}

	public class ImageData {
		public final ByteBuffer pixels;
		public final InternalFormat format;
		public final int width;
		public final int height;

		public ImageData(ByteBuffer pixels, InternalFormat format, int width, int height) {
			this.pixels = pixels;
			this.format = format;
			this.width = width;
			this.height = height;
		}
	}
}
