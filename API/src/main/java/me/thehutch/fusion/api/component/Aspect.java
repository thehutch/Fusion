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

import java.util.BitSet;

/**
 * @author thehutch
 */
public final class Aspect {
	private final BitSet allSet = new BitSet();
	private final BitSet oneSet = new BitSet();
	private final BitSet exclusionSet = new BitSet();

	/**
	 * Private constructor for {@link Aspect}.
	 */
	private Aspect() {
	}

	/**
	 * @return The all set
	 */
	BitSet getAllSet() {
		return allSet;
	}

	/**
	 * @return The one set
	 */
	BitSet getOneSet() {
		return oneSet;
	}

	/**
	 * @return The exclude set
	 */
	BitSet getExclusionSet() {
		return exclusionSet;
	}

	/**
	 * Creates a new {@link Aspect} for all of the provided component classes.
	 * <p>
	 * @param types The types of classes
	 * <p>
	 * @return A new aspect
	 */
	public static Aspect newAspectForAll(Class<? extends IComponent>... types) {
		if (types == null || types.length == 0) {
			throw new IllegalArgumentException("Number of Aspect types must be greater than 0");
		}
		final Aspect aspect = new Aspect();
		final BitSet bitSet = aspect.getAllSet();
		for (Class<? extends IComponent> type : types) {
			bitSet.set(ComponentType.getIndexFor(type));
		}
		return aspect;
	}

	/**
	 * Creates a new {@link Aspect} for all of the provided component classes.
	 * <p>
	 * @param types The types of classes
	 * <p>
	 * @return A new aspect
	 */
	public static Aspect newAspectForOne(Class<? extends IComponent>... types) {
		if (types == null || types.length == 0) {
			throw new IllegalArgumentException("Number of Aspect types must be greater than 0");
		}
		final Aspect aspect = new Aspect();
		final BitSet bitSet = aspect.getOneSet();
		for (Class<? extends IComponent> type : types) {
			bitSet.set(ComponentType.getIndexFor(type));
		}
		return aspect;
	}

	/**
	 * Creates a new {@link Aspect} for all of the provided component classes.
	 * <p>
	 * @param types The types of classes
	 * <p>
	 * @return A new aspect
	 */
	public static Aspect newAspectForExclude(Class<? extends IComponent>... types) {
		if (types == null || types.length == 0) {
			throw new IllegalArgumentException("Number of Aspect types must be greater than 0");
		}
		final Aspect aspect = new Aspect();
		final BitSet bitSet = aspect.getExclusionSet();
		for (Class<? extends IComponent> type : types) {
			bitSet.set(ComponentType.getIndexFor(type));
		}
		return aspect;
	}

	/**
	 * Creates a new empty {@link Aspect}.
	 * <p>
	 * @return A new empty aspect
	 */
	public static Aspect newEmpty() {
		return new Aspect();
	}
}
