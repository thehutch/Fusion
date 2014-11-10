/*
 * This file is part of Engine, licensed under the Apache 2.0 License.
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
package me.thehutch.fusion.engine.scheduler;

import java.util.concurrent.atomic.AtomicBoolean;
import me.thehutch.fusion.api.scheduler.TaskPriority;

/**
 * @author thehutch
 */
public final class Task implements Comparable<Task> {
	private final Runnable runnable;
	private final TaskPriority priority;
	private final AtomicBoolean isAlive;
	private final long creationTime;
	private final boolean isAsync;
	private final long period;
	private final int id;
	private long tick;

	/**
	 * Default constructor for {@link Task}.
	 *
	 * @param id           The id of the task
	 * @param runnable     The task runnable function
	 * @param priority     The priority of the task
	 * @param async        True if the task is executed asynchronously
	 * @param creationTime A timestamp of this task's creation
	 * @param delay        The number of ticks before task execution
	 * @param period       The number of ticks between task execution
	 */
	public Task(int id, Runnable runnable, TaskPriority priority, boolean async, long creationTime, long delay, long period) {
		this.id = id;
		this.runnable = runnable;
		this.priority = priority;
		this.isAsync = async;
		this.period = period;
		this.creationTime = creationTime;
		this.isAlive = new AtomicBoolean(true);
		this.tick = creationTime + delay;
	}

	/**
	 * @return The task runnable function
	 */
	public Runnable getRunnable() {
		return runnable;
	}

	/**
	 * @return The priority of the task
	 */
	public TaskPriority getPriority() {
		return priority;
	}

	/**
	 * @return True if the task is still running
	 */
	public boolean isAlive() {
		return isAlive.get();
	}

	/**
	 * @return A timestamp of this task's creation
	 */
	public long getCreationTime() {
		return creationTime;
	}

	/**
	 * @return True if this task is executed asynchronously
	 */
	public boolean isAsync() {
		return isAsync;
	}

	/**
	 * @return The number of ticks between task executions
	 */
	public long getPeriod() {
		return period;
	}

	/**
	 * @return The id of the task
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return True if this task repeats
	 */
	public boolean isRepeating() {
		return period > 0;
	}

	/**
	 * @return The tick when this task is next executed
	 */
	public long getTime() {
		return tick;
	}

	/**
	 * Sets the next tick when this task is executed.
	 *
	 * @param tick The next execution tick
	 */
	public void setTime(long tick) {
		this.tick = tick;
	}

	/**
	 * Cancels this task
	 */
	public void cancel() {
		this.isAlive.set(false);
	}

	/**
	 * Compares this task with the given task.
	 *
	 * @param task The task to compare this task with
	 *
	 * @return Greater than 1 if this task has a greater priority
	 *         than the given task else less than one
	 */
	@Override
	public int compareTo(Task task) {
		if (getTime() == task.getTime()) {
			return (int) (getPriority() == task.getPriority() ? getCreationTime() - task.getCreationTime() : task.getPriority().getMaxDeferred() - getPriority().getMaxDeferred());
		}
		return getTime() < task.getTime() ? 1 : -1;
	}
}
