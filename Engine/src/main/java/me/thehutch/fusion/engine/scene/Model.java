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
package me.thehutch.fusion.engine.scene;

import me.thehutch.fusion.api.scene.SceneComponent;
import me.thehutch.fusion.api.util.Disposable;
import me.thehutch.fusion.engine.render.Program;
import me.thehutch.fusion.engine.render.VertexArray;
import me.thehutch.fusion.engine.render.VertexData;

public class Model extends SceneComponent implements Disposable {
	private final VertexArray mesh;
	private final Program program;

	public Model(Program program, VertexData meshData) {
		this.mesh = new VertexArray(meshData);
		this.program = program;
	}

	@Override
	public void dispose() {
		// Dispose the program
		this.program.dispose();
		// Dispose the mesh
		this.mesh.dispose();
	}

	@Override
	public void render() {
		// Use the program
		this.program.use();

		// Set the matrix uniform
		this.program.setUniform("modelViewProjection", getTransform().getTransformation());

		// Draw the mesh
		this.mesh.draw();
	}

	@Override
	public void update(float delta) {
		// Ignore
	}
}
