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
package me.thehutch.fusion.engine.scene;

import me.thehutch.fusion.api.maths.Matrix4;
import me.thehutch.fusion.api.maths.Vector4;
import me.thehutch.fusion.api.scene.IModel;
import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.render.Program;
import me.thehutch.fusion.engine.render.Texture;
import me.thehutch.fusion.engine.render.VertexArrayObject;

public class Model extends SceneNode implements IModel {
	private final Material material;
	private final ModelData data;
	private Vector4 scale;

	protected Model(ModelData data, Material material) {
		this.data = data;
		this.material = material;
		this.scale = Vector4.ONE;
	}

	public Material getMaterial() {
		return material;
	}

	@Override
	public void render(Program program) {
		// Bind the texture
		this.data.texture.bind(0);
		program.setUniform("material", 0);

		final Matrix4 modelMatrix = Matrix4.createScale(getScale()).rotate(getRotation()).translate(getPosition());
		// Set the model matrix
		program.setUniform("modelMatrix", modelMatrix);
		program.setUniform("normalMatrix", modelMatrix.invert().transpose().toMatrix3());
		// Draw the mesh
		this.data.mesh.draw();
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Vector4 getScale() {
		return scale;
	}

	@Override
	public void setScale(Vector4 scale) {
		this.scale = scale;
	}

	@Override
	public void scaleX(float scale) {
		scale(scale, 1.0f, 1.0f);
	}

	@Override
	public void scaleY(float scale) {
		scale(scale, 1.0f, 1.0f);
	}

	@Override
	public void scaleZ(float scale) {
		scale(scale, 1.0f, 1.0f);
	}

	@Override
	public void scale(float scale) {
		scale(scale, scale, scale);
	}

	@Override
	public void scale(float scaleX, float scaleY, float scaleZ) {
		this.scale = scale.mul(scaleX, scaleY, scaleZ, 1.0f);
	}

	public static class ModelData implements Disposable {
		public VertexArrayObject mesh;
		public Texture texture;

		@Override
		public void dispose() {
			// Dispose the mesh
			this.mesh.dispose();
			// Dispose the texture
			this.texture.dispose();
		}
	}
}
