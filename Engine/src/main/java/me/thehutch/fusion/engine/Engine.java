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
package me.thehutch.fusion.engine;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.thehutch.fusion.api.IEngine;
import me.thehutch.fusion.api.component.ComponentSystem;
import me.thehutch.fusion.api.scheduler.TaskPriority;
import me.thehutch.fusion.engine.event.EventManager;
import me.thehutch.fusion.engine.filesystem.FileSystem;
import me.thehutch.fusion.engine.scheduler.Scheduler;

/**
 * @author thehutch
 */
public abstract class Engine implements IEngine {
	// Engine Version
	public static final int ENGINE_MAJOR_VERSION = 0;
	public static final int ENGINE_MINOR_VERSION = 0;
	public static final int ENGINE_BUILD_VERSION = 1;
	public static final String ENGINE_VERSION = ENGINE_MAJOR_VERSION + "." + ENGINE_MINOR_VERSION + "." + ENGINE_BUILD_VERSION;
	private static final Logger LOGGER = Logger.getLogger("Fusion-" + ENGINE_VERSION);
	private static final long TICKS_PER_SECOND = 60L;
	private final EventManager eventManager;
	private final ComponentSystem system;
	private final FileSystem fileSystem;
	private final Scheduler scheduler;
	private final boolean debug;

	protected Engine(Application application) {
		// Initialise debug status
		this.debug = application.debug;

		// Create the engine scheduler
		this.scheduler = new Scheduler(TICKS_PER_SECOND);

		// Create the event manager
		this.eventManager = new EventManager();

		// Create the file system
		this.fileSystem = new FileSystem();

		// Create the component system
		this.system = new ComponentSystem();
	}

	public void initialise() {
		// Schedule the component system task
		this.scheduler.scheduleSyncRepeatingTask(system::process, TaskPriority.CRITICAL, 0L, 1L);

		// Initialise the component system
		this.system.initialise();
	}

	@Override
	public final String getVersion() {
		return ENGINE_VERSION;
	}

	@Override
	public final Scheduler getScheduler() {
		return scheduler;
	}

	@Override
	public final EventManager getEventManager() {
		return eventManager;
	}

	@Override
	public final FileSystem getFileSystem() {
		return fileSystem;
	}

	@Override
	public final ComponentSystem getComponentSystem() {
		return system;
	}

	@Override
	public final boolean debugMode() {
		return debug;
	}

	@Override
	public void stop(String reason) {
		// Terminate the Scheduler
		getScheduler().stop();

		getLogger().log(Level.INFO, "Exiting Engine: {0}", reason);
	}

	public static Logger getLogger() {
		return LOGGER;
	}
}
