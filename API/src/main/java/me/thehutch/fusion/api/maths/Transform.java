/*
 * This file is part of API.
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
package me.thehutch.fusion.api.maths;

import me.thehutch.fusion.api.maths.imaginary.Quaternion;
import me.thehutch.fusion.api.maths.matrix.Matrix4;
import me.thehutch.fusion.api.maths.vector.Vector3f;

public class Transform {
	private Quaternion rotation;
	private Vector3f position;
	private Vector3f scale;

	public Transform() {
		this(Quaternion.IDENTITY, Vector3f.ZERO, Vector3f.ONE);
	}

	public Transform(Quaternion rotation, Vector3f position, Vector3f scale) {
		this.rotation = rotation;
		this.position = position;
		this.scale = scale;
	}

	public Matrix4 getTransformation() {
		final Matrix4 translation = Matrix4.createTranslation(getPosition());
		final Matrix4 rotation = getRotation().toRotationMatrix();
		final Matrix4 scale = Matrix4.createScale(getScale());
		return translation.mul(rotation.mul(scale));
	}

	public Vector3f getPosition() {
		return position;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void moveX(float dx) {
		move(dx, 0.0f, 0.0f);
	}

	public void moveY(float dy) {
		move(0.0f, dy, 0.0f);
	}

	public void moveZ(float dz) {
		move(0.0f, 0.0f, dz);
	}

	public void move(float dx, float dy, float dz) {
		this.position = getPosition().add(dx, dy, dz);
	}

	public void rotateX(float angle) {
		rotate(Quaternion.fromAxisAngleRad(Vector3f.X_AXIS, angle));
	}

	public void rotateY(float angle) {
		rotate(Quaternion.fromAxisAngleRad(Vector3f.Y_AXIS, angle));
	}

	public void rotateZ(float angle) {
		rotate(Quaternion.fromAxisAngleRad(Vector3f.Z_AXIS, angle));
	}

	public void rotate(Quaternion rotation) {
		this.rotation = rotation.mul(getRotation()).normalise();
	}

	public void scaleX(float dx) {
		scale(dx, 1.0f, 1.0f);
	}

	public void scaleY(float dy) {
		scale(1.0f, dy, 1.0f);
	}

	public void scaleZ(float dz) {
		scale(1.0f, 1.0f, dz);
	}

	public void scale(float scale) {
		scale(scale, scale, scale);
	}

	public void scale(float dx, float dy, float dz) {
		this.scale = getScale().mul(dx, dy, dz);
	}
}
