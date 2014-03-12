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
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import me.thehutch.fusion.api.event.Event;
import me.thehutch.fusion.api.event.EventHandler;
import me.thehutch.fusion.api.event.EventPriority;
import me.thehutch.fusion.api.event.IEventManager;

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
				// Set the executor's event
				executor.event = event;
				// Execute the event
				executor.execute();
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
		final Method[] methods = listenerObj.getClass().getMethods();
		for (Method method : methods) {
			final EventHandler handler = method.getAnnotation(EventHandler.class);
			if (handler != null) {
				final Class<?>[] parameters = method.getParameterTypes();
				if (parameters.length == 1 && Event.class.isAssignableFrom(parameters[0])) {
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
					if (executors.add(new EventExecutor(listenerObj, method, handler.priority()))) {
						System.out.println("Duplicate Event Found!");
					}
					this.events.put(event, executors);
				}
			}
		}
	}

	private class EventExecutor {
		private final EventPriority priority;
		private final Object invoker;
		private final Method method;
		private Event event;

		private EventExecutor(Object invoker, Method method, EventPriority priority) {
			this.invoker = invoker;
			this.method = method;
			this.priority = priority;
		}

		public void execute() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			this.method.invoke(invoker, event);
		}
	}
}
