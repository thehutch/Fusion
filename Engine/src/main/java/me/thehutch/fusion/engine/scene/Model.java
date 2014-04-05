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

import com.flowpowered.math.imaginary.Quaternionf;
import com.flowpowered.math.matrix.Matrix4f;
import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector4f;
import me.thehutch.fusion.api.scene.IModel;
import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.render.Program;
import me.thehutch.fusion.engine.render.Texture;
import me.thehutch.fusion.engine.render.VertexArray;

public class Model extends SceneNode implements IModel {
	private final ModelData data;
	private Vector4f scale;

	protected Model(ModelData data) {
		this.data = data;

		this.rotation = Quaternionf.IDENTITY;
		this.position = Vector3f.ZERO;
		this.scale = Vector4f.ONE;
	}

	@Override
	public void render(Program program) {
		// Bind the texture
		this.data.texture.bind(0);

		final Matrix4f modelMatrix = Matrix4f.createScaling(scale).rotate(rotation).translate(position);
		// Set the model matrix
		program.setUniform("model", modelMatrix);

		// Set the material uniforms
		program.setUniform("materialShininess", 80.f);
		program.setUniform("materialSpecularColour", Vector3f.ONE);

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
	public Vector4f getScale() {
		return scale;
	}

	@Override
	public void setScale(Vector4f scale) {
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
		public VertexArray mesh;
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
