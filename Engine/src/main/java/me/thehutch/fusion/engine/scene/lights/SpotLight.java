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
package me.thehutch.fusion.engine.scene.lights;

import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.scene.lights.ISpotLight;
import me.thehutch.fusion.engine.render.Program;

/**
 * @author thehutch
 */
public class SpotLight extends PointLight implements ISpotLight {
	private Vector3 direction;
	private float angle;

	public SpotLight(Program program, Vector3 direction, Vector3 position, Vector3 colour, float attenuation, float angle) {
		super(program, position, colour, attenuation);
		this.direction = direction;
		this.angle = angle;
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
	public float getAngle() {
		return angle;
	}

	@Override
	public void setAngle(float angle) {
		this.angle = angle;
	}

	@Override
	public void uploadUniforms() {
		// Set the light uniforms
		this.program.setUniform("light.direction", direction);
		this.program.setUniform("light.cutoff", (float) Math.cos(angle));
		super.uploadUniforms();
	}
}
