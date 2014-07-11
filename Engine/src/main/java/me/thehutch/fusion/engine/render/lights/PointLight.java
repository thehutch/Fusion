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
import me.thehutch.fusion.api.render.lights.IPointLight;
import me.thehutch.fusion.engine.render.opengl.Program;

public class PointLight extends Light implements IPointLight {
	private float attenuation;

	public PointLight(Program program, Vector3 position, Vector3 colour, float attenuation) {
		super(program, position, colour);
		this.attenuation = attenuation;
	}

	@Override
	public float getAttenuation() {
		return attenuation;
	}

	@Override
	public void setAttenuation(float attenuation) {
		this.attenuation = attenuation;
	}

	@Override
	public void uploadUniforms() {
		// Set the light uniforms
		this.program.setUniform("light.attenuation", attenuation);
		super.uploadUniforms();
	}
}
