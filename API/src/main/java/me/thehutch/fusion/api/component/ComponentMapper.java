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

import me.thehutch.fusion.api.util.container.Bag;

/**
 * @author thehutch
 * @param <T> The type of component this mapper maps to
 */
public final class ComponentMapper<T extends Component> {
	private final Bag<Component> components;
	private final Class<T> classType;

	public ComponentMapper(Class<T> type, ComponentSystem system) {
		this.components = system.getComponentManager().getComponentsByType(ComponentType.getTypeFor(type));
		this.classType = type;
	}

	public T get(Entity e) {
		return classType.cast(components.get(e.getId()));
	}

	public T getSafe(Entity e) {
		if (components.isIndexWithinBounds(e.getId())) {
			return classType.cast(components.get(e.getId()));
		}
		return null;
	}

	public boolean has(Entity e) {
		return getSafe(e) != null;
	}

	public static <T extends Component> ComponentMapper<T> getFor(Class<T> type, ComponentSystem system) {
		return new ComponentMapper<>(type, system);
	}
}
