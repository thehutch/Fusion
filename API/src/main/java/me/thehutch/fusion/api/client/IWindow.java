/*
 * This file is part of API.
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
package me.thehutch.fusion.api.client;

/**
 * @author thehutch
 */
public interface IWindow {
	/**
	 * Sets the text of the title bar of the window.
	 *
	 * @param title The title text
	 */
	public void setWindowTitle(String title);

	/**
	 * Sets the dimensions of the window.
	 *
	 * @param width  The width of the window
	 * @param height The height of the window
	 */
	public void setWindowSize(int width, int height);

	/**
	 * Sets the fullscreen state of the window.
	 *
	 * @param fullscreen True to enable fullscreen
	 */
	public void setFullscreen(boolean fullscreen);

	/**
	 * Sets the vertical sync state.
	 *
	 * @param enabled True to enable v-sync
	 */
	public void setVSync(boolean enabled);

	/**
	 * Sets the background colour of the window in RGB. Equivalent to OpenGL's glClearColor.
	 *
	 * @param red   The red channel value
	 * @param green The green channel value
	 * @param blue  The blue channel value
	 */
	public void setBackgroundColour(float red, float green, float blue);
}
