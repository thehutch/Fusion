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
package me.thehutch.fusion.engine.component;

import me.thehutch.fusion.api.component.IComponent;
import me.thehutch.fusion.api.maths.Quaternion;
import me.thehutch.fusion.api.maths.Vector3;

/**
 * @author thehutch
 */
public class TransformComponent implements IComponent {
	private Quaternion rotation;
	private Vector3 position;
	private Vector3 scale;

	public TransformComponent() {
		this(Quaternion.IDENTITY, Vector3.ZERO, Vector3.ONE);
	}

	public TransformComponent(Quaternion rotation, Vector3 position, Vector3 scale) {
		this.rotation = rotation;
		this.position = position;
		this.scale = scale;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public Vector3 getScale() {
		return scale;
	}

	public void moveX(float dx) {
		this.position.add(dx, 0.0f, 0.0f);
	}

	public void moveLocalX(float dx) {
		this.position.add(getRight().mul(dx));
	}

	public void moveY(float dy) {
		this.position.add(0.0f, dy, 0.0f);
	}

	public void moveLocalY(float dy) {
		this.position.add(getUp().mul(dy));
	}

	public void moveZ(float dz) {
		this.position.add(0.0f, 0.0f, dz);
	}

	public void moveLocalZ(float dz) {
		this.position.add(getForward().mul(dz));
	}

	public void move(float dx, float dy, float dz) {
		this.position.add(dx, dy, dz);
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
	}

	public void setScale(Vector3 scale) {
		this.scale = scale;
	}

	public void scaleX(float scale) {
		this.scale.mul(scale, 1.0f, 1.0f);
	}

	public void scaleY(float scale) {
		this.scale.mul(1.0f, scale, 1.0f);
	}

	public void scaleZ(float scale) {
		this.scale.mul(1.0f, 1.0f, scale);
	}

	public void scale(float scale) {
		this.scale.mul(scale, scale, scale);
	}

	public void scale(float scaleX, float scaleY, float scaleZ) {
		this.scale.mul(scaleX, scaleY, scaleZ);
	}

	public Vector3 getRight() {
		return Vector3.UNIT_X.rotate(rotation);
	}

	public Vector3 getUp() {
		return Vector3.UNIT_Y.rotate(rotation);
	}

	public Vector3 getForward() {
		return Vector3.UNIT_Z.rotate(rotation);
	}
}
