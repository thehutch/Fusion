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
package me.thehutch.fusion.api.input.mouse;

import me.thehutch.fusion.api.event.Event;

/**
 * An event to represent when a mouse button is clicked or released.
 *
 * @author thehutch
 */
public class MouseButtonEvent extends Event {
	private final MouseButton button;
	private final boolean state;
	private final int x;
	private final int y;

	public MouseButtonEvent(MouseButton button, int x, int y, boolean state) {
		this.button = button;
		this.state = state;
		this.x = x;
		this.y = y;
	}

	/**
	 * The x-coordinate on the window where the mouse was clicked/released.
	 *
	 * @return The x-coordinate of the mouse
	 */
	public int getX() {
		return x;
	}

	/**
	 * The y-coordinate on the window where the mouse was clicked/released.
	 *
	 * @return The y-coordinate of the mouse
	 */
	public int getY() {
		return y;
	}

	/**
	 * The button which was clicked/released.
	 *
	 * @return The event mouse button
	 */
	public MouseButton getButton() {
		return button;
	}

	/**
	 * The current state of the mouse button.
	 *
	 * @return True if the mouse button is down
	 */
	public boolean getState() {
		return state;
	}
}
