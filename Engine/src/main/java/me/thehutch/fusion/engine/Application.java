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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import me.thehutch.fusion.api.Platform;
import me.thehutch.fusion.engine.filesystem.FileSystem;
import org.lwjgl.LWJGLUtil;

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
		// The game engine
		final Engine engine;
		// Create Application
		{
			final Application application = new Application(args);
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
		}
		engine.initialise();
		engine.getScheduler().execute();
	}

	private static void loadNatives() throws IOException {
		// Get the native libraries required by this operating system
		final String[] files;
		switch (LWJGLUtil.getPlatform()) {
			case LWJGLUtil.PLATFORM_LINUX:
				files = new String[] { "liblwjgl.so", "liblwjgl64.so", "libopenal.so", "libopenal64.so", "libjinput-linux.so", "libjinput-linux64.so" };
				break;
			case LWJGLUtil.PLATFORM_WINDOWS:
				files = new String[] { "jinput-dx8.dll", "jinput-dx8_64.dll", "jinput-raw.dll", "jinput-raw_64.dll", "jinput-wintab.dll", "lwjgl.dll", "lwjgl64.dll", "OpenAL32.dll", "OpenAL64.dll" };
				break;
			case LWJGLUtil.PLATFORM_MACOSX:
				files = new String[] { "libjinput-osx.jnilib", "liblwjgl.jnilib", "openal.dylib" };
				break;
			default:
				throw new IllegalStateException("Unknown operating system");
		}
		// Locate the natives directory
		final Path nativesDir = FileSystem.BASE_DIRECTORY.resolve("natives").resolve(LWJGLUtil.getPlatformName());
		// Create the natives directory
		Files.createDirectories(nativesDir);
		// Copy each native library into the natives directory
		for (String file : files) {
			try (InputStream stream = FileSystem.getJarResource(file)) {
				Files.copy(stream, nativesDir.resolve(file), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		// Set the library paths
		System.setProperty("org.lwjgl.librarypath", nativesDir.toAbsolutePath().toString());
		System.setProperty("net.java.games.input.librarypath", nativesDir.toAbsolutePath().toString());
	}
}
