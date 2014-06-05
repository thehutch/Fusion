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
package me.thehutch.fusion.engine.util;

import gnu.trove.list.TFloatList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.list.array.TIntArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import me.thehutch.fusion.engine.render.VertexArrayObject;

/**
 * @author thehutch
 */
public class WavefrontOBJLoader {
	private static final String COMPONENT_SEPARATOR = " ";
	private static final String INDEX_SEPARATOR = "/";
	private static final String POSITION_PREFIX = "v";
	private static final String TEXTURE_PREFIX = "vt";
	private static final String NORMAL_PREFIX = "vn";
	private static final String INDEX_PREFIX = "f";
	private static final int POSITION_SIZE = 3;
	private static final int TEXCOORD_SIZE = 2;
	private static final int NORMAL_SIZE = 3;

	private WavefrontOBJLoader() {
	}

	public static VertexArrayObject load(Path path) {
		try {
			// Create the lists to store the vertex data
			final TFloatList positions = new TFloatArrayList();
			final TFloatList texcoords = new TFloatArrayList();
			final TFloatList normals = new TFloatArrayList();
			final TIntList indices = new TIntArrayList();
			// Load the raw vertex data into the lists
			load(Files.lines(path), positions, texcoords, normals, indices);
			// Calculate the normals if the model does not have any
			if (normals.isEmpty()) {
				calculateNormals(positions, indices, normals);
			}
			// Create the vertex array object
			final VertexArrayObject vao = new VertexArrayObject();
			vao.setIndices(indices);
			vao.addAttribute(0, POSITION_SIZE, positions);
			vao.addAttribute(1, TEXCOORD_SIZE, texcoords);
			vao.addAttribute(2, NORMAL_SIZE, normals);
			return vao;
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load mesh: " + path, ex);
		}
	}

	/**
	 * Loads a .obj file, storing the data in the provided lists. After loading, the input stream will be closed.
	 * Note that normal and/or texture coord attributes might be missing from the .obj file. If this is the case, their lists will be empty.
	 * The indices are stored in the indices list.
	 *
	 * @param stream        The input stream for the .obj file
	 * @param positions     The list in which to store the positions
	 * @param textureCoords The list in which to store the texture coords
	 * @param normals       The list in which to store the normals
	 * @param indices       The list in which to store the indices
	 *
	 * @throws MalformedOBJFileException If any errors occur during loading
	 */
	private static void load(Stream<String> lines, TFloatList positions, TFloatList textureCoords, TFloatList normals, TIntList indices) {
		final TFloatList rawTextureCoords = new TFloatArrayList();
		final TFloatList rawNormalComponents = new TFloatArrayList();
		final TIntList textureCoordIndices = new TIntArrayList();
		final TIntList normalIndices = new TIntArrayList();

		// Iterate over each line and parse them
		lines.forEachOrdered((String line) -> {
			if (line.startsWith(POSITION_PREFIX + COMPONENT_SEPARATOR)) {
				parseComponents(positions, line);
			} else if (line.startsWith(TEXTURE_PREFIX + COMPONENT_SEPARATOR)) {
				parseComponents(rawTextureCoords, line);
			} else if (line.startsWith(NORMAL_PREFIX + COMPONENT_SEPARATOR)) {
				parseComponents(rawNormalComponents, line);
			} else if (line.startsWith(INDEX_PREFIX + COMPONENT_SEPARATOR)) {
				parseIndices(indices, textureCoordIndices, normalIndices, line);
			}
		});
		// Close the stream
		lines.close();

		final boolean hasTextureCoords;
		final boolean hasNormals;
		if (!textureCoordIndices.isEmpty() && !rawTextureCoords.isEmpty()) {
			textureCoords.fill(0, positions.size() / POSITION_SIZE * TEXCOORD_SIZE, 0);
			hasTextureCoords = true;
		} else {
			hasTextureCoords = false;
		}
		if (!normalIndices.isEmpty() && !rawNormalComponents.isEmpty()) {
			normals.fill(0, positions.size() / POSITION_SIZE * NORMAL_SIZE, 0);
			hasNormals = true;
		} else {
			hasNormals = false;
		}
		if (hasTextureCoords) {
			for (int i = 0; i < textureCoordIndices.size(); i++) {
				final int textureCoordIndex = textureCoordIndices.get(i) * TEXCOORD_SIZE;
				final int positionIndex = indices.get(i) * TEXCOORD_SIZE;
				for (int ii = 0; ii < TEXCOORD_SIZE; ii++) {
					textureCoords.set(positionIndex + ii, rawTextureCoords.get(textureCoordIndex + ii));
				}
			}
		}
		if (hasNormals) {
			for (int i = 0; i < normalIndices.size(); i++) {
				final int normalIndex = normalIndices.get(i) * NORMAL_SIZE;
				final int positionIndex = indices.get(i) * NORMAL_SIZE;
				for (int ii = 0; ii < NORMAL_SIZE; ii++) {
					normals.set(positionIndex + ii, rawNormalComponents.get(normalIndex + ii));
				}
			}
		}
	}

