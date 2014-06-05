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
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import me.thehutch.fusion.api.event.EventPriority;
import me.thehutch.fusion.api.input.keyboard.Key;
import me.thehutch.fusion.api.input.keyboard.KeyboardEvent;
import me.thehutch.fusion.api.maths.MathsHelper;
import me.thehutch.fusion.api.maths.Matrix4;
import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.scene.Camera;
import me.thehutch.fusion.api.scene.IModel;
import me.thehutch.fusion.api.scene.IScene;
import me.thehutch.fusion.api.scene.lights.ILight;
import me.thehutch.fusion.engine.Client;
import me.thehutch.fusion.engine.filesystem.FileSystem;
import me.thehutch.fusion.engine.filesystem.loaders.ModelLoader;
import me.thehutch.fusion.engine.filesystem.loaders.ProgramLoader;
import me.thehutch.fusion.engine.filesystem.loaders.TextureLoader;
import me.thehutch.fusion.engine.scene.lights.AmbientLight;
import me.thehutch.fusion.engine.scene.lights.DirectionalLight;
import me.thehutch.fusion.engine.scene.lights.Light;
import me.thehutch.fusion.engine.scene.lights.PointLight;
import me.thehutch.fusion.engine.scene.lights.SpotLight;
import me.thehutch.fusion.engine.util.RenderUtil;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.PixelFormat;

public final class Scene implements IScene {
	public static final Path TEXTURE_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("textures");
	public static final Path PROGRAM_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("programs");
	public static final Path SHADER_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("shaders");
	public static final Path MODELS_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("models");
	public static final Path MESH_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("meshes");
	private final Collection<Model> models = new ArrayList<>();
	private final Collection<Light> lights = new ArrayList<>();
	private final AmbientLight ambientLight;
	private final Camera camera;
	private final Client engine;

	public Scene(Client engine, Camera camera) {
		this.camera = camera;
		camera.setPosition(new Vector3(0.0f, 0.25f, 1.0f));
		this.engine = engine;
		// Register the model loader
		engine.getFileSystem().registerLoader(new ModelLoader(engine.getFileSystem()), "fmdl");
		// Register the texture loader
		engine.getFileSystem().registerLoader(new TextureLoader(), "jpg", "png");
		// Register the program loader
		engine.getFileSystem().registerLoader(new ProgramLoader(), "fprg");

		// Create the window
		createWindow();

		// Create the room model
		createModel("ground.fmdl", Vector3.ZERO);

		// Set the ambient light
		this.ambientLight = new AmbientLight(engine.getFileSystem().getResource(PROGRAM_DIRECTORY.resolve("ambient.fprg")), 0.125f);

		// Create the directional light (sun)
		final Vector3 lightDirection = new Vector3(0.0f, 2.0f, 2.0f);
		createDirectionalLight(Vector3.ONE, lightDirection);

		// Enable depth testing
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glEnable(GL_DEPTH_CLAMP);

		// Enable face culling
		GL11.glEnable(GL_CULL_FACE);
		GL11.glCullFace(GL_BACK);

		// Check for errors
		RenderUtil.checkGLError();

		engine.getEventManager().registerEvent((KeyboardEvent event) -> {
			if (event.getKeycode() == Key.KEY_L && event.getState() && !event.isRepeat()) {
				createPointLight(camera.getPosition(), Vector3.ONE, 0.75f);
			}
		}, KeyboardEvent.class, EventPriority.HIGH, true);
	}

	@Override
	public Camera getCamera() {
		return camera;
	}

	@Override
	public Model createModel(String name, Vector3 position) {
		final Model model = new Model(engine.getFileSystem().getResource(MODELS_DIRECTORY.resolve(name)), new Material(50.0f));
		model.setPosition(position);
		this.models.add(model);
		return model;
	}

	@Override
	public Model createModel(String name, float x, float y, float z) {
		return createModel(name, new Vector3(x, y, z));
	}

	@Override
	public void removeModel(IModel model) {
		this.models.remove((Model) model);
		model.dispose();
	}

	@Override
	public float getAmbientLevel() {
		return ambientLight.getAmbientLevel();
	}

	@Override
	public void setAmbientLevel(float ambientLevel) {
		this.ambientLight.setAmbientLevel(ambientLevel);
	}

	@Override
	public PointLight createPointLight(Vector3 position, Vector3 colour, float attenuation) {
		final PointLight light = new PointLight(engine.getFileSystem().getResource(PROGRAM_DIRECTORY.resolve("point.fprg")), position, colour, attenuation);
		this.lights.add(light);
		return light;
	}

	@Override
	public SpotLight createSpotLight(Vector3 position, Vector3 colour, Vector3 direction, float attenutation, float angle) {
		final SpotLight light = new SpotLight(engine.getFileSystem().getResource(PROGRAM_DIRECTORY.resolve("spot.fprg")), direction, position, colour, attenutation, MathsHelper.toRadians(angle));
		this.lights.add(light);
		return light;
	}

	public DirectionalLight createDirectionalLight(Vector3 colour, Vector3 direction) {
		final DirectionalLight light = new DirectionalLight(engine.getFileSystem().getResource(PROGRAM_DIRECTORY.resolve("directional.fprg")), colour, direction);
		this.lights.add(light);
		return light;
	}

	@Override
	public void removeLight(ILight light) {
		this.lights.remove((Light) light);
		light.dispose();
	}

	@Override
	public void dispose() {
		this.models.stream().forEach((node) -> {
			node.dispose();
		});
	}

	public void execute() {
		// Clear the colour and depth buffers
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Calculate the camera matrix (projection * view)
		final Matrix4 cameraMatrix = camera.getProjectionMatrix().mul(camera.getViewMatrix());

		// Render the first pass of the scene for the ambient light
		this.ambientLight.getProgram().bind();
		this.ambientLight.uploadUniforms();
		this.ambientLight.getProgram().setUniform("cameraMatrix", cameraMatrix);
		this.models.stream().forEach((Model model) -> {
			model.render(ambientLight.getProgram());
		});
		this.ambientLight.getProgram().unbind();

		// Enable blending for the light render
		GL11.glEnable(GL_BLEND);
		GL14.glBlendEquation(GL_FUNC_ADD);
		GL11.glBlendFunc(GL_ONE, GL_ONE);
		GL11.glDepthMask(false);
		GL11.glDepthFunc(GL_EQUAL);

		// Render each of the lights in the scene
		this.lights.stream().forEach((Light light) -> {

			light.getProgram().bind();
			light.uploadUniforms();
			// Set the camera matrix
			light.getProgram().setUniform("cameraMatrix", cameraMatrix);
			// Set the position of the camera
			light.getProgram().setUniform("cameraPos", camera.getPosition());
			// Render all the models
			this.models.stream().forEach((Model model) -> {
				model.getMaterial().uploadUniforms(light.getProgram());
				model.render(light.getProgram());
			});
			light.getProgram().unbind();
		});

		// Disable blending
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL_LESS);
		GL11.glDisable(GL_BLEND);

		// Update the display
		Display.update();

		// Check for errors
		RenderUtil.checkGLError();
	}

	private void createWindow() {
		try {
			Display.setTitle("Fusion Engine | " + ENGINE_VERSION);
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setVSyncEnabled(true);
			Display.create(new PixelFormat(24, 8, 8, 0, 8), new ContextAttribs(3, 3).withProfileCore(true));
		} catch (LWJGLException ex) {
			ex.printStackTrace();
		}
		GL11.glClearColor(0.125f, 0.4f, 0.75f, 1.0f);
	}
}
