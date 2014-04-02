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
package me.thehutch.fusion.engine;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.thehutch.fusion.api.IEngine;
import me.thehutch.fusion.api.filesystem.IFileSystem;
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
	private final EventManager eventManager;
	private final FileSystem fileSystem;
	private final Scheduler scheduler;
	private final boolean debug;

	protected Engine(Application application) {
		// Initialise Debug Status
		this.debug = application.debug;
		this.scheduler = new Scheduler(60L);
		this.eventManager = new EventManager();
		this.fileSystem = new FileSystem();
	}

	@Override
	public final String getVersion() {
		return ENGINE_VERSION;
	}

	@Override
	public final Logger getLogger() {
		return LOGGER;
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
	public IFileSystem getFileSystem() {
		return fileSystem;
	}

	@Override
	public final boolean debugMode() {
		return debug;
	}

	@Override
	public final void stop(String reason) {
		// Terminate the Scheduler
		getScheduler().stop();

		getLogger().log(Level.INFO, "Exiting Engine: {0}", reason);

		System.exit(0);
	}
}
