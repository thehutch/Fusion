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

	private GLVersion(int major, int minor, int glslMajor, int glslMinor) {
		this.major = major;
		this.minor = minor;
		this.glslMajor = glslMajor;
		this.glslMinor = glslMinor;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getGLSLMajor() {
		return glslMajor;
	}

	public int getGLSLMinor() {
		return glslMinor;
	}

	public int getVersion() {
		return major * 10 + minor;
	}

	public int getGLSLVersion() {
		return glslMajor * 100 + glslMinor * 10;
	}

	public boolean supportsGLSL() {
		return glslMajor != 0;
	}

	public boolean supportsGLVersion(GLVersion version) {
		return major >= version.major && minor >= version.minor;
	}
}
