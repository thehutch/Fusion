/*
 * This file is part of Engine.
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import me.thehutch.fusion.api.event.Event;
import me.thehutch.fusion.api.event.EventHandler;
import me.thehutch.fusion.api.event.EventPriority;
import me.thehutch.fusion.api.event.IEventManager;
import me.thehutch.fusion.api.util.ReflectionHelper;

/**
 * @author thehutch
 */
public class EventManager implements IEventManager {
	private final TMap<Class<? extends Event>, SortedSet<EventExecutor>> events = new THashMap<>();

	@Override
	public <T extends Event> T callEvent(final T event) {
		final SortedSet<EventExecutor> executors = events.get(event.getClass());
		// Check if there are any event executors for this event
		if (executors == null) {
			return event;
		}
		try {
			for (EventExecutor executor : executors) {
				// Execute the event
				executor.execute(event);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			ex.printStackTrace();
		}
		return event;
	}

	@Override
	public <T extends Event> T callEventAsync(T event) {
		throw new UnsupportedOperationException("callEventAsync in class EventManager is not supported yet.");
	}

	@Override
	public void registerListener(Object listenerObj) {
		final Collection<Method> methods = ReflectionHelper.getAnnotatedMethods(listenerObj.getClass(), EventHandler.class);
		for (Method method : methods) {
			final EventHandler handler = method.getAnnotation(EventHandler.class);
			final Class<?>[] parameters = method.getParameterTypes();
			if (ReflectionHelper.hasExactParameters(method, Event.class)) {
				final Class<? extends Event> event = parameters[0].asSubclass(Event.class);
				SortedSet<EventExecutor> executors = events.get(event);
				if (executors == null) {
					executors = new TreeSet<>(new Comparator<EventExecutor>() {
						@Override
						public int compare(EventExecutor e1, EventExecutor e2) {
							return e1.priority.getPriority() - e2.priority.getPriority();
						}
					});
				}
				if (executors.add(new EventExecutor(listenerObj, method, handler.priority(), handler.ignoreCancelled()))) {
					System.out.println("Duplicate Event Found!");
				}
				this.events.put(event, executors);
			}
		}
	}

	private class EventExecutor {
		private final boolean ignoreCancelled;
		private final EventPriority priority;
		private final Object invoker;
		private final Method method;

		private EventExecutor(Object invoker, Method method, EventPriority priority, boolean ignoreCancelled) {
			this.invoker = invoker;
			this.method = method;
			this.priority = priority;
			this.ignoreCancelled = ignoreCancelled;
		}

		public void execute(Event event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			if (!event.isCancelled() || ignoreCancelled) {
				this.method.invoke(invoker, event);
			}
		}
	}
}
