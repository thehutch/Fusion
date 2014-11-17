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
	private final int mDX;
	private final int mDY;
	private final int mX;
	private final int mY;

	/**
	 * The default constructor for {@link MouseMotionEvent}.
	 *
	 * @param dx The relative motion of the mouse in the mX-axis
	 * @param dy The relative motion of the mouse in the mY-axis
	 * @param x  The mX position of the mouse
	 * @param y  The mY position of the mouse
	 */
	public MouseMotionEvent(int dx, int dy, int x, int y) {
		super(true);
		mDX = dx;
		mDY = dy;
		mX = x;
		mY = y;
	}

	/**
	 * The new mX-coordinate of the mouse.
	 *
	 * @return The mX-coordinate of the mouse
	 */
	public int getX() {
		return mX;
	}

	/**
	 * The new mY-coordinate of the mouse.
	 *
	 * @return The mY-coordinate of the mouse
	 */
	public int getY() {
		return mY;
	}

	/**
	 * The relative motion of the mouse in the mX-axis.
	 *
	 * @return The amount the mouse moved in the mX-axis
	 */
	public int getDX() {
		return mDX;
	}

	/**
	 * The relative motion of the mouse in the mY-axis.
	 *
	 * @return The amount the mouse moved in the mY-axis
	 */
	public int getDY() {
		return mDY;
	}
}
