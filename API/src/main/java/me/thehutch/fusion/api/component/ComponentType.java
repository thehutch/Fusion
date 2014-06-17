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

/**
 * @author thehutch
 */
public class ComponentType {
	private static final TMap<Class<? extends Component>, ComponentType> COMPONENT_TYPES = new THashMap<>();
	private static int INDEX = 0;

	private final Class<? extends Component> type;
	private final int index;

	public ComponentType(Class<? extends Component> type) {
		this.index = INDEX++;
		this.type = type;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public String toString() {
		return "ComponentType[" + type.getSimpleName() + "] (" + index + ")";
	}

	public static ComponentType getTypeFor(Class<? extends Component> c) {
		ComponentType type = COMPONENT_TYPES.get(c);
		if (type == null) {
			type = new ComponentType(c);
			COMPONENT_TYPES.put(c, type);
		}
		return type;
	}

	public static int getIndexFor(Class<? extends Component> c) {
		return getTypeFor(c).getIndex();
	}
}
