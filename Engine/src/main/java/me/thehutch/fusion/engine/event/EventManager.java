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
/*
 * This file is part of FusionEngine.
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
import gnu.trove.set.hash.THashSet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Set;
import me.thehutch.fusion.api.event.Event;
import me.thehutch.fusion.api.event.EventHandler;
import me.thehutch.fusion.api.event.EventPriority;
import me.thehutch.fusion.api.event.IEventManager;
import me.thehutch.fusion.api.scheduler.TaskPriority;
import me.thehutch.fusion.engine.scheduler.Scheduler;

/**
 * @author thehutch
 */
public class EventManager implements IEventManager {
	private final TMap<Class<? extends Event>, EnumMap<EventPriority, Set<EventExecutor>>> handlers = new THashMap<>();
	private final Scheduler scheduler;

	public EventManager(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public <T extends Event> T callEvent(final T event) {
		final EnumMap<EventPriority, Set<EventExecutor>> listeners = handlers.get(event.getClass());
		// Check if there are any event listeners for this event
		if (listeners == null) {
			return event;
		}
		// Call the event listeners in priority order
		for (EventPriority priority : EventPriority.values()) {
			// Check to see if the event has been cancelled
			if (event.isCancelled() && !priority.ignoresCancelled()) {
				continue;
			}
			// Get all the methods called by the event priority
			final Set<EventExecutor> executors = listeners.get(priority);
			// Check to see if there are methods for this priority
			if (executors != null) {
				for (EventExecutor executor : executors) {
					// Set the event to be executed
					executor.event = event;
					// Schedule the event
					this.scheduler.scheduleSyncTask(executor, TaskPriority.HIGHEST);
				}
			}
		}
		return event;
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
					EnumMap<EventPriority, Set<EventExecutor>> map = handlers.get(event);
					final Set<EventExecutor> executor;
					if (map == null) {
						map = new EnumMap<>(EventPriority.class);
						executor = new THashSet<>();
					} else {
						executor = map.get(handler.priority());
					}
					executor.add(new EventExecutor(listenerObj, method));
					map.put(handler.priority(), executor);
					this.handlers.put(event, map);
				}
			}
		}
	}

	@Override
	public <T extends Event> T callEventAsync(T event) {
		throw new UnsupportedOperationException("callEventAsync in class EventManager is not supported yet.");
	}

	private class EventExecutor implements Runnable {
		private final Object invoker;
		private final Method method;
		private Event event;

		private EventExecutor(Object invoker, Method method) {
			this.invoker = invoker;
			this.method = method;
		}

		@Override
		public void run() {
			try {
				this.method.invoke(invoker, event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				ex.printStackTrace();
			}
		}
	}
}
