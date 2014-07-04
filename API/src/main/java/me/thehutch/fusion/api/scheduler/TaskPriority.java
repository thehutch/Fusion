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
package me.thehutch.fusion.api.scheduler;

/**
 * @author thehutch
 */
public enum TaskPriority {
	/**
	 * Priority for tasks which may not be deferred.
	 */
	CRITICAL(0L),
	/**
	 * Priority for tasks which can be deferred by up to 50ms when under load.
	 */
	HIGHEST(50L),
	/**
	 * Priority for tasks which can be deferred by up to 150ms when under load.
	 */
	HIGH(150L),
	/**
	 * Priority for tasks which can be deferred by up to 500ms when under load.
	 */
	MEDIUM(500L),
	/**
	 * Priority for tasks which can be deferred by up to 1500ms when under load.
	 */
	LOW(1500L),
	/**
	 * Priority for tasks which can be deferred by up to 10000ms when under load.
	 */
	LOWEST(10000L);
	private final long maxDeferred;

	/**
	 * Creates a TaskPriority instance which sets the maximum time that a task can be deferred.
	 *
	 * @param maxDeferred The maximum delay for this task
	 */
	private TaskPriority(long maxDeferred) {
		this.maxDeferred = maxDeferred;
	}

	/**
	 * Gets the maximum time that the task can be deferred.
	 *
	 * @return The maximum deferred time
	 */
	public long getMaxDeferred() {
		return maxDeferred;
	}
}
