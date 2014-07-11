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
package me.thehutch.fusion.engine;

import me.thehutch.fusion.api.IClient;
import me.thehutch.fusion.api.Platform;
import me.thehutch.fusion.api.event.EventPriority;
import me.thehutch.fusion.api.input.mouse.MouseMotionEvent;
import me.thehutch.fusion.api.maths.MathsHelper;
import me.thehutch.fusion.api.maths.Quaternion;
import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.render.Camera;
import me.thehutch.fusion.api.scheduler.TaskPriority;
import me.thehutch.fusion.engine.client.Window;
import me.thehutch.fusion.engine.filesystem.loaders.ImageLoader;
import me.thehutch.fusion.engine.filesystem.loaders.MeshManager;
import me.thehutch.fusion.engine.filesystem.loaders.ProgramManager;
import me.thehutch.fusion.engine.filesystem.loaders.TextureManager;
import me.thehutch.fusion.engine.input.InputManager;
import me.thehutch.fusion.engine.render.Renderer;

/**
 * @author thehutch
 */
public final class Client extends Engine implements IClient {
	private static final float MOUSE_SENSITIVITY = 10.0f;
	private final InputManager inputManager;
	private final Renderer renderer;
	private final Window window;

	private float cameraPitch;
	private float cameraYaw;

	protected Client(Application application) {
		super(application);
		// Create the window
		this.window = new Window(getLogger(), 800, 600);

		// Create the scene
		this.renderer = new Renderer(this, Camera.createPerspective(70.0f, window.getResolution().getAspectRatio(), 0.01f, 1000.0f));

		// Create the input manager
		this.inputManager = new InputManager(this);
	}

	@Override
	public void initialise() {
		// Schedule the input manager task
		getScheduler().scheduleSyncRepeatingTask(inputManager::execute, TaskPriority.CRITICAL, 0L, 1L);

		// Register the model loader
		getFileSystem().registerResourceManager(new MeshManager(), "obj");
		// Register the program loader
		getFileSystem().registerResourceManager(new ProgramManager(), "fprg");
		// Register the image loader
		getFileSystem().registerResourceManager(new ImageLoader(), "png", "jpg");
		// Register the texture loader
		getFileSystem().registerResourceManager(new TextureManager(getFileSystem()), "ftex");

		// Add the scene to the component system
		getComponentSystem().addProcessor(renderer);

		/*
		 * Register the mouse motion event
		 */
		getEventManager().registerEvent((MouseMotionEvent event) -> {
			if (getInputManager().isMouseGrabbed()) {
				final float sensitivity = MOUSE_SENSITIVITY * getScheduler().getDelta();

				this.cameraPitch += event.getDY() * sensitivity;
				this.cameraPitch = MathsHelper.clamp(cameraPitch, -90.0f, 90.0f);
				final Quaternion pitch = Quaternion.fromAxisAngleDeg(Vector3.UNIT_X, cameraPitch);

				this.cameraYaw -= event.getDX() * sensitivity;
				this.cameraYaw %= 360;
				final Quaternion yaw = Quaternion.fromAxisAngleDeg(Vector3.UNIT_Y, cameraYaw);

				getRenderer().getCamera().setRotation(yaw.mul(pitch));
			}
		}, MouseMotionEvent.class, EventPriority.HIGH, true);

		// Call the Engine initialise()
		super.initialise();
	}

	public Renderer getRenderer() {
		return renderer;
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
		//this.inputManager.dispose();
		//this.window.dispose();
		super.stop(reason);
	}
}
