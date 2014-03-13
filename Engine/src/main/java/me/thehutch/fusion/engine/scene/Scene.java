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
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.io.InputStream;
import me.thehutch.fusion.api.scene.AbstractScene;
import me.thehutch.fusion.api.scene.Camera;
import me.thehutch.fusion.api.scene.SceneNode;
import me.thehutch.fusion.engine.Client;
import me.thehutch.fusion.engine.render.Program;
import me.thehutch.fusion.engine.render.Shader;
import me.thehutch.fusion.engine.render.Shader.ShaderType;
import me.thehutch.fusion.engine.render.VertexData;
import me.thehutch.fusion.engine.util.WavefrontOBJLoader;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class Scene extends AbstractScene implements Runnable {
	private static final String VERTEX_SHADER_EXTENSION = ".vs";
	private static final String FRAGMENT_SHADER_EXTENSION = ".fs";
	private final TMap<String, Program> programs = new THashMap<>();
	private final String shaderDirectory;
	private final Client engine;

	public Scene(Client engine, Camera camera, String shaderDirectory) {
		super(camera);
		this.shaderDirectory = shaderDirectory;
		this.engine = engine;
		// Create the window
		createWindow();
		//TODO: Load all programs from the directory
		// Create the vertex data
		final VertexData mesh = WavefrontOBJLoader.load(Client.class.getResourceAsStream("/models/bunny.obj"));

		// Create the model
		final Model bunny = new Model(getProgram("basic"), mesh);
		bunny.getTransform().scale(5.0f);

		// Add the model to the scene
		final SceneNode modelNode = createNode(bunny);
		addNode("models", modelNode);
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
	public void dispose() {
		// Dispose of the programs
		for (Program program : programs.values()) {
			program.dispose();
		}
	}

	@Override
	public void run() {
		// Clear the screen
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Update the scene
		update(getEngine().getScheduler().getDelta());
		// Render the scene
		render();

		// Update the display
		Display.update();
	}

	private void createWindow() {
		try {
			Display.setTitle("Fusion Engine | " + ENGINE_VERSION);
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setVSyncEnabled(true);
			Display.create(new PixelFormat(), new ContextAttribs(3, 2).withProfileCore(true).withDebug(true));
		} catch (LWJGLException ex) {
			ex.printStackTrace();
		}
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
