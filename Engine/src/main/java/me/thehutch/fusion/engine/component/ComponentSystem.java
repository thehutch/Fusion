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

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import me.thehutch.fusion.api.component.EntityProcessor;
import me.thehutch.fusion.api.component.IComponentSystem;
import me.thehutch.fusion.api.component.IEntity;
import me.thehutch.fusion.api.component.IEntityObserver;
import me.thehutch.fusion.api.component.Manager;
import me.thehutch.fusion.api.component.annotations.Mapper;
import me.thehutch.fusion.api.util.container.Bag;
import me.thehutch.fusion.api.util.container.ImmutableBag;

/**
 * @author thehutch
 */
public final class ComponentSystem implements IComponentSystem {
	/*
	 * The managers of this system
	 */
	private final TMap<Class<? extends Manager>, Manager> mManagers = new THashMap<>();
	private final Bag<Manager> mManagersBag = new Bag<>();
	/**
	 * The processors of this system
	 */
	private final TMap<Class<?>, EntityProcessor> mProcessors = new THashMap<>();
	private final Bag<EntityProcessor> mProcessorsBag = new Bag<>();
	/**
	 * The entities
	 */
	private final Bag<IEntity> mAdded = new Bag<>();
	private final Bag<IEntity> mDeleted = new Bag<>();
	private final Bag<IEntity> mChanged = new Bag<>();
	private final Bag<IEntity> mEnabled = new Bag<>();
	private final Bag<IEntity> mDisabled = new Bag<>();
	/**
	 * The component manager
	 */
	private final ComponentManager mComponentManager;
	/**
	 * The entity manager
	 */
	private final EntityManager mEntityManager;

	/**
	 * Default constructor for {@link ComponentSystem}.
	 */
	public ComponentSystem() {
		mComponentManager = new ComponentManager();
		addManager(mComponentManager);

		mEntityManager = new EntityManager();
		addManager(mEntityManager);
	}

	/**
	 * Initialise the component system by initialising all {@link Manager}'s,
	 * {@link EntityProcessor}'s and setting the {@link ComponentMapper}'s.
	 */
	public void initialise() {
		// Initialise the managers
		final Bag<Manager> managersBag = mManagersBag;
		final int numManagers = managersBag.size();
		for (int i = 0; i < numManagers; ++i) {
			managersBag.get(i).initialise();
		}
		// Initialise the processors
		final ComponentManager componentManager = mComponentManager;
		final Bag<EntityProcessor> processors = mProcessorsBag;
		final int numProcessors = processors.size();
		for (int i = 0; i < numProcessors; ++i) {
			final EntityProcessor processor = processors.get(i);
			config(processor, componentManager);
			processor.initialise();
		}
	}

	/**
	 * @return The component manager
	 */
	public ComponentManager getComponentManager() {
		return mComponentManager;
	}

	/**
	 * @return The entity manager
	 */
	public EntityManager getEntityManager() {
		return mEntityManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addEntity(IEntity e) {
		mAdded.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteEntity(IEntity e) {
		mDeleted.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changeEntity(IEntity e) {
		mChanged.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enable(IEntity e) {
		mEnabled.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disable(IEntity e) {
		mDisabled.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Entity getEntity(int entityId) {
		return (Entity) mEntityManager.getEntity(entityId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Entity createEntity() {
		return mEntityManager.createEntityInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Manager> T getManager(Class<T> managerType) {
		return managerType.cast(mManagers.get(managerType));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Manager> T addManager(T manager) {
		mManagers.put(manager.getClass(), manager);
		mManagersBag.add(manager);
		manager.setSystem(this);
		return manager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeManager(Manager manager) {
		mManagers.remove(manager.getClass());
		mManagersBag.remove(manager);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends EntityProcessor> T getProcessor(Class<T> type) {
		return type.cast(mProcessors.get(type));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImmutableBag<EntityProcessor> getProcessors() {
		return mProcessorsBag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends EntityProcessor> T addProcessor(T processor) {
		return addProcessor(processor, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends EntityProcessor> T addProcessor(T processor, boolean passive) {
		processor.setPassive(passive);
		processor.setSystem(this);

		mProcessors.put(processor.getClass(), processor);
		mProcessorsBag.add(processor);

		return processor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeProcessor(EntityProcessor processor) {
		mProcessors.remove(processor.getClass());
		mProcessorsBag.remove(processor);
	}

	/**
	 * Update all entities and process all non-passive processors.
	 */
	public void process() {
		check(mAdded, (observer, e) -> observer.added(e));
		check(mChanged, (observer, e) -> observer.changed(e));
		check(mDisabled, (observer, e) -> observer.disabled(e));
		check(mEnabled, (observer, e) -> observer.enabled(e));
		check(mDeleted, (observer, e) -> observer.deleted(e));
		mComponentManager.clean();

		// Process the processors
		final ImmutableBag<EntityProcessor> processorBag = mProcessorsBag;
		final int numProcessors = processorBag.size();
		for (int i = 0; i < numProcessors; ++i) {
			final EntityProcessor processor = processorBag.get(i);
			if (!processor.isPassive()) {
				processor.process();
			}
		}
	}

	/**
	 * Internal method to process all the entities inside of the given {@link Bag}.
	 * <p>
	 * @param entities  The entities to process
	 * @param performer The function to execute on each entity
	 */
	private void check(Bag<IEntity> entities, Performer performer) {
		if (!entities.isEmpty()) {
			final ImmutableBag<Manager> managerBag = mManagersBag;
			final ImmutableBag<EntityProcessor> processorBag = mProcessorsBag;
			for (int i = 0; i < entities.size(); ++i) {
				// Get the next entity
				final IEntity e = entities.get(i);

				// Notify the managers
				final int numManagers = managerBag.size();
				for (int m = 0; m < numManagers; ++m) {
					performer.perform(managerBag.get(m), e);
				}

				// Notify the processors
				final int numProcessors = processorBag.size();
				for (int j = 0, p = numProcessors; p > j; ++j) {
					performer.perform(processorBag.get(j), e);
				}
			}
			entities.clear();
		}
	}

	/**
	 * Internal function to set the {@link ComponentMapper}'s of the given target
	 * <p>
	 * @param target
	 * @param system
	 */
	private static void config(Object target, ComponentManager componentManager) {
		try {
			final Class<?> clazz = target.getClass();
			for (Field field : clazz.getDeclaredFields()) {
				final Mapper annotation = field.getAnnotation(Mapper.class);
				if (annotation != null) {
					final ParameterizedType genericType = (ParameterizedType) field.getGenericType();
					final Class componentType = (Class) genericType.getActualTypeArguments()[0];

					field.setAccessible(true);
					field.set(target, ComponentMapper.getFor(componentType, componentManager));
					field.setAccessible(false);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException ex) {
			throw new RuntimeException("Error whilst setting component mappers!", ex);
		}
	}

	/**
	 * Functional interface to aid in updating entities.
	 */
	@FunctionalInterface
	private interface Performer {
		public void perform(IEntityObserver observer, IEntity e);
	}
}
