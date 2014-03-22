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
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.hash.THashSet;
import java.nio.FloatBuffer;
import java.util.Set;
import me.thehutch.fusion.api.maths.matrix.Matrix4;
import me.thehutch.fusion.api.maths.vector.Vector2;
import me.thehutch.fusion.api.maths.vector.Vector3;
import me.thehutch.fusion.api.maths.vector.Vector4;
import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class Program implements Disposable {
	private final TObjectIntMap<String> uniforms = new TObjectIntHashMap<>();
	private final Set<Shader> shaders = new THashSet<>();
	private final int id;

	public Program() {
		// Create the program
		this.id = GL20.glCreateProgram();
	}

	public void use() {
		GL20.glUseProgram(id);
	}

	public void attachShader(Shader shader) {
		// Attach the shader
		GL20.glAttachShader(id, shader.getId());
		// Add the shader to the program
		this.shaders.add(shader);
	}

	public void detachShader(Shader shader) {
		// Detach the shader
		GL20.glDetachShader(id, shader.getId());
		// Remove the shader from the program
		this.shaders.remove(shader);
	}

	public void link() {
		// Link the program
		GL20.glLinkProgram(id);
		// Check program link success
		if (GL20.glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
			final int logLength = GL20.glGetProgrami(id, GL_INFO_LOG_LENGTH);
			throw new IllegalStateException("Program could not be linked\n" + GL20.glGetProgramInfoLog(id, logLength));
		}
		RenderUtil.checkGLError();
		// TODO: Move validation to debug only
		// Validate the program
		GL20.glValidateProgram(id);
		// Check program validation success
		if (GL20.glGetProgrami(id, GL_VALIDATE_STATUS) == GL_FALSE) {
			final int logLength = GL20.glGetProgrami(id, GL_INFO_LOG_LENGTH);
			System.err.println("Program validation failed:\n" + GL20.glGetProgramInfoLog(id, logLength));
		}
		RenderUtil.checkGLError();
		// Load the program uniforms
		final int uniformCount = GL20.glGetProgrami(id, GL_ACTIVE_UNIFORMS);
		for (int i = 0; i < uniformCount; ++i) {
			final String name = GL20.glGetActiveUniform(id, i, GL20.glGetProgrami(id, GL_ACTIVE_UNIFORM_MAX_LENGTH));
			this.uniforms.put(name, GL20.glGetUniformLocation(id, name));
		}
		RenderUtil.checkGLError();
	}

	@Override
	public void dispose() {
		this.shaders.stream().map((shader) -> {
			GL20.glDetachShader(id, shader.getId());
			GL20.glDeleteShader(shader.getId());
			return shader;
		});
		// Delete the program
		GL20.glDeleteProgram(id);
		// Clear the shaders set
		this.shaders.clear();
		// Clear the uniforms map
		this.uniforms.clear();
		// Check for errors
		RenderUtil.checkGLError();
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

	public void setUniform(String name, Matrix4 matrix) {
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(matrix.toArray());
		buffer.flip();
		GL20.glUniformMatrix4(uniforms.get(name), false, buffer);
	}
}
