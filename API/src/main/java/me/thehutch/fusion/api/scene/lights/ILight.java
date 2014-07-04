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
package me.thehutch.fusion.api.scene.lights;

import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.scene.ISceneNode;

/**
 * @author thehutch
 */
public interface ILight extends ISceneNode {
	/**
	 * Gets the colour of the light in RGB format.
	 *
	 * @return The colour of the light
	 */
	public Vector3 getColour();

	/**
	 * Sets the colour of the light in the format RGB.
	 *
	 * @param colour The new colour of the light
	 */
	public void setColour(Vector3 colour);
}
