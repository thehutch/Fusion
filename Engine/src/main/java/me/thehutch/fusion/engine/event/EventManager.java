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

/**
 * @author thehutch
 */
public class EventManager implements IEventManager {
	private final TMap<Class<? extends Event>, SortedSet<EventExecutor>> events = new THashMap<>();

	@Override
	public <T extends Event> T callEvent(T event) {
		final Set<EventExecutor> executors = events.get(event.getClass());
		// Check if there are any event executors for this event
		if (executors != null) {
			executors.stream().forEachOrdered((EventExecutor executor) -> {
				executor.execute(event);
			});
		}
		return event;
	}

	@Override
	public <T extends Event> T callEventAsync(T event) {
		final Set<EventExecutor> executors = events.get(event.getClass());
		// Check if there are any event executors for this event
		if (executors != null) {
			final Thread thread = new Thread(() -> {
				executors.stream().forEachOrdered((EventExecutor executor) -> {
					executor.execute(event);
				});
			});
			thread.start();
		}
		return event;
	}

	@Override
	public <T extends Event> void registerEvent(Consumer<T> handler, Class<T> eventClass, EventPriority priority, boolean ignoreCancelled) {
		SortedSet<EventExecutor> executors = events.get(eventClass);
		if (executors == null) {
			executors = new TreeSet<>();
		}
		if (!executors.add(new EventExecutor<>(handler, priority, ignoreCancelled))) {
			throw new IllegalStateException("Duplicate events registered: " + eventClass.getName());
		}
		this.events.put(eventClass, executors);
	}

	private class EventExecutor<T extends Event> implements Comparable<EventExecutor> {
		private final Consumer<T> function;
		private final EventPriority priority;
		private final boolean ignoreCancelled;

		private EventExecutor(Consumer<T> function, EventPriority priority, boolean ignoreCancelled) {
			this.function = function;
			this.priority = priority;
			this.ignoreCancelled = ignoreCancelled;
		}

		public void execute(T event) {
			if (!event.isCancelled() || ignoreCancelled) {
				this.function.accept(event);
			}
		}

		@Override
		public int compareTo(EventExecutor other) {
			return priority.getPriority() - other.priority.getPriority();
		}
	}
}
