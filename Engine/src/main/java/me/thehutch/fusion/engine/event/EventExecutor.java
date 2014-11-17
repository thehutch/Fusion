/*
 * This file is part of Engine, licensed under the Apache 2.0 License.
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
package me.thehutch.fusion.engine.event;

import java.util.function.Consumer;
import me.thehutch.fusion.api.event.Event;
import me.thehutch.fusion.api.event.EventPriority;

/**
 * @author thehutch
 * @param <T>
 */
public class EventExecutor<T extends Event> implements Comparable<EventExecutor> {
	private final EventPriority mPriority;
	private final boolean mIgnoreCancelled;
	protected final Consumer<T> mFunction;

	/**
	 * The default constructor for {@link EventExecutor}.
	 *
	 * @param function        The event handler function
	 * @param priority        The priority of the event
	 * @param ignoreCancelled True if this event executor ignores cancelled events
	 */
	public EventExecutor(Consumer<T> function, EventPriority priority, boolean ignoreCancelled) {
		this.mFunction = function;
		this.mPriority = priority;
		this.mIgnoreCancelled = ignoreCancelled;
	}

	/**
	 * Invokes the event handler function with the given event.
	 *
	 * @param event The event to handle
	 */
	public void execute(T event) {
		if (!event.isCancelled() || mIgnoreCancelled) {
			mFunction.accept(event);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(EventExecutor o) {
		return o.mPriority.ordinal() - mPriority.ordinal();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return mFunction.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof EventExecutor && super.equals(obj));
	}
}
