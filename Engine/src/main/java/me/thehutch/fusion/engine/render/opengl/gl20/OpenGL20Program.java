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
package me.thehutch.fusion.engine.render.opengl.gl20;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_ACTIVE_UNIFORMS;
import static org.lwjgl.opengl.GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_SAMPLER_2D;
import static org.lwjgl.opengl.GL20.GL_SAMPLER_CUBE;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;

import gnu.trove.impl.Constants;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import me.thehutch.fusion.api.maths.Matrix3;
import me.thehutch.fusion.api.maths.Matrix4;
import me.thehutch.fusion.api.maths.Vector2;
import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.maths.Vector4;
import me.thehutch.fusion.api.util.GLVersion;
import me.thehutch.fusion.engine.render.opengl.Program;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

/**
 * @author thehutch
 */
public class OpenGL20Program extends Program {
	private static final FloatBuffer MATRIX_3X3_BUFFER = BufferUtils.createFloatBuffer(9);
	private static final FloatBuffer MATRIX_4X4_BUFFER = BufferUtils.createFloatBuffer(16);
	private final TObjectIntMap<String> uniforms = new TObjectIntHashMap<>(Constants.DEFAULT_CAPACITY, Constants.DEFAULT_LOAD_FACTOR, -1);
	private final TIntObjectMap<String> textures = new TIntObjectHashMap<>();
	private final TIntList shaders = new TIntArrayList(2, -1);

	@Override
	public void create() {
		ensureNotCreated("Program is already created");
		// Create the program
		this.id = GL20.glCreateProgram();
		super.create();
	}

	@Override
	public void dispose() {
		ensureCreated("Program must be created to dispose.");
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
		RenderUtil.checkGLError();
		// Dispose the program
		super.dispose();
	}

	@Override
	public void bind() {
		ensureCreated("Program must be created to bind.");
		GL20.glUseProgram(id);
	}

	@Override
	public void unbind() {
		GL20.glUseProgram(0);
	}

	@Override
	public void attachShader(CharSequence source, int type) {
		ensureCreated("Program must be created to attach a shader.");
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
		RenderUtil.checkGLError();
	}

	@Override
	public void link() {
		ensureCreated("Program must be created to link.");
		// Link the program
		GL20.glLinkProgram(id);
		// Check program link success
		if (GL20.glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
			final int logLength = GL20.glGetProgrami(id, GL_INFO_LOG_LENGTH);
			throw new IllegalStateException("Program could not be linked\n" + GL20.glGetProgramInfoLog(id, logLength));
		}
		// Check for errors
		RenderUtil.checkGLError();

		// Validate the program
		GL20.glValidateProgram(id);
		// Check program validation success
		if (GL20.glGetProgrami(id, GL_VALIDATE_STATUS) == GL_FALSE) {
			final int logLength = GL20.glGetProgrami(id, GL_INFO_LOG_LENGTH);
			System.err.println("Program validation failed:\n" + GL20.glGetProgramInfoLog(id, logLength));
		}
		// Check for errors
		RenderUtil.checkGLError();

		/*
		 * Load the program uniforms
		 */
		// Get the maximum uniform name length
		final int maxNameLength = GL20.glGetProgrami(id, GL_ACTIVE_UNIFORM_MAX_LENGTH);
		// Create a buffer to store the name of the uniform
		final ByteBuffer nameBuffer = BufferUtils.createByteBuffer(maxNameLength);
		// Create a buffer to store the length of the uniform name
		final IntBuffer lengthBuffer = BufferUtils.createIntBuffer(1);
		// Create a buffer to store the uniform size
		final IntBuffer sizeBuffer = BufferUtils.createIntBuffer(1);
		// Create a buffer to stroe the uniform type
		final IntBuffer typeBuffer = BufferUtils.createIntBuffer(1);
		// Create a byte array to store the name in
		final byte[] nameBytes = new byte[maxNameLength];
		int textureUnit = 0;

		final int uniformCount = GL20.glGetProgrami(id, GL_ACTIVE_UNIFORMS);
		for (int i = 0; i < uniformCount; ++i) {
			// Retrieve the attributes of the uniform (length, size, type and name)
			GL20.glGetActiveUniform(id, i, lengthBuffer, sizeBuffer, typeBuffer, nameBuffer);

			// Get the length of the uniform name
			final int length = lengthBuffer.get();

			// Get the name from the buffer and put it in the byte[]
			nameBuffer.get(nameBytes, 0, length);

			final String name = new String(nameBytes, 0, length);

			// Convert the name buffer to a String
			this.uniforms.put(name, GL20.glGetUniformLocation(id, name));

			// Check if the uniform is a texture/sampler
			switch (typeBuffer.get()) {
				case GL_SAMPLER_2D:
				case GL_SAMPLER_CUBE:
					this.textures.put(textureUnit++, name);
					break;
				default:
					break;
			}

			// Clear the buffers
			lengthBuffer.clear();
			sizeBuffer.clear();
			typeBuffer.clear();
			nameBuffer.clear();
		}
		// Check for errors
		RenderUtil.checkGLError();
	}

