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
package me.thehutch.fusion.api.scheduler;

/**
 * @author thehutch
 */
public interface IScheduler {
	/**
	 * Schedules a one off task.
	 * This task will be executed by the main thread.
	 *
	 * @param task Task to be executed
	 * @param priority Priority of the task
	 *
	 * @return The id of the task which has been scheduled
	 */
	public int scheduleSyncTask(Runnable task, TaskPriority priority);

	/**
	 * Schedules a one off task.
	 * This task will be executed by the main thread.
	 *
	 * @param task Task to be executed
	 * @param priority Priority of the task
	 * @param delay Delay in ticks before executing the task
	 *
	 * @return The id of the task which has been scheduled
	 */
	public int scheduleSyncDelayedTask(Runnable task, TaskPriority priority, long delay);

	/**
	 * Schedules a task which will be executed at a ticking interval of period.
	 * This task will be executed by the main thread.
	 *
	 * @param task Task to be executed
	 * @param priority Priority of the task
	 * @param delay Delay in ticks before executing the task
	 * @param period The ticks between executions of this task
	 *
	 * @return The id of the task which has been scheduled
	 */
	public int scheduleSyncRepeatingTask(Runnable task, TaskPriority priority, long delay, long period);

	/**
	 * Schedules a once off short lived task to occur as soon as possible. This
	 * task will be executed by a thread managed by the scheduler.
	 *
	 * @param task Task to be executed
	 * @param priority Priority of the task
	 *
	 * @return The id of the task which has been scheduled
	 */
	public int scheduleAsyncTask(Runnable task, TaskPriority priority);

	/**
	 * Schedules a once off short lived task to occur as soon as possible. This
	 * task will be executed by a thread managed by the scheduler.
	 *
	 * @param task Task to be executed
	 * @param priority Priority of the task
	 * @param delay Delay in ticks before executing the task
	 *
	 * @return The id of the task which has been scheduled
	 */
	public int scheduleAsyncDelayedTask(Runnable task, TaskPriority priority, long delay);

	/**
	 * Cancels the task associated with this id.
	 *
	 * @param taskId Id of the task to be cancelled
	 */
	public void cancelTask(int taskId);
}
