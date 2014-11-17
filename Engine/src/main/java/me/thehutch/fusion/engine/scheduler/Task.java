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
	private final Runnable mRunnable;
	private final TaskPriority mPriority;
	private final AtomicBoolean mIsAlive;
	private final long mCreationTime;
	private final boolean mIsAsync;
	private final long mPeriod;
	private final int mID;
	private long mTick;

	/**
	 * Default constructor for {@link Task}.
	 *
	 * @param id           The mID of the task
	 * @param runnable     The task mRunnable function
	 * @param priority     The mPriority of the task
	 * @param async        True if the task is executed asynchronously
	 * @param creationTime A timestamp of this task's creation
	 * @param delay        The number of ticks before task execution
	 * @param period       The number of ticks between task execution
	 */
	public Task(int id, Runnable runnable, TaskPriority priority, boolean async, long creationTime, long delay, long period) {
		mID = id;
		mRunnable = runnable;
		mPriority = priority;
		mIsAsync = async;
		mPeriod = period;
		mCreationTime = creationTime;
		mIsAlive = new AtomicBoolean(true);
		mTick = creationTime + delay;
	}

	/**
	 * @return The task mRunnable function
	 */
	public Runnable getRunnable() {
		return mRunnable;
	}

	/**
	 * @return The mPriority of the task
	 */
	public TaskPriority getPriority() {
		return mPriority;
	}

	/**
	 * @return True if the task is still running
	 */
	public boolean isAlive() {
		return mIsAlive.get();
	}

	/**
	 * @return A timestamp of this task's creation
	 */
	public long getCreationTime() {
		return mCreationTime;
	}

	/**
	 * @return True if this task is executed asynchronously
	 */
	public boolean isAsync() {
		return mIsAsync;
	}

	/**
	 * @return The number of ticks between task executions
	 */
	public long getPeriod() {
		return mPeriod;
	}

	/**
	 * @return The mID of the task
	 */
	public int getId() {
		return mID;
	}

	/**
	 * @return True if this task repeats
	 */
	public boolean isRepeating() {
		return mPeriod > 0;
	}

	/**
	 * @return The tick when this task is next executed
	 */
	public long getTime() {
		return mTick;
	}

	/**
	 * Sets the next tick when this task is executed.
	 *
	 * @param tick The next execution tick
	 */
	public void setTime(long tick) {
		mTick = tick;
	}

	/**
	 * Cancels this task
	 */
	public void cancel() {
		mIsAlive.set(false);
	}

	/**
	 * Compares this task with the given task.
	 *
	 * @param task The task to compare this task with
	 *
	 * @return Greater than 1 if this task has a greater mPriority
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
