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
package me.thehutch.fusion.engine.filesystem.loaders;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import me.thehutch.fusion.api.filesystem.IResourceManager;
import me.thehutch.fusion.engine.filesystem.FileSystem;
import me.thehutch.fusion.engine.render.opengl.Program;

/**
 * @author thehutch
 */
public class ProgramManager implements IResourceManager<Program> {
	private final TMap<Path, Program> programs = new THashMap<>();

	public ProgramManager() {
	}

	@Override
	public Program get(Path path, boolean load) {
		final Program program = programs.get(path);
		if (program == null && load) {
			return load(path);
		}
		return program;
	}

	@Override
	public boolean isLoaded(Path path) {
		return get(path, false) != null;
	}

	@Override
	public Program load(Path path) {
		try (final Stream<String> lines = Files.lines(path)) {
			final Program program = new Program();
			lines.forEach((String line) -> {
				try {
					if (line.startsWith("vertex_shader")) {
						final StringBuilder source = new StringBuilder();
						loadShaderSource(source, Files.lines(getPath(line)));
						program.attachShader(source, GL_VERTEX_SHADER);
					} else if (line.startsWith("fragment_shader")) {
						final StringBuilder source = new StringBuilder();
						loadShaderSource(source, Files.lines(getPath(line)));
						program.attachShader(source, GL_FRAGMENT_SHADER);
					} else if (line.startsWith("geometry_shader")) {
						final StringBuilder source = new StringBuilder();
						loadShaderSource(source, Files.lines(getPath(line)));
						program.attachShader(source, GL_GEOMETRY_SHADER);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			});
			// Link the program
			program.link();
			// Add the resource to the resource cache
			//this.resources.put(path, program);
			return program;
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load model: " + path, ex);
		}
	}

	@Override
	public void unload(Path path) {
		final Program program = programs.remove(path);
		if (program != null) {
			program.dispose();
		}
	}

	@Override
	public void dispose() {
		this.programs.forEachValue((Program program) -> {
			program.dispose();
			return true;
		});
		this.programs.clear();
	}

	private static void loadShaderSource(StringBuilder source, Stream<String> lines) {
		lines.forEachOrdered((String line) -> {
			source.append(line).append('\n');
		});
		lines.close();
	}

	private static Path getPath(String line) {
		return FileSystem.DATA_DIRECTORY.resolve(line.substring(line.indexOf(':', 0) + 1).trim());
	}
}
