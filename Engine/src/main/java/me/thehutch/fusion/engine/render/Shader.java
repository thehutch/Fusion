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

import static org.lwjgl.opengl.ARBComputeShader.GL_COMPUTE_SHADER;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;

import java.io.InputStream;
import java.util.Scanner;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.opengl.GL20;

/**
 * @author thehutch
 */
public class Shader {
	private final ShaderType type;
	private final int id;

	public Shader(InputStream source, ShaderType type) {
		// Check that the source is not null
		if (source == null) {
			throw new IllegalArgumentException("Shader source can not be null");
		}
		// Set the shader type
		this.type = type;
		// Generate the shader
		this.id = GL20.glCreateShader(type.getGLConstant());
		// Upload the shader's source to the GPU
		GL20.glShaderSource(id, loadSource(source));
		// Compile the shader
		GL20.glCompileShader(id);
		// Check for a shader compile error
		if (GL20.glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
			// Get the shader info log length
			final int logLength = GL20.glGetShaderi(id, GL20.GL_INFO_LOG_LENGTH);
			throw new IllegalStateException("OpenGL Error: Could not compile shader\n" + GL20.glGetShaderInfoLog(id, logLength));
		}
		RenderUtil.checkGLError();
	}

	public int getId() {
		return id;
	}

	public ShaderType getType() {
		return type;
	}

	private static CharSequence loadSource(InputStream stream) {
		final StringBuilder source = new StringBuilder();
		try (final Scanner scanner = new Scanner(stream)) {
			while (scanner.hasNextLine()) {
				source.append(scanner.nextLine()).append('\n');
			}
		}
		return source;
	}

	public static enum ShaderType {
		FRAGMENT(GL_FRAGMENT_SHADER),
		VERTEX(GL_VERTEX_SHADER),
		GEOMETRY(GL_GEOMETRY_SHADER),
		TESS_EVALUATION(GL_TESS_EVALUATION_SHADER),
		TESS_CONTROL(GL_TESS_CONTROL_SHADER),
		COMPUTE(GL_COMPUTE_SHADER);
		private final int glConstant;

		private ShaderType(int glConstant) {
			this.glConstant = glConstant;
		}

		/**
		 * Returns the OpenGL constant associated to the shader type.
		 *
		 * @return The OpenGL constant
		 */
		public int getGLConstant() {
			return glConstant;
		}
	}
}