	@Override
	public GLVersion getGLVersion() {
		return GLVersion.GL20;
	}

	@Override
	public void bindSampler(int unit) {
		final String name = textures.get(unit);
		if (name == null) {
			throw new IllegalArgumentException("No texture has been set for the unit: " + unit);
		}
		setUniform(name, unit);
	}

	@Override
	public void setUniform(String name, boolean b) {
		final int loc = uniforms.get(name);
		if (loc >= 0) {
			GL20.glUniform1i(loc, b ? 1 : 0);
		}
	}

	@Override
	public void setUniform(String name, int i) {
		final int loc = uniforms.get(name);
		if (loc >= 0) {
			GL20.glUniform1i(loc, i);
		}
	}

	@Override
	public void setUniform(String name, float f) {
		final int loc = uniforms.get(name);
		if (loc >= 0) {
			GL20.glUniform1f(loc, f);
		}
	}

	@Override
	public void setUniform(String name, Vector2 vec) {
		final int loc = uniforms.get(name);
		if (loc >= 0) {
			GL20.glUniform2f(loc, vec.getX(), vec.getY());
		}
	}

	@Override
	public void setUniform(String name, Vector3 vec) {
		final int loc = uniforms.get(name);
		if (loc >= 0) {
			GL20.glUniform3f(loc, vec.getX(), vec.getY(), vec.getZ());
		}
	}

	@Override
	public void setUniform(String name, Vector4 vec) {
		final int loc = uniforms.get(name);
		if (loc >= 0) {
			GL20.glUniform4f(loc, vec.getX(), vec.getY(), vec.getZ(), vec.getW());
		}
	}

	@Override
	public void setUniform(String name, Matrix3 matrix) {
		final int loc = uniforms.get(name);
		if (loc >= 0) {
			MATRIX_3X3_BUFFER.put(matrix.toArray(true));
			MATRIX_3X3_BUFFER.flip();
			GL20.glUniformMatrix3(loc, false, MATRIX_3X3_BUFFER);
			MATRIX_3X3_BUFFER.clear();
		}
	}

	@Override
	public void setUniform(String name, Matrix3[] matrix) {
		final int loc = uniforms.get(name);
		if (loc >= 0) {
			final int length = matrix.length;
			final FloatBuffer buffer = BufferUtils.createFloatBuffer(9 * length);
			for (int i = 0; i < length; ++i) {
				buffer.put(matrix[i].toArray(true));
			}
			buffer.flip();
			GL20.glUniformMatrix3(loc, false, buffer);
		}
	}

	@Override
	public void setUniform(String name, Matrix4 matrix) {
		final int loc = uniforms.get(name);
		if (loc >= 0) {
			MATRIX_4X4_BUFFER.put(matrix.toArray(true));
			MATRIX_4X4_BUFFER.flip();
			GL20.glUniformMatrix4(loc, false, MATRIX_4X4_BUFFER);
			MATRIX_4X4_BUFFER.clear();
		}
	}

	@Override
	public void setUniform(String name, Matrix4[] matrix) {
		final int loc = uniforms.get(name);
		if (loc >= 0) {
			final int length = matrix.length;
			final FloatBuffer buffer = BufferUtils.createFloatBuffer(16 * length);
			for (int i = 0; i < length; ++i) {
				buffer.put(matrix[i].toArray(true));
			}
			buffer.flip();
			GL20.glUniformMatrix4(loc, false, buffer);
		}
	}
}
