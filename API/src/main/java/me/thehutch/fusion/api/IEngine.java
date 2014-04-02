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
package me.thehutch.fusion.api;

import java.util.logging.Logger;
import me.thehutch.fusion.api.event.IEventManager;
import me.thehutch.fusion.api.filesystem.IFileSystem;
import me.thehutch.fusion.api.scheduler.IScheduler;

/**
 * @author thehutch
 */
public interface IEngine {
	/**
	 * Gets the {@link Platform} this engine is using.
	 *
	 * @return The platform
	 */
	public Platform getPlatform();

	/**
	 * Gets the version of the engine. Will always be in the format 'major.minor.build'.
	 *
	 * @return A string representation of the version
	 */
	public String getVersion();

	/**
	 * Gets the {@link Logger} used by this engine.
	 *
	 * @return The logger
	 */
	public Logger getLogger();

	/**
	 * Gets the {@link IScheduler} used by the engine.
	 *
	 * @return The scheduler
	 */
	public IScheduler getScheduler();

	/**
	 * Gets the {@link IEventManager} used by the engine.
	 *
	 * @return The event manager
	 */
	public IEventManager getEventManager();

	/**
	 * Gets the {@link IFileSystem} used by the engine.
	 *
	 * @return The file system
	 */
	public IFileSystem getFileSystem();

	/**
	 * Returns true if debug mode is enabled. Debug mode is more verbose and
	 * will terminate after any errors.
	 *
	 * @return True if debug mode is enabled
	 */
	public boolean debugMode();

	/**
	 * Stops the engine safely. All data is saved.
	 *
	 * @param reason The reason for the stopping
	 */
	public void stop(String reason);
}
