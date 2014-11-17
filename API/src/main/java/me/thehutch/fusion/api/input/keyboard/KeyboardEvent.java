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
package me.thehutch.fusion.api.input.keyboard;

import me.thehutch.fusion.api.event.Event;

/**
 * @author thehutch
 */
public final class KeyboardEvent extends Event {
	private final Key mKey;
	private final boolean mState;
	private final boolean mRepeat;

	/**
	 * The default constructor for {@link KeyboardEvent}.
	 *
	 * @param key    The mKey
	 * @param state  The state of the mKey
	 * @param repeat True if this is a repeated mKey event
	 */
	public KeyboardEvent(Key key, boolean state, boolean repeat) {
		super(true);
		mKey = key;
		mState = state;
		mRepeat = repeat;
	}

	/**
	 * The mKey involved in the event.
	 *
	 * @return The event mKey
	 */
	public Key getKey() {
		return mKey;
	}

	/**
	 * Gets the current state of the event mKey.
	 *
	 * @return True if the mKey is pressed down
	 */
	public boolean getState() {
		return mState;
	}

	/**
	 * A mRepeat mKey is when the mKey has been pressed down and continues to fire events.
	 *
	 * @return True if the event is for a mRepeat mKey
	 */
	public boolean isRepeat() {
		return mRepeat;
	}
}
