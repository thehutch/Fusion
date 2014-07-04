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
package me.thehutch.fusion.api.scene;

import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.scene.lights.ILight;
import me.thehutch.fusion.api.scene.lights.IPointLight;
import me.thehutch.fusion.api.scene.lights.ISpotLight;
import me.thehutch.fusion.api.util.Disposable;

/**
 * @author thehutch
 */
public interface IScene extends Disposable {
	public Camera getCamera();

	public IModel createModel(String name, Vector3 position);

	public IModel createModel(String name, float x, float y, float z);

	public void removeModel(IModel model);

	public float getAmbientLevel();

	public void setAmbientLevel(float ambientLevel);

	public IPointLight createPointLight(Vector3 position, Vector3 colour, float attenuation);

	public ISpotLight createSpotLight(Vector3 position, Vector3 colour, Vector3 direction, float attenutation, float angle);

	public void removeLight(ILight light);
}
