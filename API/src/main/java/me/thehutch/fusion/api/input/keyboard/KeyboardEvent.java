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
package me.thehutch.fusion.api.input.keyboard;

import me.thehutch.fusion.api.event.Event;

/**
 * @author thehutch
 */
public class KeyboardEvent extends Event {
	private final Key keycode;
	private final boolean isDown;
	private final boolean repeat;

	public KeyboardEvent(Key keycode, boolean isDown, boolean repeat) {
		this.keycode = keycode;
		this.isDown = isDown;
		this.repeat = repeat;
	}

	public Key getKeycode() {
		return keycode;
	}

	public boolean isDown() {
		return isDown;
	}

	public boolean isRepeat() {
		return repeat;
	}
}
