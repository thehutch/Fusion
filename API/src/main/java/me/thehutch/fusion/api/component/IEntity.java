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
package me.thehutch.fusion.api.component;

import java.util.BitSet;
import java.util.UUID;
import me.thehutch.fusion.api.util.container.Bag;

/**
 * @author thehutch
 */
public interface IEntity {
	/**
	 *
	 * @return The entity's id
	 */
	public int getId();

	/**
	 * @return The entity's unique id
	 */
	public UUID getUUID();

	/**
	 * @return True if the entity is currently active
	 */
	public boolean isActive();

	/**
	 * @return True if the entity is currently enabled
	 */
	public boolean isEnabled();

	/**
	 * @return A bitset of the components the entity has
	 */
	public BitSet getComponentBits();

	/**
	 * @return A bitset of the processor this entity is in
	 */
	public BitSet getProcessorBits();

	/**
	 * Enables the entity. An enabled entity will be processed
	 * by all processors which it is part of.
	 */
	public void enable();

	/**
	 * Disables the entity. A disabled entity will not be processed
	 * by any processors.
	 */
	public void disable();

	/**
	 * Returns the {@link IComponent} specified by the given {@link ComponentType}.
	 * <p>
	 * @param type The type of component
	 * <p>
	 * @return The component, null if not found
	 */
	public IComponent getComponent(ComponentType type);

	/**
	 * Returns the {@link IComponent} specified by the given component class.
	 * <p>
	 * @param <T>  The type of component
	 * @param type The component class
	 * <p>
	 * @return The component, null if not found
	 */
	public <T extends IComponent> T getComponent(Class<T> type);

	/**
	 * Retrieves all components held by this entity
	 * <p>
	 * @param fillbag The bag to fill with the components
	 */
	public void getComponents(Bag<IComponent> fillbag);

	/**
	 * Adds a component to the entity
	 * <p>
	 * @param component The component to add
	 * <p>
	 * @return The entity
	 */
	public IEntity addComponent(IComponent component);

	/**
	 * Adds a component to the entity
	 * <p>
	 * @param component The component to add
	 * @param type      The type of component
	 * <p>
	 * @return The entity
	 */
	public IEntity addComponent(IComponent component, ComponentType type);

	/**
	 * Removes a component from the entity.
	 * <p>
	 * @param component The component to remove
	 * <p>
	 * @return The entity
	 */
	public IEntity removeComponent(IComponent component);

	/**
	 * Removes a component from the entity.
	 * <p>
	 * @param type The component class
	 * <p>
	 * @return The entity
	 */
	public IEntity removeComponent(Class<? extends IComponent> type);

	/**
	 * Removes a component from the entity.
	 * <p>
	 * @param type The component type
	 * <p>
	 * @return The entity
	 */
	public IEntity removeComponent(ComponentType type);
}
