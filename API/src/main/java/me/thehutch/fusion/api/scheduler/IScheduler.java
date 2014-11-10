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
public interface IScheduler {
	/**
	 * Schedules a one off task to be executed as soon as possible.
	 * This task will be executed synchronously.
	 *
	 * @param task     Task to be executed
	 * @param priority Priority of the task
	 *
	 * @return The id of the task which has been scheduled
	 */
	public int invoke(Runnable task, TaskPriority priority);

	/**
	 * Schedules a one off task and executed after the delay period.
	 * This task will be executed synchronously.
	 *
	 * @param task     Task to be executed
	 * @param priority Priority of the task
	 * @param delay    Delay in ticks before executing the task
	 *
	 * @return The id of the task which has been scheduled
	 */
	public int invokeDelayed(Runnable task, TaskPriority priority, long delay);

	/**
	 * Schedules a delayed task which will repeat every given number of ticks.
	 * This task will be executed synchronously.
	 *
	 * @param task     Task to be executed
	 * @param priority Priority of the task
	 * @param delay    Delay in ticks before executing the task
	 * @param period   The ticks between executions of this task
	 *
	 * @return The id of the task which has been scheduled
	 */
	public int invokeRepeating(Runnable task, TaskPriority priority, long delay, long period);

	/**
	 * Schedules a once off short lived task to occur as soon as possible.
	 * This task will be executed asynchronously.
	 *
	 * @param task     Task to be executed
	 * @param priority Priority of the task
	 *
	 * @return The id of the task which has been scheduled
	 */
	public int invokeAsync(Runnable task, TaskPriority priority);

	/**
	 * Schedules a once off short lived task to occur after the delay period.
	 * This task will be executed asynchronously.
	 *
	 * @param task     Task to be executed
	 * @param priority Priority of the task
	 * @param delay    Delay in ticks before executing the task
	 *
	 * @return The id of the task which has been scheduled
	 */
	public int invokeDelayedAsync(Runnable task, TaskPriority priority, long delay);

	/**
	 * Cancels the task associated with this id.
	 *
	 * @param taskId Id of the task to be cancelled
	 */
	public void cancelTask(int taskId);
}
