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

import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;

import me.thehutch.fusion.api.util.GLVersion;
import me.thehutch.fusion.engine.render.opengl.RenderBuffer;
import me.thehutch.fusion.engine.render.texture.InternalFormat;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.opengl.GL30;

/**
 * @author thehutch
 */
public class OpenGL30RenderBuffer extends RenderBuffer {

	@Override
	public void create() {
		ensureNotCreated("Renderbuffer is already created.");
		// Generate the render buffer
		this.id = GL30.glGenRenderbuffers();
		// Check for errors
		RenderUtil.checkGLError();
		super.create();
	}

	@Override
	public void dispose() {
		ensureCreated("Renderbuffer must be created to dispose.");
		// Delete the render buffer
		GL30.glDeleteRenderbuffers(id);
		// Check for errors
		RenderUtil.checkGLError();
		super.dispose();
	}

	@Override
	public void bind() {
		ensureCreated("Renderbuffer must be created to bind.");
		GL30.glBindRenderbuffer(GL_RENDERBUFFER, id);
	}

	@Override
	public void unbind() {
		GL30.glBindRenderbuffer(GL_RENDERBUFFER, 0);
	}

	@Override
	public void setStorage(int width, int height, InternalFormat format) {
		ensureCreated("Renderbuffer must be created to set storage");
		GL30.glBindRenderbuffer(GL_RENDERBUFFER, id);
		GL30.glRenderbufferStorage(GL_RENDERBUFFER, format.getGLConstant(), width, height);
	}

	@Override
	public GLVersion getGLVersion() {
		return GLVersion.GL30;
	}
}
