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
import me.thehutch.fusion.api.input.keyboard.Key;
import me.thehutch.fusion.api.render.Camera;
import me.thehutch.fusion.api.scheduler.TaskPriority;
import me.thehutch.fusion.api.util.GLVersion;
import me.thehutch.fusion.engine.filesystem.loaders.ImageLoader;
import me.thehutch.fusion.engine.filesystem.loaders.MaterialLoader;
import me.thehutch.fusion.engine.filesystem.loaders.MeshManager;
import me.thehutch.fusion.engine.filesystem.loaders.ProgramManager;
import me.thehutch.fusion.engine.filesystem.loaders.TextureManager;
import me.thehutch.fusion.engine.input.InputManager;
import me.thehutch.fusion.engine.render.Renderer;
import me.thehutch.fusion.engine.render.opengl.GLContext;
import me.thehutch.fusion.engine.render.opengl.gl30.OpenGL30Context;

/**
 * @author thehutch
 */
public final class Client extends Engine implements IClient {
	private final InputManager mInputManager;
	private final GLContext mContext;
	private final Renderer mRenderer;

	protected Client(Application application) {
		super(application);
		// Create the opengl mContext
		mContext = new OpenGL30Context();
		mContext.create();

		// Create the scene
		mRenderer = new Renderer(this, Camera.createPerspective(70.0f, mContext.getWindowPositionAndSize().getAspectRatio(), 0.01f, 1000.0f));

		// Create the input manager
		mInputManager = new InputManager(this);
	}

	@Override
	public void initialise() {
		// Schedule the input manager task
		getScheduler().invokeRepeating(mInputManager::execute, TaskPriority.CRITICAL, 0L, 1L);

		// Register the image loader
		getFileSystem().registerResourceManager(new ImageLoader(), "png", "jpg");
		// Register the texture loader
		getFileSystem().registerResourceManager(new TextureManager(this), "ftex");
		// Register the program loader
		getFileSystem().registerResourceManager(new ProgramManager(this), "fprg");
		// Register the material loader
		getFileSystem().registerResourceManager(new MaterialLoader(this), "fmat");
		// Register the model loader
		getFileSystem().registerResourceManager(new MeshManager(mContext), "obj");

		// Add the scene to the component system
		getComponentSystem().addProcessor(mRenderer);

		// Enable mouse grab toggle
		getInputManager().registerKeyBinding(() -> {
			getInputManager().toggleMouseGrab();
		}, Key.KEY_GRAVE);

		// Enable exit key
		getInputManager().registerKeyBinding(() -> {
			stop("Engine exit");
		}, Key.KEY_ESCAPE);

		// Enable mouse grab
		getInputManager().setMouseGrabbed(true);

		// Call the Engine initialise()
		super.initialise();
	}

	public Renderer getRenderer() {
		return mRenderer;
	}

	@Override
	public Camera getCamera() {
		return mRenderer.getCamera();
	}

	@Override
	public GLContext getContext() {
		return mContext;
	}

	@Override
	public InputManager getInputManager() {
		return mInputManager;
	}

	@Override
	public Platform getPlatform() {
		return Platform.CLIENT;
	}

	@Override
	public GLVersion getOpenGLVersion() {
		return mContext.getGLVersion();
	}

	@Override
	public void stop(String reason) {
		//this.mInputManager.dispose();
		//this.window.dispose();
		super.stop(reason);
	}
}
