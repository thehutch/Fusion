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

import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * @author thehutch
 */
public class VertexArray implements Disposable {
	private final int[] attributeIds;
	private final int numIndices;
	private final int vao;
	private final int ibo;

	public VertexArray(VertexData data) {
		this.numIndices = data.getIndicesCount();
		// Create the vertex array object
		this.vao = GL30.glGenVertexArrays();
		// Bind the vertex array
		GL30.glBindVertexArray(vao);

		// Create the index buffer
		this.ibo = GL15.glGenBuffers();
		// Bind the index buffer
		GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		// Set the indices data to the vao from the vertex data
		GL15.glBufferData(GL_ELEMENT_ARRAY_BUFFER, data.getIndices(), GL_STATIC_DRAW);
		// Unbind the index buffer
		GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		// Get the attributes from the vertex data
		this.attributeIds = new int[data.getAttributeCount()];

		// Create each attribute
		for (int i = 0; i < attributeIds.length; ++i) {
			// Generate a buffer for the attribute
			this.attributeIds[i] = GL15.glGenBuffers();
			// Bind the attribute
			GL15.glBindBuffer(GL_ARRAY_BUFFER, attributeIds[i]);
			// Get the next attribute
			final VertexAttribute attrib = data.getAttribute(i);
			// Set the attribute data to the vao
			GL15.glBufferData(GL_ARRAY_BUFFER, attrib.getData(), GL_STATIC_DRAW);
			// Enable the vertex attribute
			GL20.glEnableVertexAttribArray(i);
			// Set the vertex attribute settings
			GL20.glVertexAttribPointer(i, attrib.getSize(), GL_FLOAT, false, 0, 0L);
			// Disable the vertex attribute
			GL20.glDisableVertexAttribArray(i);
			// Unbind the attribute buffer
			GL15.glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
		// Unbind the vertex array object
		GL30.glBindVertexArray(0);
		// Check for errors
		RenderUtil.checkGLError();
	}

	public void draw() {
		//Bind the vertex array object
		GL30.glBindVertexArray(vao);
		// Enable the vertex attributes
		for (int i = 0; i < attributeIds.length; ++i) {
			GL20.glEnableVertexAttribArray(i);
		}
		// Bind the index buffer
		GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		// Draw the vertex elements
		GL11.glDrawElements(GL_TRIANGLES, numIndices, GL_UNSIGNED_INT, 0L);
		// Unbind the index buffer
		GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		// Disable the vertex attributes
		for (int i = 0; i < attributeIds.length; ++i) {
			GL20.glDisableVertexAttribArray(i);
		}
		// Unbind the vertex array object
		GL30.glBindVertexArray(0);
	}

	@Override
	public void dispose() {
		// Delete the attribute buffers
		for (int buffer : attributeIds) {
			GL15.glDeleteBuffers(buffer);
		}
		// Delete the index buffer
		GL15.glDeleteBuffers(ibo);
		// Delete the vertex array object
		GL30.glDeleteVertexArrays(vao);
	}
}
