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

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import me.thehutch.fusion.api.IEngine;
import me.thehutch.fusion.api.component.ComponentSystem;
import me.thehutch.fusion.api.plugin.Plugin;
import me.thehutch.fusion.api.scheduler.TaskPriority;
import me.thehutch.fusion.engine.event.EventManager;
import me.thehutch.fusion.engine.filesystem.FileSystem;
import me.thehutch.fusion.engine.plugin.PluginManager;
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
	private static final Logger LOGGER;
	private static final long TICKS_PER_SECOND = 60L;
	private final PluginManager pluginManager;
	private final EventManager eventManager;
	private final ComponentSystem system;
	private final FileSystem fileSystem;
	private final Scheduler scheduler;
	private final boolean debug;

	static {
		final Formatter formatter = new Formatter() {
			private final String format = "[%s]: %s\n";

			@Override
			public String format(LogRecord record) {
				final Object[] params = record.getParameters();
				String message = record.getMessage();
				if (params != null) {
					final int length = params.length;
					for (int i = 0; i < length; ++i) {
						if (params[i] instanceof Path) {
							final Path path = (Path) params[i];
							message = message.replace("{" + i + "}", FileSystem.BASE_DIRECTORY.relativize(path).toString());
						} else {
							message = message.replace("{" + i + "}", params[i].toString());
						}
					}
				}
				final Throwable exception = record.getThrown();
				if (exception != null) {
					exception.printStackTrace();
				}
				return String.format(format, record.getLevel().getName(), message);
			}
		};
		final ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(formatter);

		LOGGER = Logger.getLogger("Fusion-" + ENGINE_VERSION);
		LOGGER.setUseParentHandlers(false);
		LOGGER.addHandler(handler);
	}

	protected Engine(Application application) {
		// Initialise debug status
		this.debug = application.debug;

		// Create the engine scheduler
		this.scheduler = new Scheduler(TICKS_PER_SECOND);

		// Create the event manager
		this.eventManager = new EventManager();

		// Create the plugin manager
		this.pluginManager = new PluginManager(this);

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

		// Attempt to load all the plugins
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(FileSystem.PLUGIN_DIRECTORY, "*.jar")) {
			stream.forEach(pluginManager::loadPlugin);
		} catch (IOException ex) {
			getLogger().log(Level.WARNING, "Unable to load plugins!", ex);
		}
		// Enable the plugins
		// TODO: Enable in dependencies order
		final Collection<Plugin> plugins = pluginManager.getPlugins();
		plugins.forEach(pluginManager::enablePlugin);
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
	public final PluginManager getPluginManager() {
		return pluginManager;
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

		// Dispose of the plugin manager
		getPluginManager().dispose();

		getLogger().log(Level.INFO, "Exiting Engine: {0}", reason);
	}

	public static Logger getLogger() {
		return LOGGER;
	}
}
