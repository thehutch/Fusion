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
package me.thehutch.fusion.api.event;

import java.util.function.Consumer;

/**
 * @author thehutch
 */
public interface IEventManager {
	/**
	 * Executes the event synchronously.
	 *
	 * @param <T>   The type of event being invoked
	 * @param event The event to invoke
	 */
	public <T extends Event> void invoke(T event);

	/**
	 * Executes the event synchronously after the delay period.
	 *
	 * @param <T>   The type of event being invoked
	 * @param event The event to invoke
	 * @param delay The number of ticks to delay the task by
	 */
	public <T extends Event> void invokeDelayed(T event, long delay);

	/**
	 * Executes the event asynchronously.
	 *
	 * @param <T>   The type of event being invoked
	 * @param event The event to invoke
	 */
	public <T extends Event> void invokeAsync(T event);

	/**
	 * Executes the event asynchronously and executes the callback
	 * after the event has been handled.
	 *
	 * @param <T>      The type of event being invoked
	 * @param event    The event to invoke
	 * @param callback The callback to be executed
	 */
	public <T extends Event> void invokeAsync(T event, Consumer<T> callback);

	/**
	 * Registers the event handler with the given priority and ignore flag.
	 *
	 * @param <T>             The type of event being registered
	 * @param eventClass      The event class to register
	 * @param handler         The function which handles the event
	 * @param priority        The priority of the event
	 * @param ignoreCancelled If the handler ignores the cancellation state of the event
	 */
	public <T extends Event> void register(Consumer<T> handler, Class<T> eventClass, EventPriority priority, boolean ignoreCancelled);

	/**
	 * Unregisters the event handler from the event manager.
	 *
	 * @param <T>        The type of event
	 * @param handler    The handler to unregister
	 * @param eventClass The event class to unregister
	 */
	public <T extends Event> void unregister(Consumer<T> handler, Class<T> eventClass);
}
