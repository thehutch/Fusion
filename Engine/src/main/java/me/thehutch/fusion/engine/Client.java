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

import com.flowpowered.math.imaginary.Quaternionf;
import me.thehutch.fusion.api.IClient;
import me.thehutch.fusion.api.Platform;
import me.thehutch.fusion.api.event.EventPriority;
import me.thehutch.fusion.api.input.keyboard.Key;
import me.thehutch.fusion.api.input.mouse.MouseMotionEvent;
import me.thehutch.fusion.api.scene.Camera;
import me.thehutch.fusion.api.scheduler.TaskPriority;
import me.thehutch.fusion.engine.input.InputManager;
import me.thehutch.fusion.engine.scene.Scene;

/**
 * @author thehutch
 */
public final class Client extends Engine implements IClient {
	private final InputManager inputManager;
	private final Scene scene;

	protected Client(Application application) {
		super(application);
		// TODO: Get the window size either from the commandline or config file
		this.scene = new Scene(this, Camera.createPerspective(70.0f, 800.0f / 600.0f, 0.1f, 1000.0f), "/shaders/");
		this.inputManager = new InputManager(this);
		// Schedule the input manager task
		getScheduler().scheduleSyncRepeatingTask(getInputManager()::execute, TaskPriority.CRITICAL, 0L, 1L);
		// Schedule the scene task
		getScheduler().scheduleSyncRepeatingTask(getScene()::execute, TaskPriority.HIGHEST, 0L, 1L);

		getInputManager().registerKeyBinding(() -> {
			getScene().getCamera().moveLocalZ(-getScheduler().getDelta() * 0.5f);
		}, Key.KEY_W);

		getInputManager().registerKeyBinding(() -> {
			getScene().getCamera().moveLocalX(-getScheduler().getDelta() * 0.5f);
		}, Key.KEY_A);

		getInputManager().registerKeyBinding(() -> {
			getScene().getCamera().moveLocalZ(getScheduler().getDelta() * 0.5f);
		}, Key.KEY_S);

		getInputManager().registerKeyBinding(() -> {
			getScene().getCamera().moveLocalX(getScheduler().getDelta() * 0.5f);
		}, Key.KEY_D);

		getInputManager().registerKeyBinding(() -> {
			getScene().getCamera().moveLocalY(getScheduler().getDelta() * 0.5f);
		}, Key.KEY_SPACE);

		getInputManager().registerKeyBinding(() -> {
			getScene().getCamera().moveLocalY(-getScheduler().getDelta() * 0.5f);
		}, Key.KEY_LCONTROL);

		getInputManager().registerKeyBinding(() -> {
			stop("Escape Pressed");
		}, Key.KEY_ESCAPE);

		getEventManager().registerEvent((MouseMotionEvent event) -> {
			final Quaternionf yaw = Quaternionf.fromAngleRadAxis(-getScheduler().getDelta() * event.getDX(), getScene().getCamera().getUp());
			final Quaternionf pitch = Quaternionf.fromAngleRadAxis(getScheduler().getDelta() * event.getDY(), getScene().getCamera().getRight());
			getScene().getCamera().rotate(pitch.mul(yaw));
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
}
