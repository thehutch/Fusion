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
package me.thehutch.fusion.engine.render;

import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;

import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.opengl.GL30;

/**
 * @author thehutch
 */
public class RenderBuffer implements Disposable {
	private final int rbo;

	public RenderBuffer(int width, int height, int internalFormat) {
		// Generate and bind the render buffer
		this.rbo = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL_RENDERBUFFER, rbo);
		// Set the storage format and dimensions
		GL30.glRenderbufferStorage(GL_RENDERBUFFER, internalFormat, width, height);
		// Unbind the render buffer
		GL30.glBindRenderbuffer(GL_RENDERBUFFER, 0);

		// Check for errors
		RenderUtil.checkGLError(true);
	}

	public int getID() {
		return rbo;
	}

	public void bind() {
		GL30.glBindRenderbuffer(GL_RENDERBUFFER, rbo);
	}

	public void unbind() {
		GL30.glBindRenderbuffer(GL_RENDERBUFFER, 0);
	}

	@Override
	public void dispose() {
		GL30.glDeleteRenderbuffers(rbo);
		// Check for errors
		RenderUtil.checkGLError(true);
	}
}
