/*
 * This file is part of API.
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

import com.flowpowered.math.vector.Vector3f;
import me.thehutch.fusion.api.util.Disposable;

/**
 * @author thehutch
 */
public interface IScene extends Disposable {
	public Camera getCamera();

	public IModel createModel(String name, Vector3f position);

	public IModel createModel(String name, float x, float y, float z);
}
