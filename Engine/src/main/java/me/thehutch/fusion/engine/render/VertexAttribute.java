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

import gnu.trove.list.TFloatList;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

/**
 * @author thehutch
 */
public class VertexAttribute {
	private final ByteBuffer buffer;
	private final int size;

	public VertexAttribute(int size, TFloatList data) {
		this.buffer = BufferUtils.createByteBuffer(data.size() * Float.BYTES);
		data.forEach((float f) -> {
			this.buffer.putFloat(f);
			return true;
		});
		this.buffer.flip();
		this.size = size;
	}

	/**
	 * Return the size of the attribute.
	 *
	 * @return The attribute size
	 */
	public int getSize() {
		return size;
	}

	public ByteBuffer getData() {
		return buffer;
	}
}
