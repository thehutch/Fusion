/*
 * This file is part of API, licensed under the Apache 2.0 License.
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
package me.thehutch.fusion.api.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Objects;
import me.thehutch.fusion.api.IEngine;
import me.thehutch.fusion.api.plugin.exceptions.InvalidPluginException;

/**
 * @author thehutch
 */
public final class PluginClassLoader extends URLClassLoader {
	private final PluginDescriptionFile mDesc;
	private final IEngine mEngine;
	private final Path mDataFolder;
	private final Plugin mPlugin;

	public PluginClassLoader(IEngine engine, ClassLoader parent, PluginDescriptionFile description, Path dataFolder, Path file) throws MalformedURLException, InvalidPluginException {
		super(new URL[] { file.toUri().toURL() }, parent);

		mEngine = engine;
		mDesc = description;
		mDataFolder = dataFolder;

		final Class<?> mainClass;
		try {
			mainClass = Class.forName(description.getMain(), true, this);
		} catch (ClassNotFoundException ex) {
			throw new InvalidPluginException("Unable to find main class: " + description.getMain(), ex);
		}

		final Class<? extends Plugin> pluginClass;
		try {
			pluginClass = mainClass.asSubclass(Plugin.class);
		} catch (ClassCastException ex) {
			throw new InvalidPluginException("Main class '" + description.getMain() + "' does not extend Plugin", ex);
		}
		try {
			mPlugin = pluginClass.newInstance();
		} catch (InstantiationException ex) {
			throw new InvalidPluginException("Unable to instantiate plugin: " + description.getMain(), ex);
		} catch (IllegalAccessException ex) {
			throw new InvalidPluginException("No public constructor in main: " + description.getMain(), ex);
		}
	}

	/**
	 * Returns the loaded mPlugin.
	 *
	 * @return The loaded mPlugin
	 */
	public Plugin getPlugin() {
		return mPlugin;
	}

	void initialise(Plugin plugin) {
		Objects.requireNonNull(plugin, "Initialising plugin can not be null.");

		// Make sure the mPlugin was loaded by this class loader
		if (plugin.getClass().getClassLoader() != this) {
			throw new IllegalArgumentException("Can not initialise plugin outside of the class loader.");
		}
		// Initialise the mPlugin
		plugin.initialise(mEngine, mDesc, mDataFolder, this);
	}
}
