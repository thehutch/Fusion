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

import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

import java.nio.ByteBuffer;
import me.thehutch.fusion.api.util.Disposable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 *
 * @author thehutch
 */
public class Texture implements Disposable {
	private final int id;

	public Texture(ByteBuffer buffer, int width, int height, int components) {
		// Generate a texture handle
		this.id = GL11.glGenTextures();
		// Bind the texture
		GL11.glBindTexture(GL_TEXTURE_2D, id);

		// Set the texture parameters
		GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glTexImage2D(GL_TEXTURE_2D, 0, components == 4 ? GL_RGBA8 : GL_RGB8, width, height, 0, components == 4 ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE, buffer);

		// Unbind the texture
		GL11.glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void bind(int unit) {
		if (unit >= 0) {
			GL13.glActiveTexture(GL_TEXTURE0 + unit);
		}
		GL11.glBindTexture(GL_TEXTURE_2D, id);
	}

	@Override
	public void dispose() {
		// Delete the texture
		GL11.glDeleteTextures(id);
	}
}
