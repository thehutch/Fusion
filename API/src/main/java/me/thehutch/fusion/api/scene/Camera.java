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

import me.thehutch.fusion.api.maths.Matrix4;
import me.thehutch.fusion.api.maths.Quaternion;
import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.maths.Vector4;

/**
 * @author thehutch
 */
public class Camera implements ISceneNode {
	private final Matrix4 projection;
	private Matrix4 inverseRotation;
	private Matrix4 viewMatrix;
	private Quaternion rotation;
	private Vector3 position;
	private boolean dirty;

	private Camera(Matrix4 projection) {
		this.projection = projection;
		this.inverseRotation = Matrix4.IDENTITY;
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
			this.inverseRotation = Matrix4.createRotation(rotation);
			final Matrix4 rotationMatrix = Matrix4.createRotation(rotation.invert());
			final Matrix4 positionMatrix = Matrix4.createTranslation(position.negate());
			this.viewMatrix = rotationMatrix.mul(positionMatrix);
			this.dirty = false;
		}
		return viewMatrix;
	}

	@Override
	public Vector3 getPosition() {
		return position;
	}

	@Override
	public void setPosition(Vector3 position) {
		this.position = position;
		this.dirty = true;
	}

	@Override
	public Quaternion getRotation() {
		return rotation;
	}

	@Override
	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
		this.dirty = true;
	}

	@Override
	public void moveX(float dx) {
		move(dx, 0.0f, 0.0f);
	}

	@Override
	public void moveLocalX(float dx) {
		this.position = getPosition().add(getRight().mul(dx));
		this.dirty = true;
	}

	@Override
	public void moveY(float dy) {
		move(0.0f, dy, 0.0f);
	}

	@Override
	public void moveLocalY(float dy) {
		this.position = getPosition().add(getUp().mul(dy));
		this.dirty = true;
	}

	@Override
	public void moveZ(float dz) {
		move(0.0f, 0.0f, dz);
	}

	@Override
	public void moveLocalZ(float dz) {
		this.position = getPosition().add(getForward().mul(dz));
		this.dirty = true;
	}

	@Override
	public void move(float dx, float dy, float dz) {
		this.position = getPosition().add(dx, dy, dz);
		this.dirty = true;
	}

	@Override
	public void rotateX(float angle) {
		rotate(Quaternion.fromAxisAngleRad(Vector3.UNIT_X, angle));
	}

	@Override
	public void rotateLocalX(float angle) {
		rotate(Quaternion.fromAxisAngleRad(toCamera(getRight()), angle));
	}

	@Override
	public void rotateY(float angle) {
		rotate(Quaternion.fromAxisAngleRad(Vector3.UNIT_Y, angle));
	}

	@Override
	public void rotateLocalY(float angle) {
		rotate(Quaternion.fromAxisAngleRad(toCamera(getUp()), angle));
	}

	@Override
	public void rotateZ(float angle) {
		rotate(Quaternion.fromAxisAngleRad(Vector3.UNIT_Z, angle));
	}

	@Override
	public void rotateLocalZ(float angle) {
		rotate(Quaternion.fromAxisAngleRad(toCamera(getForward()), angle));
	}

	@Override
	public void rotate(Quaternion rotation) {
		this.rotation = rotation.normalise().mul(getRotation());
		this.dirty = true;
	}

	@Override
	public void dispose() {
	}

	public Vector3 getForward() {
		return toCamera(Vector3.UNIT_Z);
	}

	public Vector3 getUp() {
		return toCamera(Vector3.UNIT_Y);
	}

	public Vector3 getRight() {
		return toCamera(Vector3.UNIT_X);
	}

	private Vector3 toCamera(Vector3 vec) {
		final Vector4 transform = inverseRotation.transform(vec.getX(), vec.getY(), vec.getZ(), 1.0f);
		return new Vector3(transform.getX(), transform.getY(), transform.getZ());
	}

	public static Camera createPerspective(float fov, float aspectRatio, float near, float far) {
		return new Camera(Matrix4.createPerspective(fov, aspectRatio, near, far));
	}

	public static Camera createOrthographic(float right, float left, float top, float bottom, float near, float far) {
		return new Camera(Matrix4.createOrthographic(right, left, top, bottom, near, far));
	}
}
