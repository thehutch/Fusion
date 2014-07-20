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
package me.thehutch.fusion.engine.render.opengl.gl30;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.nio.ByteBuffer;
import me.thehutch.fusion.api.util.GLVersion;
import me.thehutch.fusion.engine.render.opengl.gl20.OpenGL20Texture;
import me.thehutch.fusion.engine.render.texture.InternalFormat;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 * @author thehutch
 */
public class OpenGL30Texture extends OpenGL20Texture {

	@Override
	public void setPixelData(ByteBuffer pixels, InternalFormat format, int width, int height, boolean generateMipmaps) {
		ensureCreated("Texture must be created to set pixel data.");
		// Upload the pixel data to the GPU
		GL11.glTexImage2D(GL_TEXTURE_2D, 0, format.getGLConstant(), width, height, 0, format.getFormat().getGLConstant(), format.getDataType().getGLConstant(), pixels);
		// Check if mipmaps should be generated for this texture
		if (generateMipmaps) {
			GL30.glGenerateMipmap(GL_TEXTURE_2D);
		}
	}

	@Override
	public GLVersion getGLVersion() {
		return GLVersion.GL30;
	}
}
