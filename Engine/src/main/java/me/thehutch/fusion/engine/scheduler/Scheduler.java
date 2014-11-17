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

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Predicate;
import me.thehutch.fusion.api.scheduler.IScheduler;
import me.thehutch.fusion.api.scheduler.TaskPriority;

/**
 * @author thehutch
 */
public final class Scheduler implements IScheduler {
	private static final float OVERLOAD_FACTOR = 1.5f;
	private static final long SECOND_AS_MILLISECOND = 1000L;
	private static final AtomicInteger TASK_ID_COUNTER = new AtomicInteger(0);
	private final Queue<Task> mCurrentTasks;
	private final Queue<Task> mPendingTasks;
	private final ExecutorService mExecutor;
	private final AtomicBoolean mIsOverloaded;
	private final AtomicBoolean mIsActive;
	private final AtomicLong mDelta;
	private final long mTicksPerSecond;
	private final long mTimePerTick;
	private long mNextTick;
	private long mUptime;

	/**
	 * The default constructor for {@link Scheduler}.
	 *
	 * @param ticksPerSecond The number of scheduler updates per second
	 */
	public Scheduler(long ticksPerSecond) {
		mCurrentTasks = new PriorityQueue<>();
		mPendingTasks = new ArrayDeque<>();
		mDelta = new AtomicLong(0L);
		mIsOverloaded = new AtomicBoolean(false);
		mIsActive = new AtomicBoolean(true);
		mTicksPerSecond = ticksPerSecond;
		mTimePerTick = SECOND_AS_MILLISECOND / ticksPerSecond;
		mNextTick = mTimePerTick;
		mUptime = 0L;

		final int availableCores = Runtime.getRuntime().availableProcessors();
		mExecutor = Executors.newFixedThreadPool(availableCores > 0 ? availableCores : 1);
	}

	/**
	 * Updates the scheduler and executes any tasks either synchronously or asynchronously
	 * and requeues them if they are repeated.
	 */
	public void execute() {
		long timeFrame = System.currentTimeMillis();
		long timeTick;

		do {
			timeTick = System.currentTimeMillis();

			// At the start of the tickm add all tasks which
			// have been marked to be added to the loop to
			// prevent any race condition or lock mechanism
			mCurrentTasks.addAll(mPendingTasks);
			mPendingTasks.clear();

			// Execute the tick of the scheduler and calculate
			// the time the scheduler spent executing
			if (!mCurrentTasks.isEmpty()) {
				// Seperate all the tasks which need to be executed asynchronously
				// from those which need to be executed synchronously
				final Queue<Task> tasks = new ArrayDeque<>();
				boolean hasExecuted;
				do {
					final Task task = mCurrentTasks.peek();
					final boolean isAlive = task.isAlive();

					hasExecuted = task.getTime() <= mUptime;

					if (hasExecuted && isAlive) {
						if (task.isAsync()) {
							mExecutor.submit(task.getRunnable());
						} else {
							tasks.add(task);
						}
						mCurrentTasks.remove();
					} else if (!isAlive) {
						mCurrentTasks.remove();
					}
				} while (!mCurrentTasks.isEmpty() && hasExecuted);

				// Generate a stream on which all tasks which requested
				// to be executed synchronously, executes with default order
				final Consumer<Task> consumer = (task) -> {
					// Execute the task runnable
					try {
						task.getRunnable().run();
					} catch (Throwable ex) {
						ex.printStackTrace();
					}

					// Update the task tick time
					task.setTime(mUptime + task.getPeriod() + (mIsOverloaded.get() ? convertToTick(task.getPriority().getMaxDeferred(), TimeUnit.MILLISECONDS) : 0L));

					// If the task is repeating then add it back to the scheduler
					if (task.isRepeating() && task.isAlive()) {
						mPendingTasks.add(task);
					}
				};
				tasks.forEach(consumer);
			}

			timeTick = System.currentTimeMillis() - timeTick;

			// Update the delta
			mDelta.set(timeTick);

			// Advance the tick counter to one and check if
			// the scheduler has been advanced a frame
			if (++mUptime == mNextTick) {
				timeTick = System.currentTimeMillis() - timeFrame;

				mNextTick += mTicksPerSecond;

				mIsOverloaded.set(timeTick > SECOND_AS_MILLISECOND * OVERLOAD_FACTOR);

				if (timeTick < SECOND_AS_MILLISECOND) {
					try {
						Thread.sleep(SECOND_AS_MILLISECOND - timeTick);
					} catch (InterruptedException ex) {
						throw new IllegalStateException("Exception trying to sleep current thread", ex);

					}
				}
				timeFrame = System.currentTimeMillis();
			} else if (timeTick < mTimePerTick) {
				try {
					Thread.sleep(mTimePerTick - timeTick);
				} catch (InterruptedException ex) {
					throw new IllegalStateException("Exception trying to sleep current thread", ex);
				}
			}
		} while (mIsActive.get());

		// Remove all reference to old tasks to ensure GC collects
		// thems when the service has been stopped
		mCurrentTasks.clear();
		mPendingTasks.clear();

		// Shutdown and wait for the executor to complete async tasks
		mExecutor.shutdown();
	}

	/**
	 * Shuts down the scheduler.
	 */
	public void shutdown() {
		mIsActive.set(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int invoke(Runnable task, TaskPriority priority) {
		return addTask(task, priority, 0L, 0L, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int invokeDelayed(Runnable task, TaskPriority priority, long delay) {
		return addTask(task, priority, delay, 0L, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int invokeRepeating(Runnable task, TaskPriority priority, long delay, long period) {
		return addTask(task, priority, delay, period, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int invokeAsync(Runnable task, TaskPriority priority) {
		return addTask(task, priority, 0L, 0L, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int invokeDelayedAsync(Runnable task, TaskPriority priority, long delay) {
		return addTask(task, priority, delay, 0L, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelTask(int taskId) {
		final Predicate<Task> predicate = task -> (task.getId() == taskId);
		mCurrentTasks.stream()
			.filter(predicate)
			.forEach(Task::cancel);
		mPendingTasks.removeIf(predicate);
	}

	@Override
	public float getDelta() {
		return mDelta.get() / 1000.0f;
	}

	/**
	 * Internal method to add a new task to the scheduler.
	 *
	 * @param executor The task runnable to execute
	 * @param priority The priority of the task
	 * @param delay    The delay in ticks before executing the task
	 * @param period   The period in ticks between each task execution
	 * @param isAsync  True if the task is executed asynchronously
	 *
	 * @return The id of the task
	 */
	private int addTask(Runnable executor, TaskPriority priority, long delay, long period, boolean isAsync) {
		final int taskId = TASK_ID_COUNTER.getAndIncrement();
		final Task task = new Task(taskId, executor, priority, isAsync, mUptime, delay, period);
		mPendingTasks.add(task);
		return taskId;
	}

	/**
	 * Calculate the tick representation of a time.
	 *
	 * @param time     The time to convert to ticks
	 * @param timeUnit The time unit of the given time
	 *
	 * @return The tick representation of the time
	 */
	private long convertToTick(long time, TimeUnit timeUnit) {
		return timeUnit.toMillis(time) / mTicksPerSecond;
	}
}
