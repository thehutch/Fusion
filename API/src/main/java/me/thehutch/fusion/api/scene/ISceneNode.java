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

import me.thehutch.fusion.api.util.Disposable;

/**
 * @author thehutch
 */
public interface ISceneNode extends Disposable {
	/**
	 * Renders the components and then the children nodes.
	 */
	public void render();

	/**
	 * Updates the components and then the children nodes.
	 *
	 * @param delta The time in milliseconds since the last update
	 */
	public void update(float delta);
}
