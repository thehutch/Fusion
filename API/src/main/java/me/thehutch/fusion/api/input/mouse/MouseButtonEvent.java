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
package me.thehutch.fusion.api.input.mouse;

import me.thehutch.fusion.api.event.Event;

/**
 * An event to represent when a mouse button is clicked or released.
 *
 * @author thehutch
 */
public final class MouseButtonEvent extends Event {
	private final boolean state;
	private final int button;
	private final int x;
	private final int y;

	/**
	 * The default constructor for {@link MouseButtonEvent}.
	 *
	 * @param button The button of the event
	 * @param x      The x position of the mouse
	 * @param y      The y position of the mouse
	 * @param state  The new state of the button
	 */
	public MouseButtonEvent(int button, int x, int y, boolean state) {
		super(true);
		this.button = button;
		this.state = state;
		this.x = x;
		this.y = y;
	}

	/**
	 * The x-coordinate of the mouse.
	 *
	 * @return The x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * The y-coordinate of the mouse.
	 *
	 * @return The y-coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * The button used in this event.
	 *
	 * @return The event mouse button
	 */
	public int getButton() {
		return button;
	}

	/**
	 * The state of the mouse button.
	 *
	 * @return True if the mouse button is down
	 */
	public boolean getState() {
		return state;
	}
}
