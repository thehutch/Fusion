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
 * An event to represent when the mouse is moved.
 *
 * @author thehutch
 */
public final class MouseMotionEvent extends Event {
	private final int dx;
	private final int dy;
	private final int x;
	private final int y;

	/**
	 * The default constructor for {@link MouseMotionEvent}.
	 *
	 * @param dx The relative motion of the mouse in the x-axis
	 * @param dy The relative motion of the mouse in the y-axis
	 * @param x  The x position of the mouse
	 * @param y  The y position of the mouse
	 */
	public MouseMotionEvent(int dx, int dy, int x, int y) {
		super(true);
		this.dx = dx;
		this.dy = dy;
		this.x = x;
		this.y = y;
	}

	/**
	 * The new x-coordinate of the mouse.
	 *
	 * @return The x-coordinate of the mouse
	 */
	public int getX() {
		return x;
	}

	/**
	 * The new y-coordinate of the mouse.
	 *
	 * @return The y-coordinate of the mouse
	 */
	public int getY() {
		return y;
	}

	/**
	 * The relative motion of the mouse in the x-axis.
	 *
	 * @return The amount the mouse moved in the x-axis
	 */
	public int getDX() {
		return dx;
	}

	/**
	 * The relative motion of the mouse in the y-axis.
	 *
	 * @return The amount the mouse moved in the y-axis
	 */
	public int getDY() {
		return dy;
	}
}
