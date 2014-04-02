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
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import me.thehutch.fusion.api.scene.Camera;
import me.thehutch.fusion.api.scene.IScene;
import me.thehutch.fusion.api.scene.ISceneNode;
import me.thehutch.fusion.engine.Client;
import me.thehutch.fusion.engine.filesystem.FileSystem;
import me.thehutch.fusion.engine.render.Program;
import me.thehutch.fusion.engine.render.Texture;
import me.thehutch.fusion.engine.render.VertexData;
import me.thehutch.fusion.engine.util.WavefrontOBJLoader;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public final class Scene implements IScene {
	private static final String VERTEX_SHADER_EXTENSION = ".vs";
	private static final String FRAGMENT_SHADER_EXTENSION = ".fs";
	private final Collection<ISceneNode> nodes = new ArrayList<>();
	private final TMap<String, Program> programs = new THashMap<>();
	private final TMap<String, Texture> textures = new THashMap<>();
	private final Path shaderDirectory;
	private final Camera camera;
	private final Client engine;

	public Scene(Client engine, Camera camera, String shaderDirectory) {
		this.shaderDirectory = FileSystem.DATA_DIRECTORY.resolve(shaderDirectory);
		this.camera = camera;
		this.engine = engine;
		// Create the window
		createWindow();
		//TODO: Load all programs from the directory
		// Create the vertex data
		final VertexData mesh = WavefrontOBJLoader.load(engine.getFileSystem().getResourceStream(FileSystem.DATA_DIRECTORY.resolve("models").resolve("teleporter.obj")));

		// Create the model
		final Model model = new Model(this, getProgram("basic"), mesh, new Texture(engine.getFileSystem().getResourceStream(FileSystem.DATA_DIRECTORY.resolve("models").resolve("teleporter.jpg"))));
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

	public Client getEngine() {
		return engine;
	}

	public Program getProgram(String name) {
		final Program program = programs.get(name);
		if (program == null) {
			return loadProgram(name);
		}
		return program;
	}

	public Texture getTexture(String name) {
		final Texture texture = textures.get(name);
		if (texture == null) {
			// TODO: Load texture
		}
		return texture;
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
	public void dispose() {
		this.programs.values().stream().forEach((program) -> {
			program.dispose();
		});
		this.nodes.stream().forEach((node) -> {
			node.dispose();
		});
	}

	public void execute() {
		// Clear the screen
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Get the delta from the scheduler
		final float delta = getEngine().getScheduler().getDelta();

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

	private Program loadProgram(String name) {
		// Create a new program
		final Program program = new Program();

		// Create an input stream for the vertex shader
		try (final InputStream vertexStream = engine.getFileSystem().getResourceStream(shaderDirectory.resolve(name + VERTEX_SHADER_EXTENSION))) {
			if (vertexStream == null) {
				throw new IllegalStateException("Unable to load program: " + name);
			}
			// Attach the vertex shader
			program.attachShader(vertexStream, GL_VERTEX_SHADER);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Create an input stream for the fragment shader
		try (final InputStream fragmentStream = engine.getFileSystem().getResourceStream(shaderDirectory.resolve(name + FRAGMENT_SHADER_EXTENSION))) {
			if (fragmentStream == null) {
				throw new IllegalStateException("Unable to load program: " + name);
			}
			// Attach the fragment shader
			program.attachShader(fragmentStream, GL_FRAGMENT_SHADER);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Link the program
		program.link();

		// Add the program to the map
		this.programs.put(name, program);

		// Return the loaded program
		return program;
	}
}
