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

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import me.thehutch.fusion.api.graphics.ITexture;
import me.thehutch.fusion.api.scene.Camera;
import me.thehutch.fusion.api.scene.IScene;
import me.thehutch.fusion.api.scene.ISceneNode;
import me.thehutch.fusion.engine.Client;
import me.thehutch.fusion.engine.Engine;
import me.thehutch.fusion.engine.render.Program;
import me.thehutch.fusion.engine.render.Shader;
import me.thehutch.fusion.engine.render.Shader.ShaderType;
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
	private final String shaderDirectory;
	private final Camera camera;
	private final Client engine;

	public Scene(Client engine, Camera camera, String shaderDirectory) {
		this.shaderDirectory = shaderDirectory;
		this.camera = camera;
		this.engine = engine;
		// Create the window
		createWindow();
		//TODO: Load all programs from the directory
		// Create the vertex data
		final VertexData mesh = WavefrontOBJLoader.load(Client.class.getResourceAsStream("/models/teleporter.obj"));

		// Create the model
		final Model model = new Model(this, getProgram("basic"), mesh, new Texture(Engine.class.getResourceAsStream("/models/teleporter.jpg"), ITexture.Format.RGB));
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
			Display.create(new PixelFormat(), new ContextAttribs(3, 0).withForwardCompatible(true));
		} catch (LWJGLException ex) {
			ex.printStackTrace();
		}
		GL11.glClearColor(0.0f, 0.50f, 0.75f, 1.0f);
	}

	private Program loadProgram(String name) {
		// Get the file path of the shader (Excluding file extension)
		final String shaderFilePath = shaderDirectory + name;
		// Create an input stream for the vertex shader
		InputStream inputStream = Client.class.getResourceAsStream(shaderFilePath + VERTEX_SHADER_EXTENSION);
		if (inputStream == null) {
			throw new IllegalStateException("Unable to load program: " + name);
		}
		final Shader vertexShader = new Shader(inputStream, ShaderType.VERTEX);
		// Create an input stream for the fragment shader
		inputStream = Client.class.getResourceAsStream(shaderFilePath + FRAGMENT_SHADER_EXTENSION);
		if (inputStream == null) {
			throw new IllegalStateException("Unable to load program: " + name);
		}
		final Shader fragmentShader = new Shader(inputStream, ShaderType.FRAGMENT);
		// Create a new program
		final Program program = new Program();
		// Attach the shaders
		program.attachShader(vertexShader);
		program.attachShader(fragmentShader);
		// Link the program
		program.link();
		// Add the program to the map
		this.programs.put(name, program);
		return program;
	}
}
