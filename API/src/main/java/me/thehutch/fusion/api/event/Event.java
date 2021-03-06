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
	private final boolean mCanCancel;
	private boolean mIsCancelled;

	/**
	 * Default constructor for {@link Event}.
	 *
	 * @param canCancel True if this event can be cancelled
	 */
	public Event(boolean canCancel) {
		mCanCancel = canCancel;
		mIsCancelled = false;
	}

	/**
	 * Returns true if the event has been cancelled.
	 *
	 * @return True if the event is cancelled
	 */
	public final boolean isCancelled() {
		return mIsCancelled;
	}

	/**
	 * Sets the cancellation state of this event. If the event can not be cancelled then
	 * the new state is ignored.
	 *
	 * @param cancelled True if event should be cancelled
	 */
	public final void setCancelled(boolean cancelled) {
		mIsCancelled = cancelled && mCanCancel;
	}
}
