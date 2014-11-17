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

import java.nio.file.Path;
import me.thehutch.fusion.api.IEngine;

/**
 * @author thehutch
 */
public abstract class Plugin {
	private ClassLoader mClassLoader;
	private PluginDescriptionFile mDesc;
	private Path mDataFolder;
	private Path mConfigFile;
	private IEngine mEngine;
	private boolean mEnabled;

	public Plugin() {
		final ClassLoader loader = getClass().getClassLoader();
		if (!(loader instanceof PluginClassLoader)) {
			throw new IllegalStateException("Plugin requires " + PluginClassLoader.class.getName());
		}
		((PluginClassLoader) loader).initialise(this);
	}

	Plugin(IEngine engine, PluginDescriptionFile description, Path dataFolder) {
		ClassLoader loader = getClass().getClassLoader();
		if (loader instanceof PluginClassLoader) {
			throw new IllegalStateException("Can not use initialisation constructor at runtime.");
		}
		initialise(engine, description, dataFolder, loader);
	}

	/**
	 * Called when the plugin is mEnabled.
	 */
	protected abstract void onEnable();

	/**
	 * Called when the plugin is disabled.
	 */
	protected abstract void onDisable();

	/**
	 * Returns the name of the plugin as found in the plugin.yml.
	 *
	 * @return The name of the plugin
	 */
	public final String getName() {
		return mDesc.getName();
	}

	/**
	 * Returns the mDesc file of the plugin (plugin.yml).
	 *
	 * @return The mDesc file
	 */
	public final PluginDescriptionFile getDescription() {
		return mDesc;
	}

	/**
	 * Returns the class loader which loaded this plugin.
	 *
	 * @return The classloader
	 */
	public final ClassLoader getClassLoader() {
		return mClassLoader;
	}

	/**
	 * Returns a path to the data folder of this plugin.
	 * The folder has the same name as the plugin.
	 *
	 * @return The path to the data folder
	 */
	public final Path getDataFolder() {
		return mDataFolder;
	}

	/**
	 * Returns a path to the configuration file (config.yml). The configuration
	 * file is extracted from the plugin jar when it is first loaded
	 * and placed inside the data folder.
	 *
	 * @return The path to the configuration file
	 */
	public final Path getConfigFile() {
		return mConfigFile;
	}

	/**
	 * Returns the mEngine which loaded this plugin.
	 *
	 * @return The mEngine
	 */
	public final IEngine getEngine() {
		return mEngine;
	}

	/**
	 * Returns the current mEnabled state of this plugin.
	 *
	 * @return True if the plugin is mEnabled
	 */
	public final boolean isEnabled() {
		return mEnabled;
	}

	@Override
	public final boolean equals(Object obj) {
		if (!(obj instanceof Plugin)) {
			return false;
		}
		return this == obj;
	}

	@Override
	public final int hashCode() {
		return getName().hashCode();
	}

	/**
	 * Sets the plugin's mEnabled state. This should only be called
	 * by the plugin loader and not by the plugin itself.
	 *
	 * @param enabled True to enable the plugin
	 */
	public final void setEnabled(boolean enabled) {
		if (enabled != isEnabled()) {
			mEnabled = enabled;
			if (enabled) {
				onEnable();
			} else {
				onDisable();
			}
		}
	}

	final void initialise(IEngine engine, PluginDescriptionFile description, Path dataFolder, ClassLoader classLoader) {
		mEngine = engine;
		mDesc = description;
		mDataFolder = dataFolder;
		mClassLoader = classLoader;
		mConfigFile = dataFolder.resolve("config.yml");
	}
}
