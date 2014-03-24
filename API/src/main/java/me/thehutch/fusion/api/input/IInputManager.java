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
package me.thehutch.fusion.api.input;

import me.thehutch.fusion.api.input.keyboard.Key;
import me.thehutch.fusion.api.input.mouse.MouseButton;

/**
 * @author thehutch
 */
public interface IInputManager {

	/**
	 * Gets the current mouse x position on the window.
	 *
	 * @return The current x-coordinate of the mouse
	 */
	public int getMouseX();

	/**
	 * Gets the current mouse y-coordinate on the window.
	 *
	 * @return The current y-coordinate of the mouse
	 */
	public int getMouseY();

	/**
	 * Gets the current state of the mouse button.
	 *
	 * @param button The mouse button to check the state of
	 *
	 * @return True if the mouse button is currently pressed down
	 */
	public boolean isMouseDown(MouseButton button);

	/**
	 * Sets the grabbed state of the mouse. A grabbed mouse can not exit the window area.
	 *
	 * @param grabbed true to set the mouse as grabbed
	 */
	public void setMouseGrabbed(boolean grabbed);

	/**
	 * Gets the current state of the key.
	 *
	 * @param key The key to check the state of
	 *
	 * @return True if the key is currently pressed down
	 */
	public boolean isKeyDown(Key key);

	/**
	 * Set to true if keyboard events should be fired continuously while a key or mouse button is pressed down.
	 *
	 * @param enable True to enable repeat event
	 */
	public void enableRepeatKeyEvents(boolean enable);

	/**
	 * Registers this function to handle keyboard input of the given keys.
	 *
	 * @param function The function to handle the input
	 * @param keys     The keys this function will handle
	 */
	public void registerKeyBinding(Runnable function, Key... keys);
}
