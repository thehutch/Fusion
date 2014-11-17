/*
 * This file is part of API, licensed under the Apache 2.0 License.
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
	private ByteBuffer mBuffer;

	private DynamicByteBuffer(ByteBuffer buffer) {
		mBuffer = buffer;
	}

	public ByteBuffer getBuffer() {
		return mBuffer;
	}

	public byte getByte() {
		return mBuffer.get();
	}

	public ByteBuffer putByte(byte value) {
		ensureCapacity(Byte.BYTES);
		return mBuffer.put(value);
	}

	public char getChar() {
		return mBuffer.getChar();
	}

	public ByteBuffer putChar(char value) {
		ensureCapacity(Character.BYTES);
		return mBuffer.putChar(value);
	}

	public ByteBuffer putChar(int index, char value) {
		return mBuffer.putChar(index, value);
	}

	public short getShort() {
		return mBuffer.getShort();
	}

	public ByteBuffer putShort(short value) {
		ensureCapacity(Short.BYTES);
		return mBuffer.putShort(value);
	}

	public ByteBuffer putShort(int index, short value) {
		return mBuffer.putShort(index, value);
	}

	public int getInt() {
		return mBuffer.getInt();
	}

	public ByteBuffer putInt(int value) {
		ensureCapacity(Integer.BYTES);
		return mBuffer.putInt(value);
	}

	public ByteBuffer putInt(int index, int value) {
		return mBuffer.putInt(index, value);
	}

	public long getLong() {
		return mBuffer.getLong();
	}

	public ByteBuffer putLong(long value) {
		ensureCapacity(Long.BYTES);
		return mBuffer.putLong(value);
	}

	public ByteBuffer putLong(int index, long value) {
		return mBuffer.putLong(index, value);
	}

	public float getFloat() {
		return mBuffer.getFloat();
	}

	public ByteBuffer putFloat(float value) {
		ensureCapacity(Float.BYTES);
		return mBuffer.putFloat(value);
	}

	public ByteBuffer putFloat(int index, float value) {
		return mBuffer.putFloat(index, value);
	}

	public double getDouble() {
		return mBuffer.getDouble();
	}

	public ByteBuffer putDouble(double value) {
		ensureCapacity(Double.BYTES);
		return mBuffer.putDouble(value);
	}

	public ByteBuffer putDouble(int index, double value) {
		return mBuffer.putDouble(index, value);
	}

	public int limit() {
		return mBuffer.limit();
	}

	public int position() {
		return mBuffer.position();
	}

	public int capacity() {
		return mBuffer.capacity();
	}

	public byte[] array() {
		return mBuffer.array();
	}

	public boolean isEmpty() {
		return mBuffer.limit() == 0;
	}

	public boolean hasRemaining() {
		return mBuffer.hasRemaining();
	}

	public DynamicByteBuffer flip() {
		mBuffer.flip();
		return this;
	}

	public DynamicByteBuffer compact() {
		mBuffer.compact();
		return this;
	}

	public DynamicByteBuffer clear() {
		mBuffer.clear();
		return this;
	}

	public DynamicByteBuffer duplicate() {
		return new DynamicByteBuffer(mBuffer.duplicate());
	}

	public DynamicByteBuffer slice() {
		return new DynamicByteBuffer(mBuffer.slice());
	}

	private void ensureCapacity(int size) {
		final int remaining = mBuffer.remaining();
		if (size > remaining) {
			reallocate((int) ((mBuffer.capacity() + (size - remaining)) * 1.5f));
		}
	}

	private void reallocate(int newCapacity) {
		// Store the current position of the mBuffer
		final int oldPosition = mBuffer.position();
		// Create a new mBuffer of the new capacity
		final byte[] newBuffer = new byte[newCapacity];
		// Copy the current mBuffer into the new one
		System.arraycopy(mBuffer.array(), 0, newBuffer, 0, oldPosition);
		// Reallocate the mBuffer
		mBuffer = ByteBuffer.wrap(newBuffer);
		mBuffer.position(oldPosition);
	}

	public static DynamicByteBuffer allocate(int capacity) {
		return new DynamicByteBuffer(ByteBuffer.allocate(capacity));
	}
}
