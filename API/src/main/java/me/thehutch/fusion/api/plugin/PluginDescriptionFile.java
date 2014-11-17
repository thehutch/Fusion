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

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import me.thehutch.fusion.api.plugin.exceptions.InvalidDescriptionException;
import org.yaml.snakeyaml.Yaml;

/**
 * @author thehutch
 */
public final class PluginDescriptionFile {
	private static final ThreadLocal<Yaml> YAML = new ThreadLocal<Yaml>() {
		@Override
		protected Yaml initialValue() {
			return new Yaml();
		}
	};
	private final String mMain;
	private final String mName;
	private final String mVersion;
	private final String mDesc;

	public PluginDescriptionFile(String main, String name, String version, String description) {
		mMain = main;
		mName = name;
		mVersion = version;
		mDesc = description;
	}

	public String getMain() {
		return mMain;
	}

	public String getName() {
		return mName;
	}

	public String getVersion() {
		return mVersion;
	}

	public String getDescription() {
		return mDesc;
	}

	public static PluginDescriptionFile loadFromStream(InputStream stream) throws InvalidDescriptionException {
		final Map<?, ?> map = (Map<?, ?>) YAML.get().load(Objects.requireNonNull(stream, "Stream can not be null!"));

		final String main;
		final String name;
		final String version;
		final String description;

		// Get the plugin mMain
		try {
			main = map.get("main").toString().trim();
			if (main.startsWith("me.thehutch.fusion")) {
				throw new InvalidDescriptionException("Plugin main can not be within the 'me.thehutch.fusion' namespace.");
			}
		} catch (NullPointerException ex) {
			throw new InvalidDescriptionException("Main is not defined", ex);
		} catch (ClassCastException ex) {
			throw new InvalidDescriptionException("Main is of wrong type", ex);
		}
		// Get the plugin mName
		try {
			name = map.get("name").toString().trim().replace(' ', '_');
			if (!name.matches("^[A-Za-z0-9 _.-]+$")) {
				throw new InvalidDescriptionException(String.format("Name '%s' contains invalid characters.", name));
			}
		} catch (NullPointerException ex) {
			throw new InvalidDescriptionException("Name is not defined", ex);
		} catch (ClassCastException ex) {
			throw new InvalidDescriptionException("Name is of wrong type", ex);
		}
		// Get the plugin mVersion
		try {
			version = map.get("version").toString().trim();
		} catch (NullPointerException ex) {
			throw new InvalidDescriptionException("Version is not defined", ex);
		} catch (ClassCastException ex) {
			throw new InvalidDescriptionException("Version is of wrong type", ex);
		}
		// Get the plugin mDesc
		final Object desc = map.get("description");
		if (desc != null) {
			description = desc.toString();
		} else {
			description = "";
		}

		// TODO: Add additional information for the plugin.yml
		return new PluginDescriptionFile(main, name, version, description);
	}
}
