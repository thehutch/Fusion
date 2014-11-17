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
 * An event to represent when a mouse mButton is clicked or released.
 *
 * @author thehutch
 */
public final class MouseButtonEvent extends Event {
	private final boolean mState;
	private final int mButton;
	private final int mX;
	private final int mY;

	/**
	 * The default constructor for {@link MouseButtonEvent}.
	 *
	 * @param button The mButton of the event
	 * @param x      The mx position of the mouse
	 * @param y      The mY position of the mouse
	 * @param state  The new state of the mButton
	 */
	public MouseButtonEvent(int button, int x, int y, boolean state) {
		super(true);
		mButton = button;
		mState = state;
		mX = x;
		mY = y;
	}

	/**
	 * The mx-coordinate of the mouse.
	 *
	 * @return The mx-coordinate
	 */
	public int getX() {
		return mX;
	}

	/**
	 * The mY-coordinate of the mouse.
	 *
	 * @return The mY-coordinate
	 */
	public int getY() {
		return mY;
	}

	/**
	 * The mButton used in this event.
	 *
	 * @return The event mouse mButton
	 */
	public int getButton() {
		return mButton;
	}

	/**
	 * The state of the mouse mButton.
	 *
	 * @return True if the mouse mButton is down
	 */
	public boolean getState() {
		return mState;
	}
}
