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
package me.thehutch.fusion.engine.component;

import java.util.BitSet;
import me.thehutch.fusion.api.component.ComponentType;
import me.thehutch.fusion.api.component.IComponent;
import me.thehutch.fusion.api.component.IEntity;
import me.thehutch.fusion.api.component.Manager;
import me.thehutch.fusion.api.util.container.Bag;

/**
 * @author thehutch
 */
public final class ComponentManager extends Manager {
	private final Bag<Bag<IComponent>> mComponentsByType = new Bag<>();
	private final Bag<IEntity> mDeleted = new Bag<>();

	/**
	 * Default constructor for {@link ComponentManager}.
	 */
	public ComponentManager() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void added(IEntity e) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleted(IEntity e) {
		mDeleted.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changed(IEntity e) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enabled(IEntity e) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disabled(IEntity e) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialise() {
	}

	/**
	 * Returns the component of the given type from the entity.
	 * <p>
	 * @param e    The entity to get the component from
	 * @param type The type of component
	 * <p>
	 * @return The component, null if not found
	 */
	public IComponent getComponent(IEntity e, ComponentType type) {
		final Bag<IComponent> components = mComponentsByType.get(type.getIndex());
		if (components != null) {
			return components.get(e.getId());
		}
		return null;
	}

	/**
	 * Returns a {@link Bag} of components mapped to the entities.
	 * <p>
	 * @param type The type of component
	 * <p>
	 * @return The bag of components
	 */
	public Bag<IComponent> getComponentsByType(ComponentType type) {
		final Bag<Bag<IComponent>> componentsByType = mComponentsByType;
		Bag<IComponent> components = componentsByType.get(type.getIndex());
		if (components == null) {
			components = new Bag<>();
			componentsByType.set(type.getIndex(), components);
		}
		return components;
	}

	/**
	 * Fills the given {@link Bag} with the components the given entity has.
	 * <p>
	 * @param e       The entity
	 * @param fillbag The bag to fill
	 */
	public void getComponentsFor(IEntity e, Bag<IComponent> fillbag) {
		final Bag<Bag<IComponent>> componentsByType = mComponentsByType;
		final BitSet componentBits = e.getComponentBits();
		for (int i = componentBits.nextSetBit(0); i >= 0; i = componentBits.nextSetBit(i + 1)) {
			fillbag.add(componentsByType.get(i).get(e.getId()));
		}
	}

	/**
	 * Adds a component to an entity.
	 * <p>
	 * @param e         The entity to add the component to
	 * @param type      The type of component to add
	 * @param component The component
	 */
	public void addComponent(IEntity e, ComponentType type, IComponent component) {
		final Bag<Bag<IComponent>> componentsByType = mComponentsByType;
		componentsByType.ensureCapacity(type.getIndex());

		Bag<IComponent> components = componentsByType.get(type.getIndex());
		if (components == null) {
			components = new Bag<>();
			componentsByType.set(type.getIndex(), components);
		}

		components.set(e.getId(), component);
		e.getComponentBits().set(type.getIndex());
	}

	/**
	 * Removes the given component type from the entity.
	 * <p>
	 * @param e    The entity to remove the component from
	 * @param type The type of component to remove
	 */
	public void removeComponent(IEntity e, ComponentType type) {
		final BitSet componentBits = e.getComponentBits();
		final int index = type.getIndex();
		if (componentBits.get(index)) {
			mComponentsByType.get(index).set(e.getId(), null);
			componentBits.clear(index);
		}
	}

	/**
	 * Removes all the deleted entities from the {@link EntityManager}.
	 */
	public void clean() {
		final Bag<Bag<IComponent>> componentsByType = mComponentsByType;
		final Bag<IEntity> deleted = mDeleted;
		if (!deleted.isEmpty()) {
			final int numDeleted = deleted.size();
			for (int i = 0; i < numDeleted; ++i) {
				final IEntity e = deleted.get(i);
				final BitSet componentBits = e.getComponentBits();
				for (int j = componentBits.nextSetBit(0); j >= 0; j = componentBits.nextSetBit(j + 1)) {
					componentsByType.get(j).set(e.getId(), null);
				}
				componentBits.clear();
			}
			deleted.clear();
		}
	}
}
