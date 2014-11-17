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

/**
 * @author thehutch
 * @param <T> The type of component this mapper maps to
 */
public interface IComponentMapper<T extends IComponent> {
	/**
	 * Returns the component which this mapper handles from
	 * the provided entity.
	 * <p>
	 * @param e The entity to get the component from
	 * <p>
	 * @return The component
	 */
	public T get(IEntity e);

	/**
	 * Returns the component which this mapper handles from
	 * the provided entity.
	 * <p>
	 * This method checks that the entity has the component
	 * otherwise it will return null.
	 * <p>
	 * @param e The entity to get the component from
	 * <p>
	 * @return The component, null if not found
	 */
	public T getSafe(IEntity e);

	/**
	 * @param e The entity
	 * <p>
	 * @return True if this provided entity has this mapper's component
	 */
	default boolean has(IEntity e) {
		return getSafe(e) != null;
	}
}
