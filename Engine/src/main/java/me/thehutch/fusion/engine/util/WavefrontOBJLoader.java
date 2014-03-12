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
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;
import me.thehutch.fusion.engine.render.VertexData;
import org.lwjgl.BufferUtils;

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

	public static VertexData load(InputStream stream) {
		// Create the lists to store the vertex data
		final TFloatList positions = new TFloatArrayList();
		final TFloatList texcoords = new TFloatArrayList();
		final TFloatList normals = new TFloatArrayList();
		final TIntList indices = new TIntArrayList();
		// Load the raw vertex data into the lists
		load(stream, positions, texcoords, normals, indices);

		// Convert the lists into buffers
		final FloatBuffer positionsBuffer = BufferUtils.createFloatBuffer(positions.size());
		positionsBuffer.put(positions.toArray());
		positionsBuffer.flip();

		final FloatBuffer texcoordsBuffer = BufferUtils.createFloatBuffer(texcoords.size());
		texcoordsBuffer.put(texcoords.toArray());
		texcoordsBuffer.flip();

		final FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(normals.size());
		normalsBuffer.put(normals.toArray());
		normalsBuffer.flip();

		final IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.size());
		indicesBuffer.put(indices.toArray());
		indicesBuffer.flip();

		// Return the vertex data with the loaded data
		return new VertexData(positionsBuffer, texcoordsBuffer, normalsBuffer, indicesBuffer);
	}

	/**
	 * Loads a .obj file, storing the data in the provided lists. After loading, the input stream will be closed.
	 * Note that normal and/or texture coord attributes might be missing from the .obj file. If this is the case, their lists will be empty.
	 * The indices are stored in the indices list.
	 *
	 * @param stream The input stream for the .obj file
	 * @param positions The list in which to store the positions
	 * @param textureCoords The list in which to store the texture coords
	 * @param normals The list in which to store the normals
	 * @param indices The list in which to store the indices
	 *
	 * @throws MalformedOBJFileException If any errors occur during loading
	 */
	private static void load(InputStream stream, TFloatList positions, TFloatList textureCoords, TFloatList normals, TIntList indices) {
		final TFloatList rawTextureCoords = new TFloatArrayList();
		final TFloatList rawNormalComponents = new TFloatArrayList();
		final TIntList textureCoordIndices = new TIntArrayList();
		final TIntList normalIndices = new TIntArrayList();
		String line = null;
		try (Scanner scanner = new Scanner(stream)) {
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				if (line.startsWith(POSITION_PREFIX + COMPONENT_SEPARATOR)) {
					parseComponents(positions, line);
				} else if (line.startsWith(TEXTURE_PREFIX + COMPONENT_SEPARATOR)) {
					parseComponents(rawTextureCoords, line);
				} else if (line.startsWith(NORMAL_PREFIX + COMPONENT_SEPARATOR)) {
					parseComponents(rawNormalComponents, line);
				} else if (line.startsWith(INDEX_PREFIX + COMPONENT_SEPARATOR)) {
					parseIndices(indices, textureCoordIndices, normalIndices, line);
				}
			}
			line = null;
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
		} catch (Exception ex) {
			throw new MalformedOBJFileException(line, ex);
		}
	}

	private static void parseComponents(TFloatList destination, String line) {
		final String[] components = line.split(COMPONENT_SEPARATOR);
		for (int i = 1; i < components.length; i++) {
			destination.add(Float.parseFloat(components[i]));
		}
	}

	private static void parseIndices(TIntList positions, TIntList textureCoords, TIntList normals, String line) {
		final String[] indicesGroup = line.split(COMPONENT_SEPARATOR);
		for (int i = 1; i < indicesGroup.length; i++) {
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

	private static class MalformedOBJFileException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		/**
		 * Creates an new exception from the line at which the error occured and the cuase.
		 * If the error did not occur on a line, the line can be passed as null.
		 *
		 * @param line The line the error occured on
		 * @param cause The cause of the exception
		 */
		private MalformedOBJFileException(String line, Throwable cause) {
			super(line != null ? "For line \"" + line + "\"" : null, cause);
		}
	}
}
