/*
 * This file is part of API, licensed under the Apache 2.0 License.
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
package me.thehutch.fusion.api.render.lights;

/**
 * @author thehutch
 */
public interface ISpotLight extends IDirectionalLight, IPointLight {
	/**
	 * Gets the angle of the spotlight.
	 *
	 * @return The angle
	 */
	public float getAngle();

	/**
	 * Sets the angle of the spotlight.
	 *
	 * @param angle The angle
	 */
	public void setAngle(float angle);
}
