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

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import me.thehutch.fusion.api.util.Disposable;

/**
 * @author thehutch
 */
public abstract class AbstractScene implements Disposable {
	private final TMap<String, SceneNode> nodes = new THashMap<>();
	private final Camera camera;

	public AbstractScene(Camera camera) {
		this.camera = camera;
	}

	public SceneNode createNode(SceneComponent... components) {
		final SceneNode node = new SceneNode();
		for (int i = 0; i < components.length; ++i) {
			node.addComponent(components[i]);
		}
		return node;
	}

	public void addNode(String name, SceneNode node) {
		this.nodes.put(name, node);
	}

	public void removeNode(String name) {
		this.nodes.remove(name);
	}

	public SceneNode getNode(String name) {
		return nodes.get(name);
	}

	public Camera getCamera() {
		return camera;
	}

	protected void render() {
		for (SceneNode node : nodes.values()) {
			node.render();
		}
	}

	protected void update(float delta) {
		for (SceneNode node : nodes.values()) {
			node.update(delta);
		}
	}
}
