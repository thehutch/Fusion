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
package me.thehutch.fusion.api.render;

import java.util.Collection;
import me.thehutch.fusion.api.maths.Rectangle;
import me.thehutch.fusion.api.maths.Vector3;

/**
 * @author thehutch
 */
public interface IContext {
	/**
	 *
	 * @return
	 */
	public String getWindowTitle();

	/**
	 * Sets the text of the title bar of the window.
	 *
	 * @param title The title text
	 */
	public void setWindowTitle(String title);

	/**
	 * Sets the position of the window.
	 *
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void setWindowPosition(int x, int y);

	/**
	 * Sets the size of the window.
	 *
	 * @param width  The width of the window
	 * @param height The height of the window
	 */
	public void setWindowSize(int width, int height);

	/**
	 * Gets the position and sie of the window.
	 *
	 * @return A rectangle representing the position and size of the window
	 */
	public Rectangle getWindowPositionAndSize();

	/**
	 * Sets the window at the given rectangle position and size.
	 *
	 * @param window A rectangle representing the window
	 */
	public void setWindowPositionAndSize(Rectangle window);

	/**
	 * Sets the clearing colour of the context.
	 *
	 * @param colour The clearing colour
	 *
	 * @see setClearColour(float, float, float)
	 */
	public void setClearColour(Vector3 colour);

	/**
	 * Sets the clearing colour of the context.
	 *
	 * @param r The red channel
	 * @param g The green channel
	 * @param b The blue channel
	 */
	public void setClearColour(float r, float g, float b);

	/**
	 * Sets the viewport of the context.
	 *
	 * @param rectangle The viewport
	 */
	public void setViewPort(Rectangle rectangle);

	/**
	 * Enables fullscreen.
	 *
	 * @param enable True to enable fullscreen, false to disable
	 */
	public void enableFullscreen(boolean enable);

	/**
	 * Enables vertical sync.
	 *
	 * @param enabled True to enable v-sync, false to disable
	 */
	public void enableVSync(boolean enabled);

	/**
	 * Retrieves all the supported resolutions of the system.
	 *
	 * @return The supported resolutions
	 */
	public Collection<Resolution> getSupportedResolutions();
}
