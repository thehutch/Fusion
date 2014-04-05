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
import com.flowpowered.math.vector.Vector3f;
import me.thehutch.fusion.api.scene.ISceneNode;
import me.thehutch.fusion.engine.render.Program;

/**
 * @author thehutch
 */
public abstract class SceneNode implements ISceneNode {
	protected Quaternionf rotation;
	protected Vector3f position;

	public abstract void update(float delta);

	public abstract void render(Program program);

	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public void setPosition(Vector3f position) {
		this.position = position;
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
	public void moveLocalX(float dx) {
		//this.position = getPosition().add(getRight().mul(dx));
	}

	@Override
	public void moveY(float dy) {
		move(0.0f, dy, 0.0f);
	}

	@Override
	public void moveLocalY(float dy) {
		//this.position = getPosition().add(getUp().mul(dy));
	}

	@Override
	public void moveZ(float dz) {
		move(0.0f, 0.0f, dz);
	}

	@Override
	public void moveLocalZ(float dz) {
		//this.position = getPosition().add(getForward().mul(dz));
	}

	@Override
	public void move(float dx, float dy, float dz) {
		this.position = getPosition().add(dx, dy, dz);
	}

	@Override
	public void rotateX(float angle) {
		rotate(Quaternionf.fromAngleRadAxis(angle, Vector3f.UNIT_X));
	}

	@Override
	public void rotateLocalX(float angle) {
		//rotate(Quaternionf.fromAngleRadAxis(angle, toCamera(getRight())));
	}

	@Override
	public void rotateY(float angle) {
		rotate(Quaternionf.fromAngleRadAxis(angle, Vector3f.UNIT_Y));
	}

	@Override
	public void rotateLocalY(float angle) {
		//rotate(Quaternionf.fromAngleRadAxis(angle, toCamera(getUp())));
	}

	@Override
	public void rotateZ(float angle) {
		rotate(Quaternionf.fromAngleRadAxis(angle, Vector3f.UNIT_Z));
	}

	@Override
	public void rotateLocalZ(float angle) {
		//rotate(Quaternionf.fromAngleRadAxis(angle, toCamera(getForward())));
	}

	@Override
	public void rotate(Quaternionf rotation) {
		this.rotation = rotation.normalize().mul(getRotation());
	}
}
