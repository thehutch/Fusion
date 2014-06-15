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

import me.thehutch.fusion.api.maths.Quaternion;
import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.scene.ISceneNode;
import me.thehutch.fusion.engine.render.Program;

/**
 * @author thehutch
 */
public abstract class SceneNode implements ISceneNode {
	protected Quaternion rotation;
	protected Vector3 position;

	public abstract void update(float delta);

	public abstract void render(Program program);

	@Override
	public Vector3 getPosition() {
		return position;
	}

	@Override
	public void setPosition(Vector3 position) {
		this.position = position;
	}

	@Override
	public Quaternion getRotation() {
		return rotation;
	}

	@Override
	public void setRotation(Quaternion rotation) {
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
		this.position = getPosition().add(dx, dy, dz);
	}

	@Override
	public void rotateX(float angle) {
		rotate(Quaternion.fromAxisAngleRad(Vector3.UNIT_X, angle));
	}

	@Override
	public void rotateY(float angle) {
		rotate(Quaternion.fromAxisAngleRad(Vector3.UNIT_Y, angle));
	}

	@Override
	public void rotateZ(float angle) {
		rotate(Quaternion.fromAxisAngleRad(Vector3.UNIT_Z, angle));
	}

	@Override
	public void rotate(Quaternion rotation) {
		this.rotation = rotation.normalise().mul(getRotation());
	}
}
