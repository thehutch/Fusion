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
package me.thehutch.fusion.engine.render.opengl.gl30;

import me.thehutch.fusion.api.util.GLVersion;
import me.thehutch.fusion.engine.Engine;
import me.thehutch.fusion.engine.render.opengl.FrameBuffer;
import me.thehutch.fusion.engine.render.opengl.GLContext;
import me.thehutch.fusion.engine.render.opengl.Program;
import me.thehutch.fusion.engine.render.opengl.RenderBuffer;
import me.thehutch.fusion.engine.render.opengl.Texture;
import me.thehutch.fusion.engine.render.opengl.VertexArray;
import me.thehutch.fusion.engine.render.opengl.gl20.OpenGL20Program;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * @author thehutch
 */
public class OpenGL30Context extends GLContext {

	@Override
	public void create() {
		try {
			// Get the desktop display mode
			final DisplayMode mode = Display.getDesktopDisplayMode();
			// Create the pixel format
			final PixelFormat format = new PixelFormat(mode.getBitsPerPixel(), 8, 8, 0, 0);
			// Create the context attrib
			ContextAttribs attribs = new ContextAttribs(getGLVersion().getMajor(), getGLVersion().getMinor());
			attribs = attribs.withProfileCore(true);
			attribs = attribs.withDebug(true);
			// Create the display
			Display.create(format, attribs);
			super.create();
		} catch (LWJGLException ex) {
			throw new IllegalStateException("Unable to create display!", ex);
		}
		setWindowTitle("Fusion Engine | Version: " + Engine.ENGINE_VERSION);
		setClearColour(0.0f, 0.35f, 0.75f);
		setWindowSize(800, 600);
		enableVSync(true);
	}

	@Override
	public Texture newTexture() {
		return new OpenGL30Texture();
	}

	@Override
	public Program newProgram() {
		return new OpenGL20Program();
	}

	@Override
	public VertexArray newVertexArray() {
		return new OpenGL30VertexArray();
	}

	@Override
	public FrameBuffer newFrameBuffer() {
		return new OpenGL30FrameBuffer();
	}

	@Override
	public RenderBuffer newRenderBuffer() {
		return new OpenGL30RenderBuffer();
	}

	@Override
	public GLVersion getGLVersion() {
		return GLVersion.GL32;
	}
}
