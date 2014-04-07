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

import com.flowpowered.math.vector.Vector3f;
import me.thehutch.fusion.engine.render.Program;

public class PointLight extends Light {
	private final float attenuation;
	private Vector3f position;
	private Vector3f colour;

	public PointLight(Program program, Vector3f position, Vector3f colour, float attenuation) {
		super(program);
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
	}

	public float getAttenuation() {
		return attenuation;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}

	@Override
	public void uploadUniforms() {
		// Bind the program before bind
		this.program.bind();
		// Set the uniforms
		this.program.setUniform("light.position", position);
		this.program.setUniform("light.colour", colour);
		this.program.setUniform("light.attenuation", attenuation);
	}
}
