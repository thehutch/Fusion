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
package me.thehutch.fusion.engine.render.opengl;

import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT15;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import java.nio.IntBuffer;
import java.util.Arrays;
import me.thehutch.fusion.api.util.Creatable;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

/**
 * @author thehutch
 */
public abstract class FrameBuffer extends Creatable implements GLVersioned {
	protected final TIntSet outputBuffers = new TIntHashSet();
	protected int fbo;

	public abstract void bind();

	public abstract void unbind();

	public abstract void attachTexture(int point, Texture texture);

	public abstract void attachBuffer(int point, RenderBuffer buffer);

	public abstract void detach(int point);

	protected final void updateOutputBuffers() {
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
		RenderUtil.checkGLError();
	}

	protected static boolean isColourAttachment(int attachment) {
		return attachment >= GL_COLOR_ATTACHMENT0 && attachment <= GL_COLOR_ATTACHMENT15;
	}
}
