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

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author thehutch
 * @param <E> The element type this bag contains
 */
public interface ImmutableBag<E> extends Iterable<E> {
	public E get(int index);

	public boolean contains(E obj);

	public int size();

	public int capacity();

	public boolean isEmpty();

	public default Stream<E> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	public default Stream<E> parallelStream() {
		return StreamSupport.stream(spliterator(), true);
	}
}
