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

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

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
import me.thehutch.fusion.engine.client.Window;
import me.thehutch.fusion.engine.input.InputManager;
import me.thehutch.fusion.engine.scene.Scene;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * @author thehutch
 */
public final class Client extends Engine implements IClient {
	private static final float MOUSE_SENSITIVITY = 10.0f;
	private static final float MOVE_SPEED = 2.0f;
	private final InputManager inputManager;
	private final Window window;
	private final Scene scene;

	private float cameraPitch;
	private float cameraYaw;

	protected Client(Application application) {
		super(application);
		this.window = new Window(getLogger(), 800, 600);
		this.scene = new Scene(this, Camera.createPerspective(70.0f, (float) Display.getWidth() / Display.getHeight(), 0.01f, 1000.0f));
		this.inputManager = new InputManager(this);
		// Schedule the input manager task
		getScheduler().scheduleSyncRepeatingTask(inputManager::execute, TaskPriority.CRITICAL, 0L, 1L);
		// Schedule the scene task
		getScheduler().scheduleSyncRepeatingTask(scene::execute, TaskPriority.CRITICAL, 0L, 1L);

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

		// Enable depth testing
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glEnable(GL_DEPTH_CLAMP);

		// Enable face culling
		GL11.glEnable(GL_CULL_FACE);
		GL11.glCullFace(GL_BACK);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public Window getWindow() {
		return window;
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
