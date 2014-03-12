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

import me.thehutch.fusion.api.maths.Transform;
import me.thehutch.fusion.api.maths.matrix.Matrix4;

/**
 * @author thehutch
 */
public class Camera {
	private final Transform transform = new Transform();
	private final Matrix4 projection;

	private Camera(Matrix4 projection) {
		this.projection = projection;
	}

	public Transform getTransform() {
		return transform;
	}

	public Matrix4 getProjection() {
		return projection;
	}

	public Matrix4 getView() {
		final Matrix4 rotation = getTransform().getRotation().conjugate().toRotationMatrix();
		final Matrix4 translation = Matrix4.createTranslation(getTransform().getPosition().negate());
		return translation.mul(rotation).mul(projection);
	}

	public static Camera createPerspective(float fov, float aspectRatio, float near, float far) {
		return new Camera(Matrix4.createPerspective(fov, aspectRatio, near, far));
	}

	public static Camera createOrthographic(float right, float left, float top, float bottom, float near, float far) {
		return new Camera(Matrix4.createOrthographic(right, left, top, bottom, near, far));
	}
}
