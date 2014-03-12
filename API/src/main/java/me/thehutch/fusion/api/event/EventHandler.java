/*
 * This file is part of API.
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author thehutch
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
	/**
	 * The priority of the event, the higher the priority the earlier the event is called.
	 *
	 * @return The priority of this event handler
	 */
	EventPriority priority() default EventPriority.MEDIUM;

	/**
	 * Set to true to ignore if the event has already been cancelled.
	 *
	 * @return True if the handler should ignore the cancellation state
	 */
	boolean ignoreCancelled() default false;
}
