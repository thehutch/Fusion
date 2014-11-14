/*
 * This file is part of API, licensed under the Apache 2.0 License.
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
package me.thehutch.fusion.api.render;

import me.thehutch.fusion.api.maths.Matrix4;
import me.thehutch.fusion.api.maths.Quaternion;
import me.thehutch.fusion.api.maths.Vector3;

/**
 * @author thehutch
 */
public final class Camera {
	private final Matrix4 projection;
	private Matrix4 viewMatrix;
	private Quaternion rotation;
	private Vector3 position;
	private boolean dirty;

	private Camera(Matrix4 projection) {
		this.projection = projection;
		this.viewMatrix = Matrix4.IDENTITY;
		this.rotation = Quaternion.IDENTITY;
		this.position = Vector3.ZERO;
		this.dirty = true;
	}

	public Matrix4 getProjectionMatrix() {
		return projection;
	}

	public Matrix4 getViewMatrix() {
		if (dirty) {
			final Matrix4 rotationMatrix = Matrix4.newRotation(rotation.invert());
			final Matrix4 positionMatrix = Matrix4.newTranslation(position.negate());
			this.viewMatrix = rotationMatrix.mul(positionMatrix);
			this.dirty = false;
		}
		return viewMatrix;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
		this.dirty = true;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
		this.dirty = true;
	}

	public void moveX(float dx) {
		move(dx, 0.0f, 0.0f);
	}

	public void moveLocalX(float dx) {
		this.position = getPosition().add(getRight().mul(dx));
		this.dirty = true;
	}

	public void moveY(float dy) {
		move(0.0f, dy, 0.0f);
	}

	public void moveLocalY(float dy) {
		this.position = getPosition().add(getUp().mul(dy));
		this.dirty = true;
	}

	public void moveZ(float dz) {
		move(0.0f, 0.0f, dz);
	}

	public void moveLocalZ(float dz) {
		this.position = getPosition().add(getForward().mul(dz));
		this.dirty = true;
	}

	public void move(float dx, float dy, float dz) {
		this.position = getPosition().add(dx, dy, dz);
		this.dirty = true;
	}

	public void rotateX(float angle) {
		rotate(Quaternion.fromAxisAngleRad(Vector3.UNIT_X, angle));
	}

	public void rotateLocalX(float angle) {
		rotate(Quaternion.fromAxisAngleRad(getRight(), angle));
	}

	public void rotateY(float angle) {
		rotate(Quaternion.fromAxisAngleRad(Vector3.UNIT_Y, angle));
	}

	public void rotateLocalY(float angle) {
		rotate(Quaternion.fromAxisAngleRad(getUp(), angle));
	}

	public void rotateZ(float angle) {
		rotate(Quaternion.fromAxisAngleRad(Vector3.UNIT_Z, angle));
	}

	public void rotateLocalZ(float angle) {
		rotate(Quaternion.fromAxisAngleRad(getForward(), angle));
	}

	public void rotate(Quaternion rotation) {
		this.rotation = rotation.normalise().mul(getRotation());
		this.dirty = true;
	}

	public Vector3 getRight() {
		return Vector3.UNIT_X.rotate(rotation);
	}

	public Vector3 getUp() {
		return Vector3.UNIT_Y.rotate(rotation);
	}

	public Vector3 getForward() {
		return Vector3.UNIT_Z.negate().rotate(rotation);
	}

	public static Camera createPerspective(float fov, float aspectRatio, float near, float far) {
		return new Camera(Matrix4.newPerspective(fov, aspectRatio, near, far));
	}

	public static Camera createOrthographic(float right, float left, float top, float bottom, float near, float far) {
		return new Camera(Matrix4.newOrthographic(right, left, top, bottom, near, far));
	}
}
