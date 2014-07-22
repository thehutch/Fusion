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

import java.util.Collection;

/**
 * @author thehutch
 */
public interface IPluginManager {
	/**
	 * Returns the plugin which has the given name.
	 *
	 * @param name The name of the plugin
	 *
	 * @return The plugin, null if not found
	 */
	public Plugin getPlugin(String name);

	/**
	 * Enables the plugin. If the plugin is already enabled then
	 * this has no effect.
	 *
	 * @param plugin The plugin to enable
	 */
	public void enablePlugin(Plugin plugin);

	/**
	 * Disables the plugin. If the plugin is already disabled
	 * then this has no effect.
	 *
	 * @param plugin The plugin to disable
	 */
	public void disablePlugin(Plugin plugin);

	/**
	 * Returns true if the plugin is currently enabled.
	 *
	 * @param plugin The plugin
	 *
	 * @return True if enabled
	 */
	public boolean isPluginEnabled(Plugin plugin);

	/**
	 * Returns true if the plugin is currently disabled.
	 *
	 * @param plugin The plugin
	 *
	 * @return True if disabled
	 */
	public boolean isPluginDisabled(Plugin plugin);

	/**
	 * Returns an immutable collection of all the currently loaded
	 * plugins.
	 *
	 * @return An immutable collection of plugins
	 */
	public Collection<Plugin> getPlugins();
}
