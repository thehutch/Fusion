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

import me.thehutch.fusion.api.component.Component;
import me.thehutch.fusion.api.maths.Matrix4;
import me.thehutch.fusion.api.maths.Quaternion;
import me.thehutch.fusion.api.maths.Vector3;

/**
 * @author thehutch
 */
public class Camera implements Component {
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
			final Matrix4 rotationMatrix = Matrix4.createRotation(rotation.invert());
			final Matrix4 positionMatrix = Matrix4.createTranslation(position.negate());
			this.viewMatrix = rotationMatrix.mul(positionMatrix);
			this.dirty = false;
		}
		return viewMatrix;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
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

	public static Camera createPerspective(float fov, float aspectRatio, float near, float far) {
		return new Camera(Matrix4.createPerspective(fov, aspectRatio, near, far));
	}

	public static Camera createOrthographic(float right, float left, float top, float bottom, float near, float far) {
		return new Camera(Matrix4.createOrthographic(right, left, top, bottom, near, far));
	}
}
