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
package me.thehutch.fusion.api.plugin.events;

import me.thehutch.fusion.api.plugin.Plugin;

/**
 * A PluginEvent called when a plugin is enabled.
 *
 * @author thehutch
 */
public final class PluginEnabledEvent extends PluginEvent {
	public PluginEnabledEvent(Plugin plugin) {
		super(plugin, false);
	}
}
