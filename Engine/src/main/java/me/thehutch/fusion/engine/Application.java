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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import me.thehutch.fusion.api.Platform;

/**
 * @author thehutch
 */
public class Application {
	protected Platform platform;
	protected boolean debug;

	private Application(String[] args) {
		this.platform = Platform.CLIENT;
		this.debug = true;
	}

	public static void main(String[] args) throws Exception {
		// Unpack natives
		{
			final String os = System.getProperty("os.name").toLowerCase();
			final String[] files;
			final String nativePath;
			if (os.contains("win")) {
				nativePath = "windows" + File.separator;
				files = new String[] { "jinput-dx8.dll", "jinput-dx8_64.dll", "jinput-raw.dll", "jinput-raw_64.dll", "jinput-wintab.dll", "lwjgl.dll", "lwjgl64.dll", "OpenAL32.dll", "OpenAL64.dll" };
			} else if (os.contains("mac")) {
				nativePath = "mac" + File.separator;
				files = new String[] { "libjinput-osx.jnilib", "liblwjgl.jnilib", "openal.dylib" };
			} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
				nativePath = "linux" + File.separator;
				files = new String[] { "liblwjgl.so", "liblwjgl64.so", "libopenal.so", "libopenal64.so", "libjinput-linux.so", "libjinput-linux64.so" };
			} else {
				throw new IllegalStateException("Unknown Operating System: " + os);
			}

			final File nativesDir = new File(System.getProperty("user.dir"), "natives" + File.separator + nativePath);
			nativesDir.mkdirs();

			for (String file : files) {
				final File target = new File(nativesDir, file);
				if (!target.exists()) {
					try {
						Files.copy(Client.class.getResourceAsStream("/" + file), target.toPath());
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
			System.setProperty("org.lwjgl.librarypath", nativesDir.getAbsolutePath());
			System.setProperty("net.java.games.input.librarypath", nativesDir.getAbsolutePath());
		}

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
}
