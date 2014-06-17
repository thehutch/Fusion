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
package me.thehutch.fusion.api.component;

import java.util.BitSet;
import me.thehutch.fusion.api.util.container.Bag;

/**
 * @author thehutch
 */
public final class ComponentManager extends Manager {
	private final Bag<Bag<Component>> componentsByType = new Bag<>();
	private final Bag<Entity> deleted = new Bag<>();

	@Override
	public void added(Entity e) {
	}

	@Override
	public void deleted(Entity e) {
		this.deleted.add(e);
	}

	@Override
	public void changed(Entity e) {
	}

	@Override
	public void enabled(Entity e) {
	}

	@Override
	public void disabled(Entity e) {
	}

	@Override
	protected void initialise() {
	}

	protected Component getComponent(Entity e, ComponentType type) {
		final Bag<Component> components = componentsByType.get(type.getIndex());
		if (components != null) {
			return components.get(e.getId());
		}
		return null;
	}

	protected Bag<Component> getComponentsByType(ComponentType type) {
		Bag<Component> components = componentsByType.get(type.getIndex());
		if (components == null) {
			components = new Bag<>();
			this.componentsByType.set(type.getIndex(), components);
		}
		return components;
	}

	protected Bag<Component> getComponentsFor(Entity e, Bag<Component> fillbag) {
		final BitSet componentBits = e.getComponentBits();
		for (int i = componentBits.nextSetBit(0); i >= 0; i = componentBits.nextSetBit(i + 1)) {
			fillbag.add(componentsByType.get(i).get(e.getId()));
		}
		return fillbag;
	}

	protected void addComponent(Entity e, ComponentType type, Component component) {
		this.componentsByType.ensureCapacity(type.getIndex());

		Bag<Component> components = componentsByType.get(type.getIndex());
		if (components == null) {
			components = new Bag<>();
			this.componentsByType.set(type.getIndex(), components);
		}

		components.set(e.getId(), component);
		e.getComponentBits().set(type.getIndex());
	}

	protected void removeComponent(Entity e, ComponentType type) {
		final BitSet componentBits = e.getComponentBits();
		final int index = type.getIndex();
		if (componentBits.get(index)) {
			this.componentsByType.get(index).set(e.getId(), null);
			componentBits.clear(index);
		}
	}

	protected void clean() {
		if (!deleted.isEmpty()) {
			for (int i = 0; i < deleted.size(); ++i) {
				final Entity e = deleted.get(i);
				final BitSet componetBits = e.getComponentBits();
				for (int j = componetBits.nextSetBit(0); j >= 0; j = componetBits.nextSetBit(j + 1)) {
					this.componentsByType.get(j).set(e.getId(), null);
				}
				componetBits.clear();
			}
			this.deleted.clear();
		}
	}
}
