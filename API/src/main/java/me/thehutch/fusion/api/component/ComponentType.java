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

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;

/**
 * @author thehutch
 */
public final class ComponentType {
	private static final TMap<Class<? extends IComponent>, ComponentType> COMPONENT_TYPES = new THashMap<>();
	private static int INDEX = 0;

	private final int index;

	/**
	 * Default constructor for {@link ComponentType}.
	 * <p>
	 * @param type The component class
	 */
	private ComponentType(Class<? extends IComponent> type) {
		this.index = INDEX++;
	}

	/**
	 * @return The index of the component
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Get the {@link ComponentType} for the given component class.
	 * <p>
	 * @param c The component class
	 * <p>
	 * @return The component type
	 */
	public static ComponentType getTypeFor(Class<? extends IComponent> c) {
		ComponentType type = COMPONENT_TYPES.get(c);
		if (type == null) {
			type = new ComponentType(c);
			COMPONENT_TYPES.put(c, type);
		}
		return type;
	}

	/**
	 * @param c The component class
	 * <p>
	 * @return The index of the component
	 */
	public static int getIndexFor(Class<? extends IComponent> c) {
		return getTypeFor(c).getIndex();
	}
}
