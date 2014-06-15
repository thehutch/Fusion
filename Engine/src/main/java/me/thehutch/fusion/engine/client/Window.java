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
package me.thehutch.fusion.engine.client;

import java.util.logging.Level;
import me.thehutch.fusion.api.client.IWindow;
import me.thehutch.fusion.engine.Client;
import me.thehutch.fusion.engine.Engine;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * @author thehutch
 */
public final class Window implements IWindow {
	private final Client engine;

	public Window(Client engine) {
		this.engine = engine;
		try {
			// Set the window settings
			setWindowTitle("Fusion Engine | " + Engine.ENGINE_VERSION);
			setWindowSize(800, 600);
			setVSync(true);
			// Create the window
			Display.create(new PixelFormat(24, 8, 8, 0, 4), new ContextAttribs(3, 3).withProfileCore(true));
		} catch (LWJGLException ex) {
			engine.getLogger().log(Level.SEVERE, "Unable to create window!", ex);
		}
		setBackgroundColour(0.0f, 0.45f, 0.75f);
	}

	@Override
	public void setWindowTitle(String title) {
		Display.setTitle(title);
	}

	@Override
	public void setWindowSize(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
		} catch (LWJGLException ex) {
			this.engine.getLogger().log(Level.SEVERE, "Unable to set window size!", ex);
		}
	}

	@Override
	public void setFullscreen(boolean fullscreen) {
		try {
			Display.setFullscreen(fullscreen);
		} catch (LWJGLException ex) {
			this.engine.getLogger().log(Level.SEVERE, "Unable to set fullscreen!", ex);
		}
	}

	@Override
	public void setVSync(boolean enabled) {
		Display.setVSyncEnabled(enabled);
	}

	@Override
	public void setBackgroundColour(float red, float green, float blue) {
		if (Display.isCreated()) {
			GL11.glClearColor(red, green, blue, 1.0f);
		}
	}
}
