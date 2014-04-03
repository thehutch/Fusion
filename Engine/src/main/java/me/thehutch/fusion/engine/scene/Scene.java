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
package me.thehutch.fusion.engine.scene;

import static me.thehutch.fusion.engine.Engine.ENGINE_VERSION;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;

import com.flowpowered.math.vector.Vector3f;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import me.thehutch.fusion.api.scene.Camera;
import me.thehutch.fusion.api.scene.IScene;
import me.thehutch.fusion.api.scene.ISceneNode;
import me.thehutch.fusion.engine.Client;
import me.thehutch.fusion.engine.filesystem.FileSystem;
import me.thehutch.fusion.engine.filesystem.loaders.ModelLoader;
import me.thehutch.fusion.engine.filesystem.loaders.TextureLoader;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public final class Scene implements IScene {
	public static final Path TEXTURE_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("textures");
	public static final Path SHADER_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("shaders");
	public static final Path MODELS_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("models");
	public static final Path MESH_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("meshes");
	private final Collection<ISceneNode> nodes = new ArrayList<>();
	private final Camera camera;
	private final Client engine;

	public Scene(Client engine, Camera camera) {
		this.camera = camera;
		this.engine = engine;
		// Register the model loader
		engine.getFileSystem().registerLoader(new ModelLoader(engine.getFileSystem()), "fmdl");
		// Register the texture loader
		engine.getFileSystem().registerLoader(new TextureLoader(), "jpg", "png");

		// Create the window
		createWindow();

		// Create the model
		final Model model = createModel("teleporter.fmdl", 0.0f, 0.0f, 0.0f);
		model.scale(0.125f);

		// Disable blending
		GL11.glDisable(GL_BLEND);
		// Enable depth mask
		GL11.glDepthMask(true);
		// Enable depth testing
		GL11.glEnable(GL_DEPTH_TEST);

		// Enable backface culling
		GL11.glEnable(GL_CULL_FACE);
		GL11.glCullFace(GL_BACK);
	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public void addNode(ISceneNode node) {
		this.nodes.add(node);
	}

	@Override
	public void removeNode(ISceneNode node) {
		this.nodes.remove(node);
		node.dispose();
	}

	@Override
	public Model createModel(String name, Vector3f position) {
		final Model model = new Model(camera, engine.getFileSystem().getResource(MODELS_DIRECTORY.resolve(name)));
		model.setPosition(position);
		addNode(model);
		return model;
	}

	@Override
	public Model createModel(String name, float x, float y, float z) {
		return createModel(name, new Vector3f(x, y, z));
	}

	@Override
	public void dispose() {
		this.nodes.stream().forEach((node) -> {
			node.dispose();
		});
	}

	public void execute() {
		// Clear the screen
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Get the delta from the scheduler
		final float delta = engine.getScheduler().getDelta();

		// Update the scene
		this.nodes.stream().forEach(node -> {
			node.update(delta);
		});
		// Render the scene
		this.nodes.stream().forEach(node -> {
			node.render();
		});

		// Update the display
		Display.update();
	}

	private void createWindow() {
		try {
			Display.setTitle("Fusion Engine | " + ENGINE_VERSION);
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setVSyncEnabled(true);
			Display.create(new PixelFormat(), new ContextAttribs(3, 3).withProfileCore(true));
		} catch (LWJGLException ex) {
			ex.printStackTrace();
		}
		GL11.glClearColor(0.0f, 0.50f, 0.75f, 1.0f);
	}
}
