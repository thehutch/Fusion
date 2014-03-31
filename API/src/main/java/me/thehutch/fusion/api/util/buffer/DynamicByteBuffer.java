/*
 * This file is part of API.
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
package me.thehutch.fusion.api.util.buffer;

import java.nio.ByteBuffer;

public class DynamicByteBuffer {
	private ByteBuffer buffer;

	private DynamicByteBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public byte getByte() {
		return buffer.get();
	}

	public ByteBuffer putByte(byte value) {
		ensureCapacity(Byte.BYTES);
		return buffer.put(value);
	}

	public char getChar() {
		return buffer.getChar();
	}

	public ByteBuffer putChar(char value) {
		ensureCapacity(Character.BYTES);
		return buffer.putChar(value);
	}

	public ByteBuffer putChar(int index, char value) {
		return buffer.putChar(index, value);
	}

	public short getShort() {
		return buffer.getShort();
	}

	public ByteBuffer putShort(short value) {
		ensureCapacity(Short.BYTES);
		return buffer.putShort(value);
	}

	public ByteBuffer putShort(int index, short value) {
		return buffer.putShort(index, value);
	}

	public int getInt() {
		return buffer.getInt();
	}

	public ByteBuffer putInt(int value) {
		ensureCapacity(Integer.BYTES);
		return buffer.putInt(value);
	}

	public ByteBuffer putInt(int index, int value) {
		return buffer.putInt(index, value);
	}

	public long getLong() {
		return buffer.getLong();
	}

	public ByteBuffer putLong(long value) {
		ensureCapacity(Long.BYTES);
		return buffer.putLong(value);
	}

	public ByteBuffer putLong(int index, long value) {
		return buffer.putLong(index, value);
	}

	public float getFloat() {
		return buffer.getFloat();
	}

	public ByteBuffer putFloat(float value) {
		ensureCapacity(Float.BYTES);
		return buffer.putFloat(value);
	}

	public ByteBuffer putFloat(int index, float value) {
		return buffer.putFloat(index, value);
	}

	public double getDouble() {
		return buffer.getDouble();
	}

	public ByteBuffer putDouble(double value) {
		ensureCapacity(Double.BYTES);
		return buffer.putDouble(value);
	}

	public ByteBuffer putDouble(int index, double value) {
		return buffer.putDouble(index, value);
	}

	public int limit() {
		return buffer.limit();
	}

	public int position() {
		return buffer.position();
	}

	public int capacity() {
		return buffer.capacity();
	}

	public byte[] array() {
		return buffer.array();
	}

	public boolean isEmpty() {
		return buffer.limit() == 0;
	}

	public boolean hasRemaining() {
		return buffer.hasRemaining();
	}

	public DynamicByteBuffer flip() {
		this.buffer.flip();
		return this;
	}

	public DynamicByteBuffer compact() {
		this.buffer.compact();
		return this;
	}

	public DynamicByteBuffer clear() {
		this.buffer.clear();
		return this;
	}

	public DynamicByteBuffer duplicate() {
		return new DynamicByteBuffer(buffer.duplicate());
	}

	public DynamicByteBuffer slice() {
		return new DynamicByteBuffer(buffer.slice());
	}

	private void ensureCapacity(int size) {
		final int remaining = buffer.remaining();
		if (size > remaining) {
			reallocate((int) ((buffer.capacity() + (size - remaining)) * 1.5f));
		}
	}

	private void reallocate(int newCapacity) {
		// Store the current position of the buffer
		final int oldPosition = buffer.position();
		// Create a new buffer of the new capacity
		final byte[] newBuffer = new byte[newCapacity];
		// Copy the current buffer into the new one
		System.arraycopy(buffer.array(), 0, newBuffer, 0, oldPosition);
		// Reallocate the buffer
		this.buffer = ByteBuffer.wrap(newBuffer);
		this.buffer.position(oldPosition);
	}

	public static DynamicByteBuffer allocate(int capacity) {
		return new DynamicByteBuffer(ByteBuffer.allocate(capacity));
	}
}
