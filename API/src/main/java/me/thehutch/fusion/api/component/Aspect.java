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

	private Aspect() {
	}

	public Aspect all(Class<? extends Component> type, Class<? extends Component>... types) {
		this.allSet.set(ComponentType.getIndexFor(type));
		for (Class<? extends Component> t : types) {
			this.allSet.set(ComponentType.getIndexFor(t));
		}
		return this;
	}

	public Aspect one(Class<? extends Component> type, Class<? extends Component>... types) {
		this.oneSet.set(ComponentType.getIndexFor(type));
		for (Class<? extends Component> t : types) {
			this.oneSet.set(ComponentType.getIndexFor(t));
		}
		return this;
	}

	public Aspect exclude(Class<? extends Component> type, Class<? extends Component>... types) {
		this.exclusionSet.set(ComponentType.getIndexFor(type));
		for (Class<? extends Component> t : types) {
			this.exclusionSet.set(ComponentType.getIndexFor(t));
		}
		return this;
	}

	BitSet getAllSet() {
		return allSet;
	}

	BitSet getOneSet() {
		return oneSet;
	}

	BitSet getExclusionSet() {
		return exclusionSet;
	}

	public static Aspect getAspectFor(Class<? extends Component> type, Class<? extends Component>... types) {
		return getAspectForAll(type, types);
	}

	public static Aspect getAspectForAll(Class<? extends Component> type, Class<? extends Component>... types) {
		final Aspect aspect = new Aspect();
		aspect.all(type, types);
		return aspect;
	}

	public static Aspect getAspectForOne(Class<? extends Component> type, Class<? extends Component>... types) {
		final Aspect aspect = new Aspect();
		aspect.one(type, types);
		return aspect;
	}

	public static Aspect getEmpty() {
		return new Aspect();
	}
}
