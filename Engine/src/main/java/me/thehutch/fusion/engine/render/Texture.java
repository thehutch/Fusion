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
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

import java.nio.ByteBuffer;
import me.thehutch.fusion.api.util.Disposable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * @author thehutch
 */
public class Texture implements Disposable {
	private final int texture;

	public Texture() {
		// Generate and bind the texture
		this.texture = GL11.glGenTextures();
		GL11.glBindTexture(GL_TEXTURE_2D, texture);
	}

	public int getID() {
		return texture;
	}

	public void bind(int unit) {
		if (unit >= 0) {
			GL13.glActiveTexture(GL_TEXTURE0 + unit);
		}
		GL11.glBindTexture(GL_TEXTURE_2D, texture);
	}

	public void unbind() {
		GL11.glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void setWrapMode(int wrapS, int wrapT) {
		// Set the texture wrap parameters
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT);
	}

	public void setFiltering(int minFilter, int magFilter) {
		// Set the texture filter parameters
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);
	}

	public void setTextureData(ByteBuffer pixels, int width, int height, int internalFormat, int format) {
		// Upload the pixel data to the GPU
		GL11.glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, pixels);
	}

	@Override
	public void dispose() {
		// Delete the texture
		GL11.glDeleteTextures(texture);
	}
}
