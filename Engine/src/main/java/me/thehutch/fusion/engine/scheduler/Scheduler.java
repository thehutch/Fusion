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
/*
 * This file is part of FusionEngine.
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

import java.util.ArrayDeque;
import java.util.Comparator;
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
	public static final float OVERLOAD_FACTOR = 1.5f;
	public static final long MILLISECOND_TO_SECOND = 1000L;
	private static final AtomicInteger TASK_ID_COUNTER = new AtomicInteger(0);
	private final PriorityQueue<Task> currentTasks = new PriorityQueue<>(4, new TaskQueueComparator());
	private final Queue<Task> pendingQueue = new LinkedBlockingQueue<>();
	private final SchedulerService service;
	private final long ticksPerSecond;
	private boolean overloaded;
	private boolean active;
	private long upTime;
	private long tick;

	public Scheduler(long ticksPersSecond) {
		this.service = new SchedulerService(Runtime.getRuntime().availableProcessors() - 1);
		this.ticksPerSecond = ticksPersSecond;
		this.overloaded = false;
		this.active = true;
		this.upTime = 0L;
		this.tick = ticksPerSecond;
	}

	public void execute() {
		final Queue<Task> asyncTasks = new ArrayDeque<>();
		final Queue<Task> syncTasks = new ArrayDeque<>();

		final long timePerTick = MILLISECOND_TO_SECOND / ticksPerSecond;

		long diffTime;
		long diffTimePerTick;

		long frameClock;
		long startClock = System.currentTimeMillis();

		do {
			frameClock = System.currentTimeMillis();
			if (!currentTasks.isEmpty()) {
				getReadyTasks(asyncTasks, syncTasks);
			}

			while (!asyncTasks.isEmpty()) {
				this.service.push(asyncTasks.remove());
			}

			while (!syncTasks.isEmpty()) {
				final Task task = syncTasks.poll();
				task.execute();
				task.setTickTime(getUpTime() + task.getPeriod(), isOverloaded());

				if (task.isAlive() && task.isRepeating()) {
					this.currentTasks.add(task);
				}
			}

			diffTimePerTick = System.currentTimeMillis() - frameClock;

			if (++upTime == tick) {
				this.tick += ticksPerSecond;

				diffTime = System.currentTimeMillis() - startClock;

				this.overloaded = diffTime > MILLISECOND_TO_SECOND * OVERLOAD_FACTOR;

				if (diffTime < MILLISECOND_TO_SECOND) {
					try {
						Thread.sleep(timePerTick - diffTimePerTick);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
				startClock = System.currentTimeMillis();
			} else if (diffTimePerTick < timePerTick) {
				try {
					Thread.sleep(timePerTick - diffTimePerTick);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}

			// Add new task back
			while (!pendingQueue.isEmpty()) {
				this.currentTasks.add(pendingQueue.poll());
			}

			// Exit the scheduler if no tasks to execute
			if (currentTasks.isEmpty()) {
				stop();
			}
		} while (isActive());

		// Wait until all threads have finished
		this.service.joinAll();
	}

	public void stop() {
		this.active = false;
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
		for (final Task task : currentTasks) {
			if (task.getId() == taskId) {
				task.cancel();
			}
		}
	}

	public void cancelAllTasks() {
		for (final Task task : currentTasks) {
			task.cancel();
		}
	}

	public long getUpTime() {
		return upTime;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isOverloaded() {
		return overloaded;
	}

	private void getReadyTasks(Queue<Task> parallelTasks, Queue<Task> syncedTasks) {
		boolean isExecuted;
		do {
			final Task task = currentTasks.element();
			isExecuted = task.getTickTime() <= getUpTime();

			if (isExecuted && task.isAlive()) {
				if (task.isParallel()) {
					parallelTasks.add(task);
				} else {
					syncedTasks.add(task);
				}
				this.currentTasks.remove();
			} else if (!task.isAlive()) {
				this.currentTasks.remove();
			}
		} while (isExecuted && !currentTasks.isEmpty());
	}

	private int addTask(Runnable executor, TaskPriority priority, long delay, long period, boolean isParallel) {
		final int taskId = TASK_ID_COUNTER.getAndIncrement();
		final Task task = new Task(taskId, executor, priority, delay, period, isParallel);
		task.setTickTime(delay + getUpTime(), isOverloaded());
		this.pendingQueue.add(task);
		return taskId;
	}

	private class TaskQueueComparator implements Comparator<Task> {
		@Override
		public int compare(Task t1, Task t2) {
			return t1.hasLessPriority(t2) ? -1 : 1;
		}
	}
}