	private static void parseComponents(TFloatList destination, String line) {
		final String[] components = line.split(COMPONENT_SEPARATOR);
		final int componentsLength = components.length;
		for (int i = 1; i < componentsLength; i++) {
			destination.add(Float.parseFloat(components[i]));
		}
	}

	private static void parseIndices(TIntList positions, TIntList textureCoords, TIntList normals, String line) {
		final String[] indicesGroup = line.split(COMPONENT_SEPARATOR);
		final int indicesLength = indicesGroup.length;
		for (int i = 1; i < indicesLength; i++) {
			final String[] indices = indicesGroup[i].split(INDEX_SEPARATOR);
			positions.add(Integer.parseInt(indices[0]) - 1);
			if (indices.length > 1 && !indices[1].isEmpty()) {
				textureCoords.add(Integer.parseInt(indices[1]) - 1);
			}
			if (indices.length > 2) {
				normals.add(Integer.parseInt(indices[2]) - 1);
			}
		}
	}

	private static void calculateNormals(TFloatList positions, TIntList indices, TFloatList normals) {
		// Initialise the normals to (0, 0, 0)
		normals.fill(0, positions.size(), 0.0f);
		// Calculate the normal of each vertex
		for (int i = 0; i < indices.size(); i += 3) {
			// Triangle position indices
			final int pos0 = indices.get(i) * 3;
			final int pos1 = indices.get(i + 1) * 3;
			final int pos2 = indices.get(i + 2) * 3;
			// First triangle vertex position
			final float x0 = positions.get(pos0);
			final float y0 = positions.get(pos0 + 1);
			final float z0 = positions.get(pos0 + 2);
			// Second triangle vertex position
			final float x1 = positions.get(pos1);
			final float y1 = positions.get(pos1 + 1);
			final float z1 = positions.get(pos1 + 2);
			// First triangle vertex position
			final float x2 = positions.get(pos2);
			final float y2 = positions.get(pos2 + 1);
			final float z2 = positions.get(pos2 + 2);
			// First edge position difference
			final float x10 = x1 - x0;
			final float y10 = y1 - y0;
			final float z10 = z1 - z0;
			// Second edge position difference
			final float x20 = x2 - x0;
			final float y20 = y2 - y0;
			final float z20 = z2 - z0;
			// Cross both edges to obtain normal
			final float nx = y10 * z20 - z10 * y20;
			final float ny = z10 * x20 - x10 * z20;
			final float nz = x10 * y20 - y10 * x20;
			// Add the normal to the first vertex
			normals.set(pos0, normals.get(pos0) + nx);
			normals.set(pos0 + 1, normals.get(pos0 + 1) + ny);
			normals.set(pos0 + 2, normals.get(pos0 + 2) + nz);
			// Add the normal to the second vertex
			normals.set(pos1, normals.get(pos1) + nx);
			normals.set(pos1 + 1, normals.get(pos1 + 1) + ny);
			normals.set(pos1 + 2, normals.get(pos1 + 2) + nz);
			// Add the normal to the third vertex
			normals.set(pos2, normals.get(pos2) + nx);
			normals.set(pos2 + 1, normals.get(pos2 + 1) + ny);
			normals.set(pos2 + 2, normals.get(pos2 + 2) + nz);
		}
		// Iterate over all normals
		for (int i = 0; i < indices.size(); ++i) {
			// Index for the normal
			final int nor = indices.get(i) * 3;
			// Get the normal
			float nx = normals.get(nor);
			float ny = normals.get(nor + 1);
			float nz = normals.get(nor + 2);
			// Length of the normal
			final float length = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
			// Normal the normal
			nx /= length;
			nx /= length;
			nx /= length;
			// Update the normal
			normals.set(nor, nx);
			normals.set(nor + 1, ny);
			normals.set(nor + 2, nz);
		}
	}
}
