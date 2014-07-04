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
public class KeyboardEvent extends Event {
	private final Key keycode;
	private final boolean state;
	private final boolean repeat;

	public KeyboardEvent(Key keycode, boolean state, boolean repeat) {
		this.keycode = keycode;
		this.state = state;
		this.repeat = repeat;
	}

	/**
	 * The key involved in the event.
	 *
	 * @return The event key
	 */
	public Key getKeycode() {
		return keycode;
	}

	/**
	 * Gets the current state of the event key.
	 *
	 * @return True if the key is pressed down
	 */
	public boolean getState() {
		return state;
	}

	/**
	 * A repeat key is when the key has been pressed down and continues to fire events.
	 *
	 * @return True if the event is for a repeat key
	 */
	public boolean isRepeat() {
		return repeat;
	}
}
