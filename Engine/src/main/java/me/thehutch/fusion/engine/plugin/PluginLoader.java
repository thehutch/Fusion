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
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import me.thehutch.fusion.api.IEngine;
import me.thehutch.fusion.api.plugin.Plugin;
import me.thehutch.fusion.api.plugin.PluginClassLoader;
import me.thehutch.fusion.api.plugin.PluginDescriptionFile;
import me.thehutch.fusion.api.plugin.events.PluginDisabledEvent;
import me.thehutch.fusion.api.plugin.events.PluginEnabledEvent;
import me.thehutch.fusion.api.plugin.exceptions.InvalidDescriptionException;
import me.thehutch.fusion.api.plugin.exceptions.InvalidPluginException;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * @author thehutch
 */
public final class PluginLoader {
	private final TMap<String, PluginClassLoader> loaders = new THashMap<>();
	private final IEngine engine;

	public PluginLoader(IEngine engine) {
		this.engine = engine;
	}

	public Plugin load(Path path) throws InvalidDescriptionException, InvalidPluginException {
		Objects.requireNonNull(path, "Path to plugin can not be null!");

		if (!Files.exists(path)) {
			throw new InvalidDescriptionException("plugin.yml does not exist");
		}

		// TODO throw invalid description exception
		final PluginDescriptionFile description = getDescriptionFile(path);

		// Get the parent folder of the plugin
		final Path parentPath = path.getParent();
		// Resolve the data folder of the plugin
		final Path dataFolder = parentPath.resolve(description.getName());

		try {
			// Attempt to create the data folder
			Files.createDirectories(dataFolder);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		final PluginClassLoader loader;
		try {
			loader = new PluginClassLoader(engine, getClass().getClassLoader(), description, dataFolder, path);
		} catch (MalformedURLException ex) {
			throw new InvalidPluginException(ex);
		} catch (InvalidPluginException ex) {
			throw ex;
		}

		// Register this plugin
		this.loaders.put(description.getName(), loader);

		// return the loaded plugin
		return loader.getPlugin();
	}

	private PluginDescriptionFile getDescriptionFile(Path path) throws InvalidDescriptionException {
		Objects.requireNonNull(path, "Plugin jar path can not be null!");

		InputStream stream = null;

		try (JarFile jar = new JarFile(path.toFile())) {
			// Locate the plugin.yml inside the plugin jar
			final JarEntry entry = jar.getJarEntry("plugin.yml");
			if (entry == null) {
				throw new InvalidDescriptionException("Unable to find plugin.yml in jar: " + path);
			}
			// Create a stream to the plugin.yml
			stream = jar.getInputStream(entry);
			// Load the plugin.yml
			return PluginDescriptionFile.loadFromStream(stream);
		} catch (IOException | YAMLException ex) {
			throw new InvalidDescriptionException(ex);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public void enablePlugin(Plugin plugin) {
		// Make sure the plugin isn't already loaded
		if (!plugin.isEnabled()) {
			// TODO: Log plugin enabling
			final String pluginName = plugin.getName();

			// Only add the plugin if it has not already been enabled before
			this.loaders.putIfAbsent(pluginName, (PluginClassLoader) plugin.getClassLoader());
			// Set the plugin as enabled
			plugin.setEnabled(true);

			//TODO: Call plugin enable event
			this.engine.getEventManager().invoke(new PluginEnabledEvent(plugin));
		}
	}

	public void disablePlugin(Plugin plugin) {
		if (plugin.isEnabled()) {
			//TODO: Log plugin disabling

			// Remove the class loader
			this.loaders.remove(plugin.getName());
			// Disable the plugin
			plugin.setEnabled(false);
			// Execute the plugin disabled event
			this.engine.getEventManager().invoke(new PluginDisabledEvent(plugin));
		}
	}
}
