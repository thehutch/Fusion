/*
 * This file is part of Engine, licensed under the Apache 2.0 License.
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
package me.thehutch.fusion.engine.input;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import java.util.Set;
import me.thehutch.fusion.api.input.IInputManager;
import me.thehutch.fusion.api.input.keyboard.Key;
import me.thehutch.fusion.api.input.keyboard.KeyboardEvent;
import me.thehutch.fusion.api.input.mouse.MouseButtonEvent;
import me.thehutch.fusion.api.input.mouse.MouseMotionEvent;
import me.thehutch.fusion.api.input.mouse.MouseWheelMotionEvent;
import me.thehutch.fusion.engine.Engine;
import me.thehutch.fusion.engine.event.EventManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * @author thehutch
 */
public final class InputManager implements IInputManager {
	private final TMap<Key, Set<Runnable>> mKeyBindings = new THashMap<>();
	private final EventManager mEventManager;
	private final Engine mEngine;

	/**
	 * Default constructor for {@link InputManager}.
	 *
	 * @param engine The game engine
	 */
	public InputManager(Engine engine) {
		mEngine = engine;
		mEventManager = engine.getEventManager();
		try {
			// Create the mouse and keyboard
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException ex) {
			ex.printStackTrace();
		}
		// Enable repeating keys
		enableRepeatKeyEvents(true);
	}

	/**
	 * Polls the OS for input events and then invoke events based on it.
	 */
	public void execute() {
		// Check for display close
		if (Display.isCloseRequested()) {
			mEngine.stop("Displayed Closed");
		}

		// Get the keybindings
		final TMap<Key, Set<Runnable>> bindings = mKeyBindings;

		// Check and execute each key binding
		bindings.keySet().stream()
			.filter(key -> isKeyDown(key))
			.map(key -> bindings.get(key))
			.forEach(executors -> {
				executors.stream().forEach(executor -> {
					executor.run();
				});
			});

		// Get the event manager
		final EventManager evManager = mEventManager;

		// Check for keyboard events
		while (Keyboard.next()) {
			// Get the event keycode
			final int keycode = Keyboard.getEventKey();
			// Call the KeyboardEvent
			evManager.invoke(new KeyboardEvent(Key.fromKeycode(keycode), Keyboard.getEventKeyState(), Keyboard.isRepeatEvent()));
		}

		// Check for mouse events
		while (Mouse.next()) {
			// Get the event mouse button
			final int mouseButton = Mouse.getEventButton();
			if (mouseButton != -1) {
				// Invoke the mouse button event
				evManager.invoke(new MouseButtonEvent(mouseButton, Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventButtonState()));
			}
			// Check if the mouse has moved
			final int mouseDX = Mouse.getEventDX();
			final int mouseDY = Mouse.getEventDY();
			if (mouseDX != 0 || mouseDY != 0) {
				// Invoke the mouse motion event
				evManager.invoke(new MouseMotionEvent(mouseDX, mouseDY, Mouse.getEventX(), Mouse.getEventY()));
			}
			// Check if the mouse wheel has moved
			final int mouseWheelDelta = Mouse.getEventDWheel();
			if (mouseWheelDelta != 0) {
				// Invoke the mouse wheel motion event
				evManager.invoke(new MouseWheelMotionEvent(mouseWheelDelta));
			}
		}

		// Check for display close
		if (Display.isCloseRequested()) {
			mEngine.stop("Displayed Closed");
		}

		//TODO: Possibilty of other input devices (Joystick, Touchscreen etc...)
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMouseX() {
		return Mouse.getX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMouseY() {
		return Mouse.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMouseDown(int button) {
		return Mouse.isButtonDown(button);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMouseGrabbed() {
		return Mouse.isGrabbed();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMouseGrabbed(boolean grabbed) {
		Mouse.setGrabbed(grabbed);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toggleMouseGrab() {
		setMouseGrabbed(!isMouseGrabbed());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isKeyDown(Key key) {
		return Keyboard.isKeyDown(key.getKeycode());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enableRepeatKeyEvents(boolean enable) {
		Keyboard.enableRepeatEvents(enable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerKeyBinding(Runnable function, Key... keys) {
		if (keys.length == 0) {
			throw new IllegalArgumentException("Can not register key binding with no keys bound.");
		}
		final TMap<Key, Set<Runnable>> bindings = mKeyBindings;
		for (int i = 0; i < keys.length; ++i) {
			Set<Runnable> functions = bindings.get(keys[i]);
			if (functions == null) {
				functions = new THashSet<>();
				bindings.put(keys[i], functions);
			}
			functions.add(function);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		if (Keyboard.isCreated()) {
			Keyboard.destroy();
		}
		if (Mouse.isCreated()) {
			Mouse.destroy();
		}
	}
}
