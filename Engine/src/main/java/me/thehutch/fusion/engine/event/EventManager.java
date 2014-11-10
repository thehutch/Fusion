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

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;
import me.thehutch.fusion.api.event.Event;
import me.thehutch.fusion.api.event.EventPriority;
import me.thehutch.fusion.api.event.IEventManager;
import me.thehutch.fusion.api.scheduler.IScheduler;
import me.thehutch.fusion.api.scheduler.TaskPriority;
import me.thehutch.fusion.engine.util.ReflectionHelper;

/**
 * @author thehutch
 */
public class EventManager implements IEventManager {
	private final TMap<Class<? extends Event>, SortedSet<EventExecutor>> events;
	private final IScheduler scheduler;

	/**
	 * The default constructor for {@link EventManager}.
	 *
	 * @param scheduler The engine scheduler
	 */
	public EventManager(IScheduler scheduler) {
		this.events = new THashMap<>();
		this.scheduler = scheduler;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Event> void invoke(T event) {
		final Set<EventExecutor> executors = events.get(event.getClass());
		if (executors != null) {
			executors.forEach(executor -> executor.execute(event));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Event> void invokeDelayed(T event, long delay) {
		this.scheduler.invokeDelayed(() -> invoke(event), TaskPriority.HIGHEST, delay);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Event> void invokeAsync(T event) {
		this.scheduler.invokeAsync(() -> invoke(event), TaskPriority.HIGHEST);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Event> void invokeAsync(T event, Consumer<T> callback) {
		this.scheduler.invokeAsync(() -> {
			invoke(event);
			callback.accept(event);
		}, TaskPriority.HIGHEST);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Event> void register(Consumer<T> handler, EventPriority priority, boolean ignoreCancelled) {
		final Class<T> eventClass = ReflectionHelper.getGenericClass();
		SortedSet<EventExecutor> executors = events.get(eventClass);
		if (executors == null) {
			executors = new TreeSet<>();
			this.events.put(eventClass, executors);
		}
		executors.add(new EventExecutor(handler, priority, ignoreCancelled));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Event> void unregister(Consumer<T> handler) {
		final Class<T> eventClass = ReflectionHelper.getGenericClass();
		final SortedSet<EventExecutor> executors = events.get(eventClass);
		if (executors == null) {
			throw new IllegalArgumentException("No events to unregister of type " + eventClass.getName());
		}
		final boolean isRemoved = executors.removeIf(executor -> executor.function.equals(handler));
		if (!isRemoved) {
			throw new IllegalStateException("Failed to unregister event " + eventClass.getName());
		}
	}

	/**
	 * A class wrapper to hold the function handler for the event
	 *
	 * @param <T> The type of event
	 */
	private class EventExecutor<T extends Event> implements Comparable<EventExecutor> {
		private final Consumer<T> function;
		private final EventPriority priority;
		private final boolean ignoreCancelled;

		/**
		 * The default constructor for {@link EventExecutor}.
		 *
		 * @param function        The event handler function
		 * @param priority        The priority of the event
		 * @param ignoreCancelled True if this event executor ignores cancelled events
		 */
		private EventExecutor(Consumer<T> function, EventPriority priority, boolean ignoreCancelled) {
			this.function = function;
			this.priority = priority;
			this.ignoreCancelled = ignoreCancelled;
		}

		/**
		 * Invokes the event handler function with the given event.
		 *
		 * @param event The event to handle
		 */
		public void execute(T event) {
			if (!event.isCancelled() || ignoreCancelled) {
				this.function.accept(event);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(EventExecutor other) {
			return priority.getPriority() - other.priority.getPriority();
		}
	}
}
