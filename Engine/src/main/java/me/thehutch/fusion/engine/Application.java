/*
 * This file is part of Engine.
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import me.thehutch.fusion.api.Platform;
import me.thehutch.fusion.engine.filesystem.FileSystem;

/**
 * @author thehutch
 */
public class Application {
	protected final Platform platform;
	protected final boolean debug;

	private Application(String[] args) {
		this.platform = Platform.CLIENT;
		this.debug = true;
	}

	public static void main(String[] args) throws Exception {
		// Load natives
		loadNatives();
		// Create Application
		final Application application = new Application(args);
		final Engine engine;
		switch (application.platform) {
			case CLIENT:
				engine = new Client(application);
				break;
			case SERVER:
				engine = new Server(application);
				break;
			default:
				throw new IllegalStateException("Unknown Platform: " + application.platform);
		}
		engine.getScheduler().execute();
	}

	private static void loadNatives() throws IOException {
		// Get the operating system the engine is running on
		final String os = System.getProperty("os.name").toLowerCase();

		final String[] files;
		final String nativePath;

		// Check which operating system it is
		if (os.contains("win")) {
			nativePath = "windows";
			files = new String[]{ "jinput-dx8.dll", "jinput-dx8_64.dll", "jinput-raw.dll", "jinput-raw_64.dll", "jinput-wintab.dll", "lwjgl.dll", "lwjgl64.dll", "OpenAL32.dll", "OpenAL64.dll" };
		} else if (os.contains("mac")) {
			nativePath = "mac";
			files = new String[]{ "libjinput-osx.jnilib", "liblwjgl.jnilib", "openal.dylib" };
		} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
			nativePath = "linux";
			files = new String[]{ "liblwjgl.so", "liblwjgl64.so", "libopenal.so", "libopenal64.so", "libjinput-linux.so", "libjinput-linux64.so" };
		} else {
			throw new IllegalStateException("Unknown Operating System: " + os);
		}

		// Locate the natives directory
		final Path nativesDir = FileSystem.BASE_DIRECTORY.resolve("natives").resolve(nativePath);
		// Create the natives directory
		Files.createDirectories(nativesDir);
		// Copy each native library into the natives directory
		for (String file : files) {
			Files.copy(FileSystem.getJarResource(file), nativesDir.resolve(file), StandardCopyOption.REPLACE_EXISTING);
		}
		// Set the library paths
		System.setProperty("org.lwjgl.librarypath", nativesDir.toAbsolutePath().toString());
		System.setProperty("net.java.games.input.librarypath", nativesDir.toAbsolutePath().toString());
	}
}
