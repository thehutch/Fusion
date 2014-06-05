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

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import gnu.trove.list.TFloatList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * @author thehutch
 */
public class VertexArrayObject implements Disposable {
	private final TIntList attributes = new TIntArrayList();
	private final int vao;
	private final int ibo;
	private int drawCount;

	public VertexArrayObject() {
		// Create and bind the vertex array object
		this.vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		// Create the index buffer
		this.ibo = GL15.glGenBuffers();

		// Check for errors
		RenderUtil.checkGLError();
	}

	public void setIndices(TIntList indices) {
		// Put the indices into an IntBuffer
		final IntBuffer buffer = BufferUtils.createIntBuffer(indices.size());
		indices.forEach((int i) -> {
			buffer.put(i);
			return true;
		});
		buffer.flip();
		// Bind the index buffer
		GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		// Set the indices data to the vao from the vertex data
		GL15.glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		// Unbind the index buffer
		GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		// Set the draw count
		this.drawCount = indices.size();

		// Check for errors
		RenderUtil.checkGLError();
	}

	public void addAttribute(int index, int size, TFloatList data) {
		// Put the indices into an FloatBuffer
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(data.size());
		data.forEach((float f) -> {
			buffer.put(f);
			return true;
		});
		buffer.flip();
		// Generate and bind the attribute buffer
		final int id = GL15.glGenBuffers();
		GL15.glBindBuffer(GL_ARRAY_BUFFER, id);
		// Set the attribute data
		GL15.glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		// Enable the vertex attribute
		GL20.glEnableVertexAttribArray(index);
		// Set the vertex attribute settings
		GL20.glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0L);
		// Disable the vertex attribute
		GL20.glDisableVertexAttribArray(index);
		// Unbind the attribute buffer
		GL15.glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Add the buffer id to the attributes list
		this.attributes.insert(index, id);

		// Check for errors
		RenderUtil.checkGLError();
	}

	public void draw() {
		//Bind the vertex array object
		GL30.glBindVertexArray(vao);
		// Enable the vertex attributes
		for (int i = 0; i < attributes.size(); ++i) {
			GL20.glEnableVertexAttribArray(i);
		}
		// Bind the index buffer
		GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		// Draw the vertex elements
		GL11.glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0L);
		// Unbind the index buffer
		GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		// Disable the vertex attributes
		for (int i = 0; i < attributes.size(); ++i) {
			GL20.glDisableVertexAttribArray(i);
		}
		// Unbind the vertex array object
		GL30.glBindVertexArray(0);
	}

	@Override
	public void dispose() {
		// Delete each attribute buffer
		this.attributes.forEach((int i) -> {
			GL15.glDeleteBuffers(i);
			return true;
		});
		// Delete the index buffer
		GL15.glDeleteBuffers(ibo);
		// Delete the vertex array object
		GL30.glDeleteVertexArrays(vao);
	}
}
