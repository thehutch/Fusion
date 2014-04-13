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

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import me.thehutch.fusion.api.scheduler.IScheduler;
import me.thehutch.fusion.api.scheduler.TaskPriority;

/**
 * @author thehutch
 */
public class Scheduler implements IScheduler {
	private static final float OVERLOAD_FACTOR = 1.25f;
	private static final long NANOSECOND_TO_SECOND = 1000000000L;
	private static final AtomicInteger TASK_ID_COUNTER = new AtomicInteger(0);
	private final Queue<Task> currentTasks = new PriorityQueue<>();
	private final Queue<Task> pendingQueue = new LinkedBlockingQueue<>();
	private final SchedulerService service;
	private final long ticksPerSecond;
	private long diffTimePerTick;
	private boolean overloaded;
	private boolean active;
	private long upTime;
	private long tick;

	public Scheduler(long ticksPerSecond) {
		this.service = new SchedulerService(Runtime.getRuntime().availableProcessors() - 1);
		this.ticksPerSecond = ticksPerSecond;
		this.overloaded = false;
		this.active = true;
		this.upTime = 0L;
		this.tick = ticksPerSecond;
	}

	public void execute() {
		long diffTime;
		long frameClock;
		long startClock = System.nanoTime();

		do {
			frameClock = System.nanoTime();

			boolean isExecuted = true;
			while (isExecuted && !currentTasks.isEmpty()) {
				final Task task = currentTasks.peek();
				isExecuted = task.getTickTime() <= upTime;
				if (isExecuted && task.isAlive()) {
					if (task.isParallel()) {
						this.service.push(task);
					} else {
						task.execute();
						task.setTickTime(upTime + task.getPeriod(), overloaded);

						if (task.isAlive() && task.isRepeating()) {
							this.pendingQueue.add(task);
						}
					}
					this.currentTasks.remove();
				} else if (!task.isAlive()) {
					this.currentTasks.remove();
				}
			}

			this.diffTimePerTick = System.nanoTime() - frameClock;

			if (++upTime == tick) {
				this.tick += ticksPerSecond;

				diffTime = System.nanoTime() - startClock;

				this.overloaded = diffTime > NANOSECOND_TO_SECOND * OVERLOAD_FACTOR;

				startClock = System.nanoTime();
			}

			// Add new tasks
			while (!pendingQueue.isEmpty()) {
				this.currentTasks.add(pendingQueue.poll());
			}

			// Exit the scheduler if no tasks to execute
			if (currentTasks.isEmpty()) {
				stop();
			}
		} while (active);

		// Wait until all threads have finished
		this.service.joinAll();
	}

	public void stop() {
		this.active = false;
	}

	/**
	 * Gets the number of seconds since the last frame.
	 *
	 * @return Frame time in seconds
	 */
	public float getDelta() {
		return diffTimePerTick / (float) NANOSECOND_TO_SECOND;
	}

	@Override
	public int scheduleSyncTask(Runnable task, TaskPriority priority) {
		return scheduleSyncRepeatingTask(task, priority, 0L, 0L);
	}

	@Override
	public int scheduleSyncDelayedTask(Runnable task, TaskPriority priority, long delay) {
		return scheduleSyncRepeatingTask(task, priority, delay, 0L);
	}

	@Override
	public int scheduleSyncRepeatingTask(Runnable task, TaskPriority priority, long delay, long period) {
		return addTask(task, priority, delay, period, false);
	}

	@Override
	public int scheduleAsyncTask(Runnable task, TaskPriority priority) {
		return scheduleAsyncDelayedTask(task, priority, 0L);
	}

	@Override
	public int scheduleAsyncDelayedTask(Runnable task, TaskPriority priority, long delay) {
		return addTask(task, priority, delay, 0L, true);
	}

	@Override
	public void cancelTask(int taskId) {
		this.currentTasks.stream().filter((task) -> (task.getId() == taskId)).forEach((task) -> {
			task.cancel();
		});
	}

	public void cancelAllTasks() {
		this.currentTasks.stream().forEach((task) -> {
			task.cancel();
		});
	}

	private int addTask(Runnable executor, TaskPriority priority, long delay, long period, boolean isParallel) {
		final int taskId = TASK_ID_COUNTER.getAndIncrement();
		final Task task = new Task(taskId, executor, priority, delay, period, isParallel);
		task.setTickTime(delay + upTime, overloaded);
		this.pendingQueue.add(task);
		return taskId;
	}
}
