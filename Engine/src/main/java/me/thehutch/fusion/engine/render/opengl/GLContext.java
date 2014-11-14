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
package me.thehutch.fusion.engine.render.opengl;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.maths.geometry.Rectangle;
import me.thehutch.fusion.api.render.IContext;
import me.thehutch.fusion.api.render.Resolution;
import me.thehutch.fusion.api.util.Creatable;
import me.thehutch.fusion.engine.Engine;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 * @author thehutch
 */
public abstract class GLContext extends Creatable implements IContext, GLVersioned {

	public abstract Texture newTexture();

	public abstract Program newProgram();

	public abstract VertexArray newVertexArray();

	public abstract FrameBuffer newFrameBuffer();

	public abstract RenderBuffer newRenderBuffer();

	@Override
	public String getWindowTitle() {
		ensureCreated("Can not get window title if the context has not been created");
		return Display.getTitle();
	}

	@Override
	public void setWindowTitle(String title) {
		ensureCreated("Can not set window title if the context has not been created");
		Display.setTitle(title);
	}

	@Override
	public void setWindowPosition(int x, int y) {
		ensureCreated("Can not set window position if the context has not been created");
		Display.setLocation(x, y);
	}

	@Override
	public void setWindowSize(int width, int height) {
		ensureCreated("Can not set window size if the context has not been created");
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
		} catch (LWJGLException ex) {
			Engine.getLogger().log(Level.SEVERE, "Unable to set window size!", ex);
		}
	}

	@Override
	public Rectangle getWindowPositionAndSize() {
		ensureCreated("Can not get window position and size if the context has not been created");
		return new Rectangle(Display.getX(), Display.getY(), Display.getWidth(), Display.getHeight());
	}

	@Override
	public void setWindowPositionAndSize(Rectangle window) {
		ensureCreated("Can not set window position and size if the context has not been created");
		setWindowPosition(window.getX(), window.getY());
		setWindowSize(window.getWidth(), window.getHeight());
	}

	@Override
	public void enableFullscreen(boolean enable) {
		ensureCreated("Can not enable fullscreen if the context has not been created");
		try {
			Display.setFullscreen(enable);
		} catch (LWJGLException ex) {
			Engine.getLogger().log(Level.SEVERE, "Unable to " + (enable ? "enable" : "disable") + " fullscreen!", ex);
		}
	}

	@Override
	public void enableVSync(boolean enabled) {
		ensureCreated("Can not enable vsync if the context has not been created");
		Display.setVSyncEnabled(enabled);
	}

	@Override
	public void setClearColour(Vector3 colour) {
		setClearColour(colour.getX(), colour.getY(), colour.getZ());
	}

	@Override
	public void setClearColour(float r, float g, float b) {
		ensureCreated("Can not set clear colour if the context has not been created");
		GL11.glClearColor(r, g, b, 1.0f);
	}

	@Override
	public void setViewPort(Rectangle viewport) {
		ensureCreated("Can not set viewport if the context has not been created");
		GL11.glViewport(viewport.getX(), viewport.getY(), viewport.getWidth(), viewport.getHeight());
	}

	@Override
	public Collection<Resolution> getSupportedResolutions() {
		try {
			final DisplayMode[] modes = Display.getAvailableDisplayModes();
			final Collection<Resolution> resolutions = new ArrayList<>(modes.length);
			for (DisplayMode mode : modes) {
				resolutions.add(new Resolution(mode.getWidth(), mode.getHeight()));
			}
			return resolutions;
		} catch (LWJGLException ex) {
			Engine.getLogger().log(Level.SEVERE, "Unable to get the supported resolutions!", ex);
		}
		return null;
	}

	@Override
	public void dispose() {
		ensureCreated("Can not dispose of the context if it has not been created");
		Display.destroy();
		super.dispose();
	}

	public void clearCurrentBuffer() {
		ensureCreated("Can not clear the current buffer if the context has not been created");
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
}
