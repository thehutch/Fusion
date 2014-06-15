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

import me.thehutch.fusion.api.util.Tickable;

/**
 * @author thehutch
 *
 * @param <T> The type of the owner of this component
 */
public abstract class Component<T> extends Tickable {
	private T owner;

	/**
	 * Retrieves the owner of this component.
	 *
	 * @return The component owner
	 */
	public T getOwner() {
		return owner;
	}

	/**
	 * Called when the component is attached.
	 */
	protected abstract void onAttach();

	/**
	 * Called when the component is detached. Should be used to release any resources.
	 */
	protected abstract void onDetach();

	/**
	 * @return True if this component can be detached from its owner.
	 */
	protected boolean isDetachable() {
		return true;
	}

	/**
	 * Attaches this component to a component holder.
	 *
	 * @param owner The owner this component is attached to
	 */
	void attach(T owner) {
		if (this.owner != null) {
			throw new IllegalStateException("Component can not be attached to more than one owner!");
		}
		this.owner = owner;
	}
}
