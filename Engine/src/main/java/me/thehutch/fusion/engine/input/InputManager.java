/*
 * This file is part of Engine.
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import me.thehutch.fusion.api.input.IInputManager;
import me.thehutch.fusion.api.input.keyboard.Key;
import me.thehutch.fusion.api.input.keyboard.KeyBinding;
import me.thehutch.fusion.api.input.keyboard.KeyboardEvent;
import me.thehutch.fusion.api.input.mouse.MouseButton;
import me.thehutch.fusion.api.input.mouse.MouseButtonEvent;
import me.thehutch.fusion.api.input.mouse.MouseMotionEvent;
import me.thehutch.fusion.api.input.mouse.MouseWheelMotionEvent;
import me.thehutch.fusion.api.util.ReflectionHelper;
import me.thehutch.fusion.engine.Engine;
import me.thehutch.fusion.engine.event.EventManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * @author thehutch
 */
public class InputManager implements IInputManager, Runnable {
	private final TMap<Key, Set<KeyBindingExecutor>> keyBindings = new THashMap<>();
	private final EventManager eventManager;
	private final Engine engine;

	public InputManager(Engine engine) {
		this.engine = engine;
		this.eventManager = engine.getEventManager();
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

	@Override
	public int getMouseX() {
		return Mouse.getX();
	}

	@Override
	public int getMouseY() {
		return Mouse.getY();
	}

	@Override
	public boolean isKeyDown(Key key) {
		return Keyboard.isKeyDown(key.getKeycode());
	}

	@Override
	public boolean isMouseDown(MouseButton button) {
		return Mouse.isButtonDown(button.getButtonIndex());
	}

	@Override
	public void enableRepeatKeyEvents(boolean enable) {
		Keyboard.enableRepeatEvents(enable);
	}

	@Override
	public void run() {
		// Check for display close
		if (Display.isCloseRequested()) {
			this.engine.stop("Displayed Closed");
		}
		// Check for the keybindings
		for (Key key : keyBindings.keySet()) {
			if (isKeyDown(key)) {
				final Set<KeyBindingExecutor> executors = keyBindings.get(key);
				for (KeyBindingExecutor executor : executors) {
					executor.execute();
				}
			}
		}
		// Check for keyboard events
		while (Keyboard.next()) {
			// Get the event keycode
			final int keycode = Keyboard.getEventKey();
			// Call the KeyboardEvent
			this.eventManager.callEvent(new KeyboardEvent(Key.fromKeycode(keycode), Keyboard.getEventKeyState(), Keyboard.isRepeatEvent()));
		}
		// Check for mouse events
		while (Mouse.next()) {
			// Get the event mouse button
			final int mouseButton = Mouse.getEventButton();
			if (mouseButton != -1) {
				// Call the mouse button event
				this.eventManager.callEvent(new MouseButtonEvent(MouseButton.values()[mouseButton], Mouse.getEventX(), Mouse.getY(), Mouse.getEventButtonState()));
			}
			// Check if the mouse has moved
			if (Mouse.getEventDX() != 0.0f || Mouse.getEventDY() != 0.0f) {
				// Call the mouse motion event
				this.eventManager.callEvent(new MouseMotionEvent(Mouse.getEventDX(), Mouse.getEventDY(), Mouse.getEventX(), Mouse.getEventY()));
			}
			// Check if the mouse wheel has moved
			if (Mouse.getEventDWheel() != 0.0f) {
				// Call the mouse wheel motion event
				this.eventManager.callEvent(new MouseWheelMotionEvent(Mouse.getEventDWheel()));
			}
		}
		//TODO: Possibilty of other input devices (Joystick, Touchscreen etc...)
	}

	@Override
	public void registerKeyBinding(Object instance) {
		final Collection<Method> methods = ReflectionHelper.getAnnotatedMethods(instance.getClass(), KeyBinding.class);
		for (Method method : methods) {
			final KeyBinding keyBinding = method.getAnnotation(KeyBinding.class);
			if (ReflectionHelper.hasExactParameters(method)) {
				for (int i = 0; i < keyBinding.keys().length; ++i) {
					Set<KeyBindingExecutor> executors = keyBindings.get(keyBinding.keys()[i]);
					if (executors == null) {
						executors = new THashSet<>();
					}
					executors.add(new KeyBindingExecutor(instance, method));
					this.keyBindings.put(keyBinding.keys()[i], executors);
				}
			}
		}
	}

	private class KeyBindingExecutor {
		private final Object invoker;
		private final Method method;

		private KeyBindingExecutor(Object invoker, Method method) {
			this.invoker = invoker;
			this.method = method;
		}

		public void execute() {
			try {
				this.method.invoke(invoker);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				ex.printStackTrace();
			}
		}
	}
}
