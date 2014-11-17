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
package me.thehutch.fusion.api.util;

/**
 * @author thehutch
 */
public abstract class Creatable implements Disposable {
	private boolean mCreated;

	/**
	 * Default constructor for {@link Creatable}.
	 */
	public Creatable() {
		mCreated = false;
	}

	/**
	 * Override this method to use as the initialisation method.
	 * The base method should also be called to set mCreated to true.
	 */
	public void create() {
		mCreated = true;
	}

	/**
	 * @return True if this object has been mCreated
	 */
	public final boolean isCreated() {
		return mCreated;
	}

	/**
	 * Ensures that this object has been mCreated, otherwise throws an
	 * {@link IllegalStateException}.
	 *
	 * @param message The message to display if this object has not been mCreated
	 */
	public final void ensureCreated(String message) {
		if (!mCreated) {
			throw new IllegalStateException(message);
		}
	}

	/**
	 * Ensures that this object has not been mCreated, otherwise throws an
	 * {@link IllegalStateException}.
	 *
	 * @param message The message to display if this object has been mCreated
	 */
	public final void ensureNotCreated(String message) {
		if (mCreated) {
			throw new IllegalStateException(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		mCreated = false;
	}
}
