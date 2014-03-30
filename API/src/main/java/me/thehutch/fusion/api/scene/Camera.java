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
package me.thehutch.fusion.api.scene;

import com.flowpowered.math.imaginary.Quaternionf;
import com.flowpowered.math.matrix.Matrix4f;
import com.flowpowered.math.vector.Vector3f;

/**
 * @author thehutch
 */
public class Camera {
	private final Matrix4f projection;
	private Matrix4f inverseRotation;
	private Matrix4f viewMatrix;
	private Quaternionf rotation;
	private Vector3f position;
	private boolean dirty;

	private Camera(Matrix4f projection) {
		this.projection = projection;
		this.inverseRotation = Matrix4f.IDENTITY;
		this.viewMatrix = Matrix4f.IDENTITY;
		this.rotation = Quaternionf.IDENTITY;
		this.position = Vector3f.ZERO;
		this.dirty = true;
	}

	public Matrix4f getProjection() {
		return projection;
	}

	public Matrix4f getView() {
		if (dirty) {
			this.inverseRotation = Matrix4f.createRotation(rotation);
			final Matrix4f rotationMatrix = Matrix4f.createRotation(rotation.invert());
			final Matrix4f positionMatrix = Matrix4f.createTranslation(position.negate());
			this.viewMatrix = rotationMatrix.mul(positionMatrix);
			this.dirty = false;
		}
		return viewMatrix;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
		this.dirty = true;
	}

	public Quaternionf getRotation() {
		return rotation;
	}

	public void setRotation(Quaternionf rotation) {
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
		rotate(Quaternionf.fromAngleRadAxis(angle, Vector3f.UNIT_X));
	}

	public void rotateLocalX(float angle) {
		rotate(Quaternionf.fromAngleRadAxis(angle, toCamera(getRight())));
	}

	public void rotateY(float angle) {
		rotate(Quaternionf.fromAngleRadAxis(angle, Vector3f.UNIT_Y));
	}

	public void rotateLocalY(float angle) {
		rotate(Quaternionf.fromAngleRadAxis(angle, toCamera(getUp())));
	}

	public void rotateZ(float angle) {
		rotate(Quaternionf.fromAngleRadAxis(angle, Vector3f.UNIT_Z));
	}

	public void rotateLocalZ(float angle) {
		rotate(Quaternionf.fromAngleRadAxis(angle, toCamera(getForward())));
	}

	public void rotate(Quaternionf rotation) {
		this.rotation = rotation.normalize().mul(getRotation());
		this.dirty = true;
	}

	public Vector3f getForward() {
		return toCamera(Vector3f.FORWARD);
	}

	public Vector3f getUp() {
		return toCamera(Vector3f.UP);
	}

	public Vector3f getRight() {
		return toCamera(Vector3f.RIGHT);
	}

	private Vector3f toCamera(Vector3f vec) {
		return inverseRotation.transform(vec.getX(), vec.getY(), vec.getZ(), 1.0f).toVector3();
	}

	public static Camera createPerspective(float fov, float aspectRatio, float near, float far) {
		return new Camera(Matrix4f.createPerspective(fov, aspectRatio, near, far));
	}

	public static Camera createOrthographic(float right, float left, float top, float bottom, float near, float far) {
		return new Camera(Matrix4f.createOrthographic(right, left, top, bottom, near, far));
	}
}
