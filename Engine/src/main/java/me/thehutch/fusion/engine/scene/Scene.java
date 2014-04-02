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

import com.flowpowered.math.vector.Vector3f;
import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;
import me.thehutch.fusion.api.scene.Camera;
import me.thehutch.fusion.api.scene.IScene;
import me.thehutch.fusion.api.scene.ISceneNode;
import me.thehutch.fusion.engine.Client;
import me.thehutch.fusion.engine.filesystem.FileSystem;
import me.thehutch.fusion.engine.render.Program;
import me.thehutch.fusion.engine.render.Texture;
import me.thehutch.fusion.engine.scene.Model.ModelData;
import me.thehutch.fusion.engine.util.WavefrontOBJLoader;
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
	private final TMap<String, ModelData> models = new THashMap<>();
	private final Collection<ISceneNode> nodes = new ArrayList<>();
	private final Camera camera;
	private final Client engine;

	public Scene(Client engine, Camera camera) {
		this.camera = camera;
		this.engine = engine;
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
		ModelData modelData = models.get(name);
		if (modelData == null) {
			// Load the model data from the model file
			modelData = loadModelData(MODELS_DIRECTORY.resolve(name));
			// Cache the model data
			this.models.put(name, modelData);
		}
		final Model model = new Model(camera, modelData);
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
		this.models.values().stream().forEach((program) -> {
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

	private static ModelData loadModelData(Path path) {
		try {
			final ModelData data = new ModelData();
			final Program program = new Program();
			Files.lines(path).forEach((String line) -> {
				try {
					if (line.startsWith("vertex_shader")) {
						// Load the source and attach the shader to the program
						final StringBuilder source = new StringBuilder();
						concatLines(source, Files.lines(getPath(line)));
						// Attach the vertex shader
						program.attachShader(source, GL_VERTEX_SHADER);
					} else if (line.startsWith("fragment_shader")) {
						// Load the source and attach the shader to the program
						final StringBuilder source = new StringBuilder();
						concatLines(source, Files.lines(getPath(line)));
						// Attach the fragment shader
						program.attachShader(source, GL_FRAGMENT_SHADER);
					} else if (line.startsWith("mesh")) {
						data.mesh = WavefrontOBJLoader.load(getPath(line));
					} else if (line.startsWith("texture")) {
						data.texture = new Texture(Files.newInputStream(getPath(line)));
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			});
			// Link the program
			program.link();

			// Set the model data program
			data.program = program;
			return data;
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load model file: " + path, ex);
		}
	}

	private static void concatLines(StringBuilder source, Stream<String> lines) {
		lines.forEachOrdered((String line) -> {
			source.append(line).append('\n');
		});
		lines.close();
	}

	private static Path getPath(String line) {
		return FileSystem.DATA_DIRECTORY.resolve(line.substring(line.indexOf(':', 0) + 1).trim());
	}
}
