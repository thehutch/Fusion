/*
 * This file is part of API, licensed under the Apache 2.0 License.
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
package me.thehutch.fusion.api.util;

/**
 * @author thehutch
 */
public enum GLVersion {
	GL11(1, 1, 0, 0),
	GL12(1, 2, 0, 0),
	GL13(1, 3, 0, 0),
	GL14(1, 4, 0, 0),
	GL15(1, 5, 0, 0),
	GL20(2, 0, 1, 1),
	GL21(2, 1, 1, 2),
	GL30(3, 0, 1, 3),
	GL31(3, 1, 1, 4),
	GL32(3, 2, 1, 5),
	GL33(3, 3, 3, 3),
	GL40(4, 0, 4, 0),
	GL41(4, 1, 4, 1),
	GL42(4, 2, 4, 2),
	GL43(4, 3, 4, 3),
	GL44(4, 4, 4, 4);
	private final int major;
	private final int minor;
	private final int glslMajor;
	private final int glslMinor;

	/**
	 * Default constructor for {@link GLVersion}.
	 *
	 * @param major     The OpenGL major version
	 * @param minor     The OpenGL minor version
	 * @param glslMajor The GLSL major version
	 * @param glslMinor The GLSL minor version
	 */
	private GLVersion(int major, int minor, int glslMajor, int glslMinor) {
		this.major = major;
		this.minor = minor;
		this.glslMajor = glslMajor;
		this.glslMinor = glslMinor;
	}

	/**
	 * Returns the minimum OpenGL major version required to use this {@link GLVersion}.
	 *
	 * @return The minimum OpenGL major version
	 */
	public int getMajor() {
		return major;
	}

	/**
	 * Returns the minimum OpenGL minor version required to use this {@link GLVersion}.
	 *
	 * @return The minimum OpenGL minor version
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * Returns the minimum GLSL major version required to use this {@link GLVersion}.
	 *
	 * @return The minimum GLSL major version
	 */
	public int getGLSLMajor() {
		return glslMajor;
	}

	/**
	 * Returns the minimum GLSL minor version required to use this {@link GLVersion}.
	 *
	 * @return The minimum GLSL minor version
	 */
	public int getGLSLMinor() {
		return glslMinor;
	}

	/**
	 * Returns the OpenGL version as an integer (e.g. OpenGL 3.3 == 33)
	 *
	 * @return The OpenGL version
	 */
	public int getVersion() {
		return (major * 10) + minor;
	}

	/**
	 * Returns the GLSL version as an integer (e.g. GLSL 1.5 == 150)
	 *
	 * @return The OpenGL version
	 */
	public int getGLSLVersion() {
		return (glslMajor * 100) + (glslMinor * 10);
	}

	/**
	 * @return True if this GLVersion supports shaders
	 */
	public boolean supportsGLSL() {
		return glslMajor != 0;
	}

	/**
	 * Used to check if this {@link GLVersion} supports another (e.g. GL30 supports GL20)
	 *
	 * @param version The GLVersion
	 *
	 * @return True if this {@link GLVersion} supports the given {@link GLVersion}
	 */
	public boolean supportsGLVersion(GLVersion version) {
		return major >= version.major && minor >= version.minor;
	}
}
