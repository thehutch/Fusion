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

/**
 * @author thehutch
 */
public class EventManager implements IEventManager {
	private final TMap<Class<? extends Event>, SortedSet<EventExecutor>> mEvents;
	private final IScheduler mScheduler;

	/**
	 * The default constructor for {@link EventManager}.
	 *
	 * @param scheduler The engine scheduler
	 */
	public EventManager(IScheduler scheduler) {
		mEvents = new THashMap<>();
		mScheduler = scheduler;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Event> void invoke(T event) {
		final Set<EventExecutor> executors = mEvents.get(event.getClass());
		if (executors != null) {
			executors.forEach(executor -> executor.execute(event));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Event> void invokeDelayed(T event, long delay) {
		mScheduler.invokeDelayed(() -> invoke(event), TaskPriority.HIGHEST, delay);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Event> void invokeAsync(T event) {
		mScheduler.invokeAsync(() -> invoke(event), TaskPriority.HIGHEST);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Event> void invokeAsync(T event, Consumer<T> callback) {
		mScheduler.invokeAsync(() -> {
			invoke(event);
			callback.accept(event);
		}, TaskPriority.HIGHEST);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Event> void register(Consumer<T> handler, Class<T> eventClass, EventPriority priority, boolean ignoreCancelled) {
		SortedSet<EventExecutor> executors = mEvents.get(eventClass);
		if (executors == null) {
			executors = new TreeSet<>();
			this.mEvents.put(eventClass, executors);
		}
		executors.add(new EventExecutor(handler, priority, ignoreCancelled));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Event> void unregister(Consumer<T> handler, Class<T> eventClass) {
		final SortedSet<EventExecutor> executors = mEvents.get(eventClass);
		if (executors == null) {
			throw new IllegalArgumentException("No events to unregister of type " + eventClass.getName());
		}
		final boolean isRemoved = executors.removeIf(executor -> executor.mFunction.equals(handler));
		if (!isRemoved) {
			throw new IllegalStateException("Failed to unregister event " + eventClass.getName());
		}
	}
}
