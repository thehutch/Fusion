/*
 * This file is part of Engine.
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

import me.thehutch.fusion.api.scheduler.TaskPriority;

/**
 * @author thehutch
 */
public class Task implements Comparable<Task> {
	private final TaskPriority priority;
	private final Runnable executor;
	private final boolean parallel;
	private final long creationTime;
	private final long period;
	private final long delay;
	private final int id;
	private boolean alive;
	private long time;

	public Task(int id, Runnable executor, TaskPriority priority, long delay, long period, boolean parallel) {
		this.id = id;
		this.priority = priority;
		this.executor = executor;
		this.parallel = parallel;
		this.period = period;
		this.delay = delay;
		this.alive = true;
		this.time = 0L;
		this.creationTime = System.currentTimeMillis();
	}

	public void execute() {
		this.executor.run();
	}

	public TaskPriority getPriority() {
		return priority;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public long getPeriod() {
		return period;
	}

	public long getDelay() {
		return delay;
	}

	public int getId() {
		return id;
	}

	public void cancel() {
		this.alive = false;
	}

	public void setTickTime(long tick, boolean isOverloaded) {
		this.time = tick + (isOverloaded ? getPriority().getMaxDeferred() : 0L);
	}

	public long getTickTime() {
		return time;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isParallel() {
		return parallel;
	}

	public boolean isRepeating() {
		return period > 0;
	}

	@Override
	public int compareTo(Task task) {
		if (getTickTime() == task.getTickTime()) {
			return (int) (getPriority() == task.getPriority() ? getCreationTime() - task.getCreationTime() : task.getPriority().getMaxDeferred() - getPriority().getMaxDeferred());
		}
		return getTickTime() < task.getTickTime() ? 1 : -1;
	}
}
