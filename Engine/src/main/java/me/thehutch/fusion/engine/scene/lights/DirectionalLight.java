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

import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.scene.lights.IDirectionalLight;
import me.thehutch.fusion.engine.render.Program;

public class DirectionalLight extends Light implements IDirectionalLight {
	private Vector3 direction;

	public DirectionalLight(Program program, Vector3 colour, Vector3 direction) {
		super(program, Vector3.ZERO, colour);
		this.direction = direction;
	}

	@Override
	public Vector3 getDirection() {
		return direction;
	}

	@Override
	public void setDirection(Vector3 direction) {
		this.direction = direction;
	}

	@Override
	public void uploadUniforms() {
		// Set the light uniforms
		this.program.setUniform("light.colour", getColour());
		this.program.setUniform("light.direction", direction);
	}
}
