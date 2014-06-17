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

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import me.thehutch.fusion.api.component.annotations.Mapper;
import me.thehutch.fusion.api.util.container.Bag;
import me.thehutch.fusion.api.util.container.ImmutableBag;

/**
 * @author thehutch
 */
public final class ComponentSystem {
	/*
	 * The managers of this system
	 */
	private final TMap<Class<? extends Manager>, Manager> managers = new THashMap<>();
	private final Bag<Manager> managersBag = new Bag<>();
	/**
	 * The processors of this system
	 */
	private final TMap<Class<?>, EntityProcessor> processors = new THashMap<>();
	private final Bag<EntityProcessor> processorsBag = new Bag<>();
	/**
	 * The entities
	 */
	private final Bag<Entity> added = new Bag<>();
	private final Bag<Entity> deleted = new Bag<>();
	private final Bag<Entity> changed = new Bag<>();
	private final Bag<Entity> enabled = new Bag<>();
	private final Bag<Entity> disabled = new Bag<>();
	/**
	 * The component manager
	 */
	private final ComponentManager componentManager;
	/**
	 * The entity manager
	 */
	private final EntityManager entityManager;

	private float delta;

	public ComponentSystem() {
		this.componentManager = new ComponentManager();
		addManager(componentManager);

		this.entityManager = new EntityManager();
		addManager(entityManager);
	}

	public void initialise() {
		// Initialise the managers
		for (int i = 0; i < managersBag.size(); ++i) {
			this.managersBag.get(i).initialise();
		}
		// Initialise the processors
		for (int i = 0; i < processorsBag.size(); ++i) {
			config(processorsBag.get(i), this);
			this.processorsBag.get(i).initialise();
		}
	}

	/**
	 * Time since the last game loop.
	 *
	 * @return delta time since last game loop
	 */
	public float getDelta() {
		return delta;
	}

	/**
	 * Specify the delta of the game.
	 *
	 * @param delta time since the last game loop
	 */
	public void setDelta(float delta) {
		this.delta = delta;
	}

	public ComponentManager getComponentManager() {
		return componentManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void addEntity(Entity e) {
		this.added.add(e);
	}

	public void deleteEntity(Entity e) {
		this.deleted.add(e);
	}

	public void changeEntity(Entity e) {
		this.changed.add(e);
	}

	public void enable(Entity e) {
		this.enabled.add(e);
	}

	public void disable(Entity e) {
		this.disabled.add(e);
	}

	public Entity getEntity(int entityId) {
		return entityManager.getEntity(entityId);
	}

	public Entity createEntity() {
		return entityManager.createEntityInstance();
	}

	public <T extends Manager> T getManager(Class<T> managerType) {
		return managerType.cast(managers.get(managerType));
	}

	public <T extends Manager> T addManager(T manager) {
		this.managers.put(manager.getClass(), manager);
		this.managersBag.add(manager);
		manager.setSystem(this);
		return manager;
	}

	public void deleteManager(Manager manager) {
		this.managers.remove(manager.getClass());
		this.managersBag.remove(manager);
	}

	public <T extends EntityProcessor> T getProcessor(Class<T> type) {
		return type.cast(processors.get(type));
	}

	public ImmutableBag<EntityProcessor> getProcessors() {
		return processorsBag;
	}

	public <T extends EntityProcessor> T addProcessor(T processor) {
		return addProcessor(processor, false);
	}

	public <T extends EntityProcessor> T addProcessor(T processor, boolean passive) {
		processor.setPassive(passive);
		processor.setSystem(this);

		this.processors.put(processor.getClass(), processor);
		this.processorsBag.add(processor);

		return processor;
	}

	public void deleteProcessor(EntityProcessor processor) {
		this.processors.remove(processor.getClass());
		this.processorsBag.remove(processor);
	}

	public <T extends Component> ComponentMapper<T> getMapper(Class<T> type) {
		return ComponentMapper.getFor(type, this);
	}

	/**
	 * Process all non-passive processors.
	 */
	public void process() {
		check(added, (EntityObserver observer, Entity e) -> {
			observer.added(e);
		});
		check(changed, (EntityObserver observer, Entity e) -> {
			observer.changed(e);
		});
		check(disabled, (EntityObserver observer, Entity e) -> {
			observer.disabled(e);
		});
		check(enabled, (EntityObserver observer, Entity e) -> {
			observer.enabled(e);
		});
		check(deleted, (EntityObserver observer, Entity e) -> {
			observer.deleted(e);
		});
		this.componentManager.clean();

		// Process the processors
		for (int i = 0; i < processors.size(); ++i) {
			final EntityProcessor processor = processorsBag.get(i);
			if (!processor.isPassive()) {
				processor.process();
			}
		}
	}

	private void check(Bag<Entity> entities, Performer performer) {
		if (!entities.isEmpty()) {
			for (int i = 0; i < entities.size(); ++i) {
				final Entity e = entities.get(i);
				// Notify the managers
				for (int m = 0; m < managersBag.size(); ++m) {
					performer.perform(managersBag.get(m), e);
				}
				// Notify the processors
				for (int j = 0, p = processorsBag.size(); p > j; ++j) {
					performer.perform(processorsBag.get(j), e);
				}
			}
			entities.clear();
		}
	}

	public static void config(Object target, ComponentSystem system) {
		try {
			final Class<?> clazz = target.getClass();
			for (Field field : clazz.getDeclaredFields()) {
				final Mapper annotation = field.getAnnotation(Mapper.class);
				if (annotation != null && Mapper.class.isAssignableFrom(Mapper.class)) {
					final ParameterizedType genericType = (ParameterizedType) field.getGenericType();
					final Class componentType = (Class) genericType.getActualTypeArguments()[0];

					field.setAccessible(true);
					field.set(target, system.getMapper(componentType));
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException ex) {
			throw new RuntimeException("Error whilst setting component mappers!", ex);
		}
	}

	@FunctionalInterface
	private interface Performer {
		public void perform(EntityObserver observer, Entity e);
	}
}
