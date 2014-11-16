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

import me.thehutch.fusion.api.component.ComponentType;
import me.thehutch.fusion.api.component.IComponent;
import me.thehutch.fusion.api.component.IComponentMapper;
import me.thehutch.fusion.api.component.IEntity;
import me.thehutch.fusion.api.util.container.Bag;

/**
 * @author thehutch
 * @param <T> The type of component this mapper maps to
 */
public final class ComponentMapper<T extends IComponent> implements IComponentMapper<T> {
	private final Bag<IComponent> mComponents;
	private final Class<T> mClassType;

	/**
	 * Default constructor for {@link ComponentMapper}.
	 * <p>
	 * @param type    The component class to map to
	 * @param manager The component manager
	 */
	private ComponentMapper(Class<T> type, ComponentManager manager) {
		mComponents = manager.getComponentsByType(ComponentType.getTypeFor(type));
		mClassType = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get(IEntity e) {
		final int entityID = e.getId();
		final IComponent component = mComponents.get(entityID);
		return mClassType.cast(component);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getSafe(IEntity e) {
		if (mComponents.isIndexWithinBounds(e.getId())) {
			return mClassType.cast(mComponents.get(e.getId()));
		}
		return null;
	}

	/**
	 * Creates a new {@link ComponentMapper} mapping to the given component type.
	 * <p>
	 * @param <T>     The type of component
	 * @param type    The component class
	 * @param manager The component manager
	 * <p>
	 * @return A new {@link ComponentMapper}
	 */
	public static <T extends IComponent> ComponentMapper<T> getFor(Class<T> type, ComponentManager manager) {
		return new ComponentMapper<>(type, manager);
	}
}
