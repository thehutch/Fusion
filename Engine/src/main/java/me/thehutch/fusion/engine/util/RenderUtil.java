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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Display;
import org.lwjgl.util.glu.GLU;

/**
 * @author thehutch
 */
public class RenderUtil {
	private RenderUtil() {
	}

	public static void checkGLError() {
		final int status = GL11.glGetError();
		if (status != GL_NO_ERROR) {
			throw new IllegalStateException("OpenGL Error: " + GLU.gluErrorString(status));
		}
	}

	public static List<DisplayMode> getSupportedResolutions() {
		try {
			final DisplayMode[] modes = Display.getAvailableDisplayModes(800, 600, 1920, 1080, 24, 24, 50, 60);
			for (DisplayMode mode : modes) {
				System.out.format("%dx%d%n", mode.getWidth(), mode.getHeight());
			}
			return Collections.unmodifiableList(Arrays.asList(modes));
		} catch (LWJGLException ex) {
			throw new IllegalStateException("Unable to retrieve available resolutions!", ex);
		}
	}
}
