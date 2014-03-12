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

/**
 * @author thehutch
 */
public enum MouseButton {
	LEFT_BUTTON(0x00),
	RIGHT_BUTTON(0x01),
	MIDDLE_BUTTON(0x02);
	private final int buttonIndex;

	private MouseButton(int buttonIndex) {
		this.buttonIndex = buttonIndex;
	}

	/**
	 * Gets the index of the mouse button.
	 *
	 * @return The button index
	 */
	public int getButtonIndex() {
		return buttonIndex;
	}
}
