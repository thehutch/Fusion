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
package me.thehutch.fusion.api.util.container;

/**
 * @author thehutch
 * @param <E> The element type of the container
 */
public class Bag<E> implements ImmutableBag<E> {
	private static final int DEFAULT_CAPACITY = 32;
	private E[] data;
	private int size;

	public Bag() {
		this(DEFAULT_CAPACITY);
	}

	public Bag(int capacity) {
		this.data = (E[]) new Object[capacity];
		this.size = 0;
	}

	@Override
	public E get(int index) {
		return data[index];
	}

	public void set(int index, E item) {
		if (index >= data.length) {
			expand(index * 2);
			this.size = index + 1;
		} else if (index >= size) {
			this.size = index + 1;
		}
		this.data[index] = item;
	}

	public void add(E item) {
		if (size == data.length) {
			expand();
		}
		this.data[size++] = item;
	}

	public void addAll(Bag<E> items) {
		for (int i = 0; items.size() > i; ++i) {
			add(items.get(i));
		}
	}

	public E remove(int index) {
		final E obj = data[index];
		this.data[index] = data[--size];
		this.data[size] = null;
		return obj;
	}

	public boolean remove(E item) {
		for (int i = 0; i < size; ++i) {
			if (item == data[i]) {
				this.data[i] = data[--size];
				this.data[size] = null;
				return true;
			}
		}
		return false;
	}

	public E removeLast() {
		if (size > 0) {
			final E obj = data[--size];
			this.data[size] = null;
			return obj;
		}
		return null;
	}

	public boolean removeAll(ImmutableBag<E> bag) {
		boolean modified = false;
		for (int i = 0; i < bag.size(); ++i) {
			for (int j = 0; j < size; ++j) {
				if (bag.get(i) == data[j]) {
					remove(j);
					--j;
					modified = true;
					break;
				}
			}
		}
		return modified;
	}

	@Override
	public boolean contains(E item) {
		for (int i = 0; i < size; ++i) {
			if (data[i] == item) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public int capacity() {
		return data.length;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	public void clear() {
		for (int i = 0; i < size; ++i) {
			this.data[i] = null;
		}
		this.size = 0;
	}

	public boolean isIndexWithinBounds(int index) {
		return index >= 0 && index < capacity();
	}

	public void ensureCapacity(int index) {
		if (index >= data.length) {
			expand(index * 2);
		}
	}

	private void expand() {
		final int newCapacity = (data.length * 3) / 2 + 1;
		expand(newCapacity);
	}

	private void expand(int newCapacity) {
		final E[] oldData = data;
		this.data = (E[]) new Object[newCapacity];
		System.arraycopy(oldData, 0, data, 0, oldData.length);
	}
}
