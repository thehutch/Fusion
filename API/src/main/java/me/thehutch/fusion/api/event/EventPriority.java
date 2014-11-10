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

/**
 * @author thehutch
 */
public enum EventPriority {
	/**
	 * High priority event, called before all other priorities.
	 */
	HIGH(0),
	/**
	 * Medium priority event, called after {@link #HIGH} priority.
	 */
	MEDIUM(1),
	/**
	 * Low priority event, called after {@link #MEDIUM} priority.
	 */
	LOW(2);
	private static final EventPriority[] VALUES = EventPriority.values();
	private final int priority;

	/**
	 * Default constructor for {@link EventPriority}.
	 *
	 * @param priority The level of priority
	 */
	private EventPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Gets the priority level of this priority. The lower the value, the more priority it has.
	 *
	 * @return The value of this priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Returns the {@link EventPriority} at the given index based on their ordinal.
	 *
	 * @param index The index of the priority
	 *
	 * @return The {@link EventPriority} at the given index
	 */
	public static EventPriority getByIndex(int index) {
		if (index < 0 || index >= VALUES.length) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return VALUES[index];
	}
}
