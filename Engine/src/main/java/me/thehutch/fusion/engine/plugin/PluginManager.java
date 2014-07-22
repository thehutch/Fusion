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
package me.thehutch.fusion.engine.plugin;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import me.thehutch.fusion.api.plugin.IPluginManager;
import me.thehutch.fusion.api.plugin.Plugin;
import me.thehutch.fusion.api.plugin.exceptions.InvalidDescriptionException;
import me.thehutch.fusion.api.plugin.exceptions.InvalidPluginException;
import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.Engine;

/**
 * @author thehutch
 */
public class PluginManager implements IPluginManager, Disposable {
	private final TMap<String, Plugin> plugins = new THashMap<>();
	private final PluginLoader loader;

	public PluginManager(Engine engine) {
		this.loader = new PluginLoader(engine);
	}

	@Override
	public Plugin getPlugin(String name) {
		return plugins.get(name);
	}

	@Override
	public void enablePlugin(Plugin plugin) {
		this.loader.enablePlugin(plugin);
	}

	@Override
	public void disablePlugin(Plugin plugin) {
		this.loader.disablePlugin(plugin);
	}

	@Override
	public boolean isPluginEnabled(Plugin plugin) {
		return plugin.isEnabled();
	}

	@Override
	public boolean isPluginDisabled(Plugin plugin) {
		return !plugin.isEnabled();
	}

	@Override
	public Collection<Plugin> getPlugins() {
		return Collections.unmodifiableCollection(plugins.values());
	}

	@Override
	public void dispose() {
		this.plugins.forEachValue((Plugin plugin) -> {
			this.loader.disablePlugin(plugin);
			return true;
		});
		this.plugins.clear();
	}

	public void loadPlugin(Path path) {
		try {
			// Load the plugin
			final Plugin plugin = loader.load(path);
			// Insert the plugin into the cache
			this.plugins.put(path.toString(), plugin);
		} catch (InvalidDescriptionException ex) {
			throw new IllegalStateException("Invalid plugin description: " + path, ex);
		} catch (InvalidPluginException ex) {
			throw new IllegalStateException("Invalid plugin: " + path, ex);
		}
	}
}
