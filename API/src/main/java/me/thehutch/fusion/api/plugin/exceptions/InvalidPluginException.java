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
package me.thehutch.fusion.api.plugin.exceptions;

/**
 * @author thehutch
 */
public class InvalidPluginException extends Exception {
	/**
	 * Exception thrown when the plugin is invalid.
	 *
	 * @param message The reason for the plugin being invalid
	 */
	public InvalidPluginException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when the plugin is invalid.
	 *
	 * @param throwable The exception
	 */
	public InvalidPluginException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Exception thrown when the plugin is invalid
	 *
	 * @param message   The reason for the plugin being invalid
	 * @param throwable The exception
	 */
	public InvalidPluginException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
