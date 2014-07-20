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
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;

import me.thehutch.fusion.api.util.GLVersion;
import me.thehutch.fusion.engine.render.opengl.FrameBuffer;
import me.thehutch.fusion.engine.render.opengl.RenderBuffer;
import me.thehutch.fusion.engine.render.opengl.Texture;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.opengl.GL30;

/**
 * @author thehutch
 */
public class OpenGL30FrameBuffer extends FrameBuffer {

	@Override
	public void create() {
		// Generate and bind the frame buffer
		this.fbo = GL30.glGenFramebuffers();
		// Check for errors
		RenderUtil.checkGLError();
		super.create();
	}

	@Override
	public void dispose() {
		// Delete the frame buffer
		GL30.glDeleteFramebuffers(fbo);
		// Clear the output buffers
		this.outputBuffers.clear();
		super.dispose();
	}

	@Override
	public void bind() {
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, fbo);
	}

	@Override
	public void unbind() {
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	@Override
	public void attachTexture(int point, Texture texture) {
		// Bind the frame buffer
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		// Bind the texture to the frame buffer
		GL30.glFramebufferTexture2D(GL_FRAMEBUFFER, point, GL_TEXTURE_2D, texture.getId(), 0);
		// Add the attachment to the outputs if it is a colour type
		if (isColourAttachment(point)) {
			this.outputBuffers.add(point);
		}
		// Update the output buffers
		updateOutputBuffers();
		// Check to see if the frame buffer is complete
		if (GL30.glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw new IllegalStateException("Unable to complete frame buffer!");
		}
		// Unbind the frame buffer
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, 0);

		// Check for errors
		RenderUtil.checkGLError();
	}

	@Override
	public void attachBuffer(int point, RenderBuffer buffer) {
		// Bind the frame buffer
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		// Attach the render buffer to the frame buffer
		GL30.glFramebufferRenderbuffer(GL_FRAMEBUFFER, point, GL_RENDERBUFFER, buffer.getId());
		RenderUtil.checkGLError();
		// Add the attachment to the outputs if it is a colour type
		if (isColourAttachment(point)) {
			this.outputBuffers.add(point);
		}
		// Update the output buffers
		updateOutputBuffers();
		// Check to see if the frame buffer is complete
		if (GL30.glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw new IllegalStateException("Unable to complete frame buffer!");
		}
		// Unbind the frame buffer
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, 0);

		// Check for errors
		RenderUtil.checkGLError();
	}

	@Override
	public void detach(int point) {
		// Bind the frame buffer
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		// Detach the render buffer from the frame buffer
		GL30.glFramebufferRenderbuffer(GL_FRAMEBUFFER, point, GL_RENDERBUFFER, 0);
		// Remove the attachment from the outputs if it is a colour type
		if (isColourAttachment(point)) {
			this.outputBuffers.remove(point);
		}
		// Update the output buffers
		updateOutputBuffers();
		// Check to see if the frame buffer is complete
		if (GL30.glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			throw new IllegalStateException("Unable to complete frame buffer!");
		}
		// Unbind the frame buffer
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, 0);

		// Check for errors
		RenderUtil.checkGLError();
	}

	@Override
	public GLVersion getGLVersion() {
		return GLVersion.GL30;
	}
}
