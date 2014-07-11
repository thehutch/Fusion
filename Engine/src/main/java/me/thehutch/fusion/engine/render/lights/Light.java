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
package me.thehutch.fusion.engine.render.lights;

import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.render.lights.ILight;
import me.thehutch.fusion.engine.render.opengl.Program;

public abstract class Light implements ILight {
	protected final Program program;
	private Vector3 position;
	private Vector3 colour;

	public Light(Program program, Vector3 position, Vector3 colour) {
		this.program = program;
		this.position = position;
		this.colour = colour;
	}

	public void uploadUniforms() {
		// Set the light uniforms
		this.program.setUniform("light.position", position);
		this.program.setUniform("light.colour", colour);
	}

	public final Program getProgram() {
		return program;
	}

	public final Vector3 getPosition() {
		return position;
	}

	public final void setPosition(Vector3 position) {
		this.position = position;
	}

	@Override
	public final Vector3 getColour() {
		return colour;
	}

	@Override
	public final void setColour(Vector3 colour) {
		this.colour = colour;
	}
}
