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

import me.thehutch.fusion.api.util.container.ImmutableBag;

/**
 * @author thehutch
 */
public interface IComponentSystem {
	/**
	 * Returns the entity with the given id.
	 * <p>
	 * @param entityId The entity's id
	 * <p>
	 * @return The entity
	 */
	public IEntity getEntity(int entityId);

	/**
	 * Creates a new entity.
	 * <p>
	 * @return A new entity
	 */
	public IEntity createEntity();

	/**
	 * Adds a new entity to the system.
	 * <p>
	 * @param e The entity to add
	 */
	public void addEntity(IEntity e);

	/**
	 * Deletes an entity from the system.
	 * <p>
	 * @param e The entity to delete
	 */
	public void deleteEntity(IEntity e);

	/**
	 * This method should be called when an entity changes, for example
	 * when a component is added or removed from them.
	 * <p>
	 * @param e The entity to change
	 */
	public void changeEntity(IEntity e);

	/**
	 * Enables an entity. Enabled entities are processed.
	 * <p>
	 * @param e The entity to enable
	 */
	public void enable(IEntity e);

	/**
	 * Disables an entity. Disabled entities are not processed.
	 * <p>
	 * @param e The entity to disable
	 */
	public void disable(IEntity e);

	/**
	 * Returns a {@link Manager} from this system.
	 * <p>
	 * @param <T>         The type of manager
	 * @param managerType The manager class
	 * <p>
	 * @return The {@link Manager}
	 */
	public <T extends Manager> T getManager(Class<T> managerType);

	/**
	 * Adds a new {@link Manager} to this system.
	 * <p>
	 * @param <T>     The type of manager
	 * @param manager The manager to add
	 * <p>
	 * @return The added {@link Manager}
	 */
	public <T extends Manager> T addManager(T manager);

	/**
	 * Removes a {@link Manager} from this system.
	 * <p>
	 * @param manager The manager to remove
	 */
	public void removeManager(Manager manager);

	/**
	 * Returns an {@link EntityProcessor} from this system.
	 * <p>
	 * @param <T>  The type of processor
	 * @param type The processor class
	 * <p>
	 * @return The {@link EntityProcessor}
	 */
	public <T extends EntityProcessor> T getProcessor(Class<T> type);

	/**
	 * Returns an {@link ImmutableBag} of {@link EntityProcessor}'s held
	 * within this system.
	 * <p>
	 * @return An immutable bag of processors
	 */
	public ImmutableBag<EntityProcessor> getProcessors();

	/**
	 * Adds a new {@link EntityProcessor} to this system. The processor will
	 * not be passive when added.
	 * <p>
	 * @param <T>       The type of processor
	 * @param processor The processor to add
	 * <p>
	 * @return The {@link EntityProcessor}
	 */
	public <T extends EntityProcessor> T addProcessor(T processor);

	/**
	 * Adds a new {@link EntityProcessor} to this system.
	 * <p>
	 * @param <T>       The type of processor
	 * @param processor The processor to add
	 * @param passive   True if this processor should start processing
	 * <p>
	 * @return The {@link EntityProcessor}
	 */
	public <T extends EntityProcessor> T addProcessor(T processor, boolean passive);

	/**
	 * Removes an {@link EntityProcessor} from this system.
	 * <p>
	 * @param processor The processor to remove
	 */
	public void removeProcessor(EntityProcessor processor);
}
