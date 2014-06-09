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
import me.thehutch.fusion.api.util.Disposable;

/**
 * @author thehutch
 *
 * @param <T> The type of the owner of this component holder
 */
public class ComponentHolder<T> implements Disposable {
	private final TMap<Class<? extends Component>, Component> components = new THashMap<>();
	private final T owner;

	public ComponentHolder(T owner) {
		this.owner = owner;
	}

	public T getOwner() {
		return owner;
	}

	public <T extends Component> T attach(T component) {
		// Check if there is already a component of the same type attached
		final Component comp = components.get(component.getClass());
		if (comp != null) {
			if (comp.isDetachable()) {
				comp.onDetach();
			} else {
				return (T) comp;
			}
		}
		this.components.put(component.getClass(), component);
		component.onAttach();
		return component;
	}

	public <T extends Component> void detach(Class<T> compClass) {
		final Component component = components.get(compClass);
		if (component != null && component.isDetachable()) {
			// Dispose the component
			component.onDetach();
			// Remove the component
			this.components.remove(compClass);
		}
	}

	public <T extends Component> T get(Class<T> compClass) {
		return (T) components.get(compClass);
	}

	public boolean has(Class<? extends Component> compClass) {
		return components.containsKey(compClass);
	}

	@Override
	public void dispose() {
		this.components.values().forEach((Component component) -> {
			component.onDetach();
		});
		this.components.clear();
	}
}
