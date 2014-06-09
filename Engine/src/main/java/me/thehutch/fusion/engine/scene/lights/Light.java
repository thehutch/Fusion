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
package me.thehutch.fusion.engine.scene.lights;

import me.thehutch.fusion.api.maths.Quaternion;
import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.scene.lights.ILight;
import me.thehutch.fusion.engine.render.Program;

public abstract class Light implements ILight {
	protected final Program program;
	private Vector3 position;
	private Vector3 colour;

	public Light(Program program, Vector3 position, Vector3 colour) {
		this.program = program;
		this.position = position;
		this.colour = colour;
	}

	public Program getProgram() {
		return program;
	}

	public void uploadUniforms() {
		// Set the light uniforms
		this.program.setUniform("light.position", position);
		this.program.setUniform("light.colour", colour);
	}

	@Override
	public Vector3 getColour() {
		return colour;
	}

	@Override
	public void setColour(Vector3 colour) {
		this.colour = colour;
	}

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
		throw new UnsupportedOperationException("getRotation in class Light is not supported yet.");
	}

	@Override
	public void setRotation(Quaternion rotation) {
		throw new UnsupportedOperationException("setRotation in class Light is not supported yet.");
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
	public void rotateX(float angle) {
		throw new UnsupportedOperationException("rotateX in class Light is not supported yet.");
	}

	@Override
	public void rotateY(float angle) {
		throw new UnsupportedOperationException("rotateY in class Light is not supported yet.");
	}

	@Override
	public void rotateZ(float angle) {
		throw new UnsupportedOperationException("rotateZ in class Light is not supported yet.");
	}

	@Override
	public void rotate(Quaternion rotation) {
		throw new UnsupportedOperationException("rotate in class Light is not supported yet.");
	}

	@Override
	public void dispose() {
		throw new UnsupportedOperationException("dispose in class Light is not supported yet.");
	}
}
