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
package me.thehutch.fusion.engine.util;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * @author thehutch
 */
public class RenderUtil {
	private RenderUtil() {
	}

	public static void checkGLError(boolean throwError) {
		final int status = GL11.glGetError();
		if (status != GL_NO_ERROR && throwError) {
			throw new IllegalStateException("OpenGL Error: " + GLU.gluErrorString(status));
		}
	}
}
