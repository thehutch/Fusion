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

import gnu.trove.set.hash.THashSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author thehutch
 */
public class SceneNode {
	private final Set<SceneComponent> components = new THashSet<>();
	private final Set<SceneNode> children = new THashSet<>();

	/**
	 * Adds a child node to this node.
	 *
	 * @param child The node to add
	 */
	public void addChild(SceneNode child) {
		// Add the child to this node
		this.children.add(child);
	}

	/**
	 * Gets all the children nodes attached to this node.
	 *
	 * @return An immutable collection of nodes
	 */
	public Collection<SceneNode> getChildren() {
		return Collections.unmodifiableSet(children);
	}

	/**
	 * Add a component to this node.
	 *
	 * @param component The component to add
	 */
	public void addComponent(SceneComponent component) {
		this.components.add(component);
	}

	/**
	 * Gets all the components attached to this node.
	 *
	 * @return An immutable collection of components
	 */
	public Collection<SceneComponent> getComponents() {
		return Collections.unmodifiableSet(components);
	}

	/**
	 * Renders the components and then the children nodes.
	 */
	void render() {
		// Render the components
		for (SceneComponent component : components) {
			component.render();
		}
		// Render the children node
		for (SceneNode child : children) {
			child.render();
		}
	}

	/**
	 * Updates the components and then the children nodes.
	 *
	 * @param delta The time in milliseconds since the last update
	 */
	void update(float delta) {
		// Update the components
		for (SceneComponent component : components) {
			component.update(delta);
		}
		// Update the children node
		for (SceneNode child : children) {
			child.update(delta);
		}
	}
}
