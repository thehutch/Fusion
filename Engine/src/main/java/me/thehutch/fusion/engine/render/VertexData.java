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
 * This file is part of FusionEngine.
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

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VertexData implements Disposable {
	private final TIntList buffers = new TIntArrayList();
	private final int numIndices;
	private final int ibo;
	private final int vao;

	public VertexData(FloatBuffer positions, FloatBuffer texcoords, FloatBuffer normals, IntBuffer indices) {
		this.numIndices = indices.capacity();

		// Create the vertex array object
		this.vao = GL30.glGenVertexArrays();
		// bind the vertex array object
		GL30.glBindVertexArray(vao);

		// Generate the position buffer
		final int positionBufferId = GL15.glGenBuffers();
		this.buffers.insert(0, positionBufferId);
		// Bind the position buffer and set its data
		GL15.glBindBuffer(GL_ARRAY_BUFFER, positionBufferId);
		GL15.glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);
		{
			// Enable the position attribute
			GL20.glEnableVertexAttribArray(0);
			GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		}

		// Generate the texture coordinate buffer
//		final int texcoordBufferId = GL15.glGenBuffers();
//		this.buffers.insert(1, texcoordBufferId);
//		// Bind the texture coordinates buffer and set its data
//		GL15.glBindBuffer(GL_ARRAY_BUFFER, texcoordBufferId);
//		GL15.glBufferData(GL_ARRAY_BUFFER, texcoords, GL_STATIC_DRAW);
//		{
//			// Enable the texcoord attribute
//			GL20.glEnableVertexAttribArray(1);
//			GL20.glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
//		}
//
//		// Generate the normals buffer
//		final int normalsBufferId = GL15.glGenBuffers();
//		this.buffers.insert(2, normalsBufferId);
//		// Bind the texture coordinates buffer and set its data
//		GL15.glBindBuffer(GL_ARRAY_BUFFER, normalsBufferId);
//		GL15.glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
//		{
//			// Enable the texcoord attribute
//			GL20.glEnableVertexAttribArray(2);
//			GL20.glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
//		}

		// Generate the index buffer
		this.ibo = GL15.glGenBuffers();
		// Bind the index buffer and set its data
		GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		// Unbind the index buffer
		GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		// Unbind any array buffer
		GL15.glBindBuffer(GL_ARRAY_BUFFER, 0);
		// Unbind the vertex array object
		GL30.glBindVertexArray(0);
		// Check for errors
		RenderUtil.checkGLError();
	}

	public void draw() {
		// Bind the vertex array object
		GL30.glBindVertexArray(vao);
		// Bind the index buffer
		GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		// Draw the vertex elements
		GL11.glDrawElements(GL_TRIANGLES, numIndices, GL_UNSIGNED_INT, 0);
		// Unbind the index buffer
		GL15.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		// Unbind the vertex array object
		GL30.glBindVertexArray(0);
	}

	@Override
	public void dispose() {
		// Delete the index buffer
		GL15.glDeleteBuffers(ibo);

		// Disable each attribute and delete its buffer
		for (int i = 0; i < buffers.size(); ++i) {
			GL20.glDisableVertexAttribArray(i);
			GL15.glDeleteBuffers(buffers.get(i));
		}

		// Delete the vertex array object
		GL30.glDeleteVertexArrays(vao);
		RenderUtil.checkGLError();
	}
}
