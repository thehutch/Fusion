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
package me.thehutch.fusion.engine;

import me.thehutch.fusion.api.IClient;
import me.thehutch.fusion.api.Platform;
import me.thehutch.fusion.api.event.EventPriority;
import me.thehutch.fusion.api.input.keyboard.Key;
import me.thehutch.fusion.api.input.keyboard.KeyboardEvent;
import me.thehutch.fusion.api.input.mouse.MouseMotionEvent;
import me.thehutch.fusion.api.maths.MathsHelper;
import me.thehutch.fusion.api.maths.Quaternion;
import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.scene.Camera;
import me.thehutch.fusion.api.scheduler.TaskPriority;
import me.thehutch.fusion.engine.input.InputManager;
import me.thehutch.fusion.engine.scene.Scene;

/**
 * @author thehutch
 */
public final class Client extends Engine implements IClient {
	private static final float MOUSE_SENSITIVITY = 10.0f;
	private static final float MOVE_SPEED = 2.0f;
	private final InputManager inputManager;
	private final Scene scene;

	private float cameraPitch;
	private float cameraYaw;

	protected Client(Application application) {
		super(application);
		// TODO: Get the window size either from the commandline or config file
		this.scene = new Scene(this, Camera.createPerspective(70.0f, 800.0f / 600.0f, 0.01f, 1000.0f));
		this.inputManager = new InputManager(this);
		// Schedule the input manager task
		getScheduler().scheduleSyncRepeatingTask(getInputManager()::execute, TaskPriority.CRITICAL, 0L, 1L);
		// Schedule the scene task
		getScheduler().scheduleSyncRepeatingTask(getScene()::execute, TaskPriority.CRITICAL, 0L, 1L);

		getInputManager().setMouseGrabbed(true);

		getInputManager().registerKeyBinding(() -> {
			getScene().getCamera().moveLocalZ(-getScheduler().getDelta() * MOVE_SPEED);
		}, Key.KEY_W);

		getInputManager().registerKeyBinding(() -> {
			getScene().getCamera().moveLocalX(-getScheduler().getDelta() * MOVE_SPEED);
		}, Key.KEY_A);

		getInputManager().registerKeyBinding(() -> {
			getScene().getCamera().moveLocalZ(getScheduler().getDelta() * MOVE_SPEED);
		}, Key.KEY_S);

		getInputManager().registerKeyBinding(() -> {
			getScene().getCamera().moveLocalX(getScheduler().getDelta() * MOVE_SPEED);
		}, Key.KEY_D);

		getInputManager().registerKeyBinding(() -> {
			getScene().getCamera().moveY(getScheduler().getDelta() * MOVE_SPEED);
		}, Key.KEY_SPACE);

		getInputManager().registerKeyBinding(() -> {
			getScene().getCamera().moveY(-getScheduler().getDelta() * MOVE_SPEED);
		}, Key.KEY_LSHIFT);

		getInputManager().registerKeyBinding(() -> {
			stop("Escape Pressed");
		}, Key.KEY_ESCAPE);

		getEventManager().registerEvent((KeyboardEvent event) -> {
			if (event.getKeycode() == Key.KEY_GRAVE && event.getState() && !event.isRepeat()) {
				getInputManager().toggleMouseGrab();
			}
		}, KeyboardEvent.class, EventPriority.MEDIUM, true);

		getEventManager().registerEvent((MouseMotionEvent event) -> {
			if (getInputManager().isMouseGrabbed()) {
				final float sensitivity = MOUSE_SENSITIVITY * getScheduler().getDelta();

				this.cameraPitch += event.getDY() * sensitivity;
				this.cameraPitch = MathsHelper.clamp(cameraPitch, -90.0f, 90.0f);
				final Quaternion pitch = Quaternion.fromAxisAngleDeg(Vector3.UNIT_X, cameraPitch);

				this.cameraYaw -= event.getDX() * sensitivity;
				this.cameraYaw %= 360;
				final Quaternion yaw = Quaternion.fromAxisAngleDeg(Vector3.UNIT_Y, cameraYaw);

				getScene().getCamera().setRotation(yaw.mul(pitch));
			}
		}, MouseMotionEvent.class, EventPriority.HIGH, true);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public InputManager getInputManager() {
		return inputManager;
	}

	@Override
	public Platform getPlatform() {
		return Platform.CLIENT;
	}

	@Override
	public void stop(String reason) {
		// Dispose of the scene
		getScene().dispose();
		// Stop the engine
		super.stop(reason);
	}
}
