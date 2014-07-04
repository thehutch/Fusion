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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author thehutch
 */
public class SchedulerService {
	private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
	private final AtomicBoolean running = new AtomicBoolean(true);
	private final Object lock = new Object();
	private final List<Thread> threads;

	public SchedulerService(int threadSize) {
		this.threads = new ArrayList<>(threadSize);
		for (int i = 0; i < threadSize; ++i) {
			this.threads.add(i, new Thread(this::handleRunnable));
		}
	}

	public void joinAll() {
		try {
			this.running.set(false);
			for (Thread thread : threads) {
				thread.join();
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public void push(Task task) {
		synchronized (lock) {
			this.taskQueue.add(task);
		}
	}

	private void handleRunnable() {
		Task task;
		do {
			try {
				synchronized (lock) {
					task = taskQueue.take();
				}
				if (task != null) {
					task.execute();
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		} while (running.get());
	}
}
