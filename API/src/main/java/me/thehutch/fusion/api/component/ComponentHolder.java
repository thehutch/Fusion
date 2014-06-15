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
 * @param <E> The type of the owner of this component holder
 */
public class ComponentHolder<E> implements Disposable {
	private final TMap<Class<? extends Component<E>>, Component<E>> components = new THashMap<>();
	private final E owner;

	public ComponentHolder(E owner) {
		this.owner = owner;
	}

	/**
	 * Retrieves the entity which holds these components.
	 *
	 * @return The components owner
	 */
	public E getOwner() {
		return owner;
	}

	/**
	 * Attaches a component to the holder. If there is already a component of the same type
	 * attached, then it if detached if and only if it is detachable, otherwise, that component
	 * is returned.
	 *
	 * @param <T>       The type of component
	 * @param compClass The component class
	 *
	 * @return The attached component
	 */
	public <T extends Component<E>> T attach(Class<T> compClass) {
		// Check if there is already a component of the same type attached
		final Component comp = components.get(compClass);
		if (comp != null) {
			if (comp.isDetachable()) {
				comp.onDetach();
			} else {
				return (T) comp;
			}
		}
		try {
			// Create the new component
			final T newComponent = compClass.newInstance();
			// Set the component owner
			newComponent.attach(owner);
			// Attach the component
			newComponent.onAttach();
			// Add the component to the components map
			this.components.put(compClass, newComponent);
			return newComponent;
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new IllegalArgumentException("Unable to attach component: " + compClass.getName(), ex);
		}
	}

	/**
	 * Detaches the component from the holder, only if that component type is detachable.
	 *
	 * @param <T>       The type of component
	 * @param compClass The component class
	 */
	public <T extends Component<E>> void detach(Class<T> compClass) {
		final Component component = components.get(compClass);
		if (component != null && component.isDetachable()) {
			// Dispose the component
			component.onDetach();
			// Remove the component
			this.components.remove(compClass);
		}
	}

	/**
	 * Retrieves the component of the given type.
	 *
	 * @param <T>       The type of component
	 * @param compClass The component class
	 *
	 * @return The attached component, null if not found
	 */
	public <T extends Component<E>> T get(Class<T> compClass) {
		return (T) components.get(compClass);
	}

	/**
	 * Checks if the component type is attached to this holder.
	 *
	 * @param compClass The component class
	 *
	 * @return True if the component is found
	 */
	public boolean has(Class<? extends Component<E>> compClass) {
		return components.containsKey(compClass);
	}

	@Override
	public void dispose() {
		this.components.values().forEach((Component<E> component) -> {
			component.onDetach();
		});
		this.components.clear();
	}
}
