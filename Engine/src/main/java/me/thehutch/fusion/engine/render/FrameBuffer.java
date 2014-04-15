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

import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT15;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import java.nio.IntBuffer;
import java.util.Arrays;
import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * @author thehutch
 */
public class FrameBuffer implements Disposable {
	private final TIntSet outputBuffers = new TIntHashSet();
	private final int fbo;

	public FrameBuffer() {
		// Generate and bind the frame buffer
		this.fbo = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		// Disable input buffers
		GL11.glReadBuffer(GL_NONE);
		// Unbind the frame buffer
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, 0);

		// Check for errors
		RenderUtil.checkGLError(true);
	}

	public void bind() {
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, fbo);
	}

	public void unbind() {
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void attachTexture(int point, int texture) {
		// Bind the frame buffer
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		// Bind the texture to the frame buffer
		GL30.glFramebufferTexture2D(GL_FRAMEBUFFER, point, GL_TEXTURE_2D, texture, 0);
		// Add the attachment to the outputs if it is a colour type
		if (isColourAttachment(point)) {
			this.outputBuffers.add(point);
		}
		// Update the output buffers
		updateOutputBuffers();
		// Unbind the frame buffer
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, 0);

		// Check for errors
		RenderUtil.checkGLError(true);
	}

	public void attachBuffer(int point, int buffer) {
		// Bind the frame buffer
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		// Attach the render buffer to the frame buffer
		GL30.glFramebufferRenderbuffer(GL_FRAMEBUFFER, point, GL_RENDERBUFFER, buffer);
		RenderUtil.checkGLError(true);
		// Add the attachment to the outputs if it is a colour type
		if (isColourAttachment(point)) {
			this.outputBuffers.add(point);
		}
		// Update the output buffers
		updateOutputBuffers();
		// Unbind the frame buffer
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, 0);

		// Check for errors
		RenderUtil.checkGLError(true);
	}

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
		// Unbind the frame buffer
		GL30.glBindFramebuffer(GL_FRAMEBUFFER, 0);

		// Check for errors
		RenderUtil.checkGLError(true);
	}

	@Override
	public void dispose() {
		// Delete the frame buffer
		GL30.glDeleteFramebuffers(fbo);
		// Clear the output buffers
		this.outputBuffers.clear();
	}

	private void updateOutputBuffers() {
		// Check if there are any attachments
		if (outputBuffers.isEmpty()) {
			// Set the draw buffers to none
			GL20.glDrawBuffers(GL_NONE);
		} else {
			final int[] outputs = outputBuffers.toArray();
			// Sort the buffers to they are in order
			Arrays.sort(outputs);
			// Convert the output buffers to an IntBuffer
			final IntBuffer buffer = BufferUtils.createIntBuffer(outputs.length);
			buffer.put(outputs);
			buffer.flip();
			// Set the draw buffers to the output buffers
			GL20.glDrawBuffers(buffer);
		}
		// Check for errors
		RenderUtil.checkGLError(true);
	}

	private static boolean isColourAttachment(int attachment) {
		return attachment >= GL_COLOR_ATTACHMENT0 && attachment <= GL_COLOR_ATTACHMENT15;
	}
}
