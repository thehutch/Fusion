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
/*
 * This file is part of FusionAPI.
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
	 * Called before all handlers.
	 */
	LOW(6, false),
	/**
	 * Called before all handlers.<\br> Is called even when event has been
	 * cancelled.
	 */
	LOW_IGNORE_CANCELLED(5, true),
	/**
	 * Called after {@link #LOW} handlers.
	 */
	MEDIUM(4, false),
	/**
	 * Called after {@link #LOW} handlers.<\br> Is called even when event has
	 * been cancelled.
	 */
	MEDIUM_IGNORE_CANCELLED(3, true),
	/**
	 * Called after {@link #MEDIUM} handlers.
	 */
	HIGH(2, false),
	/**
	 * Called after {@link #MEDIUM} handlers.<\br> Is called even when event has
	 * been cancelled.
	 */
	HIGH_IGNORE_CANCELLED(1, true),
	/**
	 * Called after all handlers.<\br> Is called even when event has been
	 * cancelled.<\br> Should only be used to determine the result of the event.
	 */
	MONITOR(0, true);

	private final int priority;
	private final boolean ignoreCancelled;

	private EventPriority(int priority, boolean ignoreCancelled) {
		this.priority = priority;
		this.ignoreCancelled = ignoreCancelled;
	}

	/**
	 * Gets the priority level of this priority.
	 * <br/>
	 * The lower the value, the more priority it has.
	 *
	 * @return The value of this priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @return True if this priority ignores the event being cancelled
	 */
	public boolean ignoresCancelled() {
		return ignoreCancelled;
	}
}
