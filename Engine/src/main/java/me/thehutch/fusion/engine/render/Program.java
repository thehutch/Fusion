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

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_ACTIVE_UNIFORMS;
import static org.lwjgl.opengl.GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.nio.FloatBuffer;
import me.thehutch.fusion.api.maths.Matrix2;
import me.thehutch.fusion.api.maths.Matrix3;
import me.thehutch.fusion.api.maths.Matrix4;
import me.thehutch.fusion.api.maths.Vector2;
import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.maths.Vector4;
import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class Program implements Disposable {
	private final TObjectIntMap<String> uniforms = new TObjectIntHashMap<>();
	private final TIntList shaders = new TIntArrayList(2, 0);
	private final int id;

	public Program() {
		// Create the program
		this.id = GL20.glCreateProgram();
	}

	public void bind() {
		GL20.glUseProgram(id);
	}

	public void unbind() {
		GL20.glUseProgram(0);
	}

	public void attachShader(CharSequence source, int type) {
		// Check that the source is not null
		if (source == null) {
			throw new IllegalArgumentException("Shader source can not be null");
		}
		// Generate a shader handle
		final int shaderId = GL20.glCreateShader(type);
		// Upload the shader's source to the GPU
		GL20.glShaderSource(shaderId, source);
		// Compile the shader
		GL20.glCompileShader(shaderId);
		// Check for a shader compile error
		if (GL20.glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
			// Get the shader info log length
			final int logLength = GL20.glGetShaderi(shaderId, GL20.GL_INFO_LOG_LENGTH);
			throw new IllegalStateException("OpenGL Error: Could not compile shader\n" + GL20.glGetShaderInfoLog(shaderId, logLength));
		}
		// Attach the shader
		GL20.glAttachShader(id, shaderId);
		// Add the shader to the program
		this.shaders.add(shaderId);
		// Check for errors
		RenderUtil.checkGLError(true);
	}

	public void link() {
		// Link the program
		GL20.glLinkProgram(id);
		// Check program link success
		if (GL20.glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
			final int logLength = GL20.glGetProgrami(id, GL_INFO_LOG_LENGTH);
			throw new IllegalStateException("Program could not be linked\n" + GL20.glGetProgramInfoLog(id, logLength));
		}
		// Check for errors
		RenderUtil.checkGLError(true);

		// Validate the program
		GL20.glValidateProgram(id);
		// Check program validation success
		if (GL20.glGetProgrami(id, GL_VALIDATE_STATUS) == GL_FALSE) {
			final int logLength = GL20.glGetProgrami(id, GL_INFO_LOG_LENGTH);
			System.err.println("Program validation failed:\n" + GL20.glGetProgramInfoLog(id, logLength));
		}
		// Check for errors
		RenderUtil.checkGLError(true);

		// Load the program uniforms
		final int uniformCount = GL20.glGetProgrami(id, GL_ACTIVE_UNIFORMS);
		for (int i = 0; i < uniformCount; ++i) {
			final String name = GL20.glGetActiveUniform(id, i, GL20.glGetProgrami(id, GL_ACTIVE_UNIFORM_MAX_LENGTH));
			this.uniforms.put(name, GL20.glGetUniformLocation(id, name));
		}
		// Check for errors
		RenderUtil.checkGLError(true);
	}

	@Override
	public void dispose() {
		// Detach and delete each shader
		this.shaders.forEach((int shader) -> {
			GL20.glDetachShader(id, shader);
			GL20.glDeleteShader(shader);
			return true;
		});
		// Delete the program
		GL20.glDeleteProgram(id);
		// Clear the shaders set
		this.shaders.clear();
		// Clear the uniforms map
		this.uniforms.clear();
		// Check for errors
		RenderUtil.checkGLError(true);
	}

	public void setUniform(String name, boolean b) {
		GL20.glUniform1i(uniforms.get(name), b ? 1 : 0);
	}

	public void setUniform(String name, int i) {
		GL20.glUniform1i(uniforms.get(name), i);
	}

	public void setUniform(String name, float f) {
		GL20.glUniform1f(uniforms.get(name), f);
	}

	public void setUniform(String name, Vector2 vec) {
		GL20.glUniform2f(uniforms.get(name), vec.getX(), vec.getY());
	}

	public void setUniform(String name, Vector3 vec) {
		GL20.glUniform3f(uniforms.get(name), vec.getX(), vec.getY(), vec.getZ());
	}

	public void setUniform(String name, Vector4 vec) {
		GL20.glUniform4f(uniforms.get(name), vec.getX(), vec.getY(), vec.getZ(), vec.getW());
	}

	public void setUniform(String name, Matrix2 matrix) {
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(matrix.toArray(true));
		buffer.flip();
		GL20.glUniformMatrix2(uniforms.get(name), false, buffer);
	}

	public void setUniform(String name, Matrix2[] matrix) {
		final int length = matrix.length;
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(4 * length);
		for (int i = 0; i < length; ++i) {
			buffer.put(matrix[i].toArray(true));
		}
		buffer.flip();
		GL20.glUniformMatrix2(uniforms.get(name), false, buffer);
	}

	public void setUniform(String name, Matrix3 matrix) {
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
		buffer.put(matrix.toArray(true));
		buffer.flip();
		GL20.glUniformMatrix3(uniforms.get(name), false, buffer);
	}

	public void setUniform(String name, Matrix3[] matrix) {
		final int length = matrix.length;
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(9 * length);
		for (int i = 0; i < length; ++i) {
			buffer.put(matrix[i].toArray(true));
		}
		buffer.flip();
		GL20.glUniformMatrix3(uniforms.get(name), false, buffer);
	}

	public void setUniform(String name, Matrix4 matrix) {
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(matrix.toArray(true));
		buffer.flip();
		GL20.glUniformMatrix4(uniforms.get(name), false, buffer);
	}

	public void setUniform(String name, Matrix4[] matrix) {
		final int length = matrix.length;
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(16 * length);
		for (int i = 0; i < length; ++i) {
			buffer.put(matrix[i].toArray(true));
		}
		buffer.flip();
		GL20.glUniformMatrix4(uniforms.get(name), false, buffer);
	}
}
