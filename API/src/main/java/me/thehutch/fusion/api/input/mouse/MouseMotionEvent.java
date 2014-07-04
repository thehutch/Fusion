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
public class MouseMotionEvent extends Event {
	private final float dx;
	private final float dy;
	private final int x;
	private final int y;

	public MouseMotionEvent(float dx, float dy, int x, int y) {
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
	 * The relative motion of the move in the x-axis.
	 *
	 * @return The amount the mouse moved in the x-axis
	 */
	public float getDX() {
		return dx;
	}

	/**
	 * The relative motion of the move in the y-axis.
	 *
	 * @return The amount the mouse moved in the y-axis
	 */
	public float getDY() {
		return dy;
	}
}
