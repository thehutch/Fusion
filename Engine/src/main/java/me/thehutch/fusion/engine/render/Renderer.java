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
package me.thehutch.fusion.engine.render;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import java.nio.file.Path;
import me.thehutch.fusion.api.component.Aspect;
import me.thehutch.fusion.api.component.ComponentMapper;
import me.thehutch.fusion.api.component.Entity;
import me.thehutch.fusion.api.component.EntityProcessor;
import me.thehutch.fusion.api.component.annotations.Mapper;
import me.thehutch.fusion.api.maths.Matrix4;
import me.thehutch.fusion.api.render.Camera;
import me.thehutch.fusion.api.util.container.ImmutableBag;
import me.thehutch.fusion.engine.Client;
import me.thehutch.fusion.engine.component.RenderComponent;
import me.thehutch.fusion.engine.component.TransformComponent;
import me.thehutch.fusion.engine.filesystem.FileSystem;
import me.thehutch.fusion.engine.render.lights.AmbientLight;
import me.thehutch.fusion.engine.render.opengl.Mesh;
import me.thehutch.fusion.engine.render.opengl.Program;
import me.thehutch.fusion.engine.render.opengl.Texture;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public final class Renderer extends EntityProcessor {
	public static final Path TEXTURE_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("textures");
	public static final Path PROGRAM_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("programs");
	public static final Path SHADER_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("shaders");
	public static final Path MODELS_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("models");
	public static final Path MESH_DIRECTORY = FileSystem.DATA_DIRECTORY.resolve("meshes");
	// Scene camera
	private final Camera camera;
	// Game engine
	private final Client engine;
	// Render component mapper
	@Mapper
	private ComponentMapper<RenderComponent> renderMapper;
	// Transform component mapper
	@Mapper
	private ComponentMapper<TransformComponent> transformMapper;
	// Ambient light
	private AmbientLight ambientLight;

	public Renderer(Client engine, Camera camera) {
		super(Aspect.getAspectFor(RenderComponent.class,
								  TransformComponent.class));
		this.camera = camera;
		this.engine = engine;
	}

	public Camera getCamera() {
		return camera;
	}

	@Override
	protected void initialise() {
		// Initialise the ambient light
		this.ambientLight = new AmbientLight(getProgram("ambient.fprg"), 0.125f);

		// Enable depth testing
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glEnable(GL_DEPTH_CLAMP);

		// Enable face culling
		GL11.glEnable(GL_CULL_FACE);
		GL11.glCullFace(GL_BACK);

		// Create an example model for testing
		final RenderComponent render = new RenderComponent(getMesh("ground.obj"), getTexture("ground.ftex"));
		final TransformComponent transform = new TransformComponent();

		final Entity e = engine.getComponentSystem().createEntity();
		e.addComponent(render);
		e.addComponent(transform);

		engine.getComponentSystem().addEntity(e);
	}

	@Override
	protected void begin() {
		// Clear the colour and depth buffers
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	@Override
	protected void end() {
		// Update the display
		Display.update();

		// Check for errors
		//RenderUtil.checkGLError();
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		// Calculate the camera matrix (projection * view)
		final Matrix4 cameraMatrix = camera.getProjectionMatrix().mul(camera.getViewMatrix());

		//
		// Render the first pass of the scene for the ambient light
		//
		this.ambientLight.uploadUniforms();

		final Program ambientProgram = ambientLight.getProgram();
		ambientProgram.setUniform("cameraMatrix", cameraMatrix);

		// Render the models
		entities.forEach((Entity e) -> {
			// Get the render and transform components
			final RenderComponent render = renderMapper.get(e);
			final TransformComponent transform = transformMapper.get(e);

			// Render the entity
			render(render, transform, ambientProgram);
		});
		// Unbind the program
		ambientProgram.unbind();

		// Enable blending
		GL11.glEnable(GL_BLEND);
		GL11.glBlendFunc(GL_ONE, GL_ONE);
		GL11.glDepthMask(false);
		GL11.glDepthFunc(GL_EQUAL);

		//
		// TODO: Perform the light render pass.
		//
		{
//		// Render each of the lights in the scene
//		this.lights.stream().forEach((Light light) -> {
//			final Program program = light.getProgram();
//			program.bind();
//			light.uploadUniforms();
//			// Set the camera matrix
//			program.setUniform("cameraMatrix", cameraMatrix);
//			// Set the position of the camera
//			program.setUniform("cameraPos", camera.getPosition());
//			// Render all the models
//			this.models.stream().forEach((Model model) -> {
//				model.getMaterial().uploadUniforms(program);
//				model.render(program);
//			});
//			program.unbind();
//		});
		}

		// Disable blending
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL_LESS);
		GL11.glDisable(GL_BLEND);
	}

	@Override
	protected boolean checkProcessing() {
		return !getActives().isEmpty();
	}

	@Override
	protected void inserted(Entity e) {
	}

	@Override
	protected void removed(Entity e) {
	}

	private Mesh getMesh(String name) {
		return engine.getFileSystem().getResource(MESH_DIRECTORY.resolve(name));
	}

	private Texture getTexture(String name) {
		return engine.getFileSystem().getResource(TEXTURE_DIRECTORY.resolve(name));
	}

	private Program getProgram(String name) {
		return engine.getFileSystem().getResource(PROGRAM_DIRECTORY.resolve(name));
	}

	private void render(RenderComponent render, TransformComponent transform, Program program) {
		// Bind the texture
		render.getTexture().bind(-1);

		// Set the material uniform
		program.setUniform("material", 0);

		// Set the model and normal matrix uniforms
		final Matrix4 modelMatrix = Matrix4.createScale(transform.getScale()).rotate(transform.getRotation()).translate(transform.getPosition());
		program.setUniform("modelMatrix", modelMatrix);
		program.setUniform("normalMatrix", modelMatrix.invert().transpose().toMatrix3());

		// Draw the mesh
		render.getMesh().draw();
	}
}
