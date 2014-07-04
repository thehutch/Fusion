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
public abstract class Event {
	private boolean isCancelled = false;

	public Event() {
	}

	/**
	 * Returns true if the event has been cancelled.
	 *
	 * @return True if the event is cancelled
	 */
	public final boolean isCancelled() {
		return isCancelled;
	}

	/**
	 * Sets the cancellation state of this event. If the event can not be cancelled then
	 * the new state is ignored.
	 *
	 * @param cancelled True if event should be cancelled
	 */
	public final void setCancelled(boolean cancelled) {
		if (canCancel()) {
			this.isCancelled = cancelled;
		}
	}

	/**
	 * Can be overrided to change whether this event can be cancelled.
	 *
	 * @return If true this event can be cancelled
	 */
	protected boolean canCancel() {
		return true;
	}
}
