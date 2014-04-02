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
import me.thehutch.fusion.api.scene.Camera;
import me.thehutch.fusion.api.scene.IModel;
import me.thehutch.fusion.api.scene.ISceneNode;
import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.render.Program;
import me.thehutch.fusion.engine.render.Texture;
import me.thehutch.fusion.engine.render.VertexArray;

public class Model implements IModel, ISceneNode {
	private final ModelData data;
	private final Camera camera;
	private Quaternionf rotation;
	private Vector3f position;
	private Vector4f scale;

	Model(Camera camera, ModelData data) {
		this.camera = camera;
		this.data = data;

		this.rotation = Quaternionf.IDENTITY;
		this.position = Vector3f.ZERO;
		this.scale = Vector4f.ONE;
	}

	@Override
	public void dispose() {
		// Dispose the program
		this.data.program.dispose();
		// Dispose the mesh
		this.data.mesh.dispose();
	}

	@Override
	public void render() {
		// Use the program
		this.data.program.use();
		// Bind the texture
		this.data.texture.bind(0);

		// Set the matrix uniform
		this.data.program.setUniform("transform", camera.getTransformation().mul(Matrix4f.createScaling(scale).rotate(rotation).translate(position)));

		// Draw the mesh
		this.data.mesh.draw();
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public void setPosition(Vector3f position) {
		this.position = position;
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
	public Quaternionf getRotation() {
		return rotation;
	}

	@Override
	public void setRotation(Quaternionf rotation) {
		this.rotation = rotation;
	}

	@Override
	public void moveX(float dx) {
		move(dx, 0.0f, 0.0f);
	}

	@Override
	public void moveY(float dy) {
		move(0.0f, dy, 0.0f);
	}

	@Override
	public void moveZ(float dz) {
		move(0.0f, 0.0f, dz);
	}

	@Override
	public void move(float dx, float dy, float dz) {
		this.position = position.add(dx, dy, dz);
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

	@Override
	public void rotateX(float angle) {
		rotate(Quaternionf.fromAngleRadAxis(angle, Vector3f.UNIT_X));
	}

	@Override
	public void rotateY(float angle) {
		rotate(Quaternionf.fromAngleRadAxis(angle, Vector3f.UNIT_Y));
	}

	@Override
	public void rotateZ(float angle) {
		rotate(Quaternionf.fromAngleRadAxis(angle, Vector3f.UNIT_Z));
	}

	@Override
	public void rotate(Quaternionf rotation) {
		this.rotation = rotation.normalize().mul(getRotation());
	}

	static class ModelData implements Disposable {
		VertexArray mesh;
		Texture texture;
		Program program;

		@Override
		public void dispose() {
			// Dispose the mesh
			this.mesh.dispose();
			// Dispose the texture
			this.texture.dispose();
			// Dispose the program
			this.program.dispose();
		}
	}
}
