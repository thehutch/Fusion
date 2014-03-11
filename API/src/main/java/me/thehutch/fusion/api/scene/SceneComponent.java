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
/*
 * This file is part of FusionAPI.
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

import me.thehutch.fusion.api.maths.Transform;

/**
 * @author thehutch
 */
public abstract class SceneComponent {
	private final Transform transform = new Transform();
	private SceneNode parent;

	/**
	 * Used to render the component.
	 */
	public abstract void render();

	/**
	 * Used to update the component.
	 *
	 * @param delta The time in milliseconds since the last update
	 */
	public abstract void update(float delta);

	/**
	 * Gets the parent scene node which this component is attached to.
	 *
	 * @return The parent scene node
	 */
	public SceneNode getParent() {
		return parent;
	}

	/**
	 * Gets the transform of this component.
	 *
	 * @return The transform
	 */
	public Transform getTransform() {
		return transform;
	}
}
