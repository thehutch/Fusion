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
package me.thehutch.fusion.engine.render;


import gnu.trove.list.TIntList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.nio.IntBuffer;
import me.thehutch.fusion.api.util.Disposable;
import org.lwjgl.BufferUtils;

public class VertexData implements Disposable {
	private final TIntObjectMap<VertexAttribute> attributes = new TIntObjectHashMap<>();
	private final IntBuffer indicesBuffer;

//	public VertexData(VertexData vertexData) {
//		// Copy the attributes across
//		this.attributes.putAll(vertexData.attributes);
//		// Create the indices buffer and copy the data cross
//		this.indicesBuffer = BufferUtils.createIntBuffer(vertexData.getIndicesCount());
//		this.indicesBuffer.put(vertexData.indicesBuffer);
//	}

	public VertexData(TIntList indices) {
		this.indicesBuffer = BufferUtils.createIntBuffer(indices.size());
		indices.forEach((int i) -> {
			this.indicesBuffer.put(i);
			return true;
		});
		this.indicesBuffer.flip();
	}

	public void addAttribute(VertexAttribute attribute) {
		this.attributes.put(attributes.size(), attribute);
	}

	public VertexAttribute getAttribute(int index) {
		return attributes.get(index);
	}

	public int getAttributeCount() {
		return attributes.size();
	}

	public IntBuffer getIndices() {
		return indicesBuffer;
	}

	public int getIndicesCount() {
		return indicesBuffer.capacity();
	}

	@Override
	public void dispose() {
		// Remove the attributes
		this.attributes.clear();
		// Remove the indices
		this.indicesBuffer.clear();
	}
}
