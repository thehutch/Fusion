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
package me.thehutch.fusion.api.util.container;

import java.util.Iterator;

/**
 * @author thehutch
 * @param <E> The element type of the container
 */
public class Bag<E> implements ImmutableBag<E> {
	private static final int DEFAULT_CAPACITY = 17;
	private E[] mData;
	private int mSize;

	public Bag() {
		this(DEFAULT_CAPACITY);
	}

	public Bag(int capacity) {
		mData = (E[]) new Object[capacity];
		mSize = 0;
	}

	@Override
	public E get(int index) {
		return mData[index];
	}

	public void set(int index, E item) {
		if (index >= mData.length) {
			expand(index * 2);
			mSize = index + 1;
		} else if (index >= mSize) {
			mSize = index + 1;
		}
		mData[index] = item;
	}

	public void add(E item) {
		if (mSize == mData.length) {
			expand();
		}
		mData[mSize++] = item;
	}

	public void addAll(Bag<E> items) {
		for (int i = 0; items.size() > i; ++i) {
			add(items.get(i));
		}
	}

	public E remove(int index) {
		final E obj = mData[index];
		mData[index] = mData[--mSize];
		mData[mSize] = null;
		return obj;
	}

	public boolean remove(E item) {
		for (int i = 0; i < mSize; ++i) {
			if (item == mData[i]) {
				mData[i] = mData[--mSize];
				mData[mSize] = null;
				return true;
			}
		}
		return false;
	}

	public E removeLast() {
		if (mSize > 0) {
			final E obj = mData[--mSize];
			mData[mSize] = null;
			return obj;
		}
		return null;
	}

	public boolean removeAll(ImmutableBag<E> bag) {
		boolean modified = false;
		for (int i = 0; i < bag.size(); ++i) {
			for (int j = 0; j < mSize; ++j) {
				if (bag.get(i) == mData[j]) {
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
		for (int i = 0; i < mSize; ++i) {
			if (mData[i] == item) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int size() {
		return mSize;
	}

	@Override
	public int capacity() {
		return mData.length;
	}

	@Override
	public boolean isEmpty() {
		return mSize == 0;
	}

	public void clear() {
		for (int i = 0; i < mSize; ++i) {
			mData[i] = null;
		}
		mSize = 0;
	}

	public boolean isIndexWithinBounds(int index) {
		return index >= 0 && index < capacity();
	}

	public void ensureCapacity(int index) {
		if (index >= mData.length) {
			expand((index * 3) / 2 + 1);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new BagIterator<>(this);
	}

	private void expand() {
		final int newCapacity = (mData.length * 3) / 2 + 1;
		expand(newCapacity);
	}

	private void expand(int newCapacity) {
		final E[] oldData = mData;
		mData = (E[]) new Object[newCapacity];
		System.arraycopy(oldData, 0, mData, 0, oldData.length);
	}

	private static class BagIterator<E> implements Iterator<E> {
		private final Bag<E> mBag;
		private int mIndex;

		private BagIterator(Bag<E> bag) {
			mBag = bag;
			mIndex = 0;
		}

		@Override
		public boolean hasNext() {
			return mIndex < mBag.mSize;
		}

		@Override
		public E next() {
			return mBag.get(mIndex++);
		}
	}
}
