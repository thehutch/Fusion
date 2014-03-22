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
package me.thehutch.fusion.api.util.hashing;

import me.thehutch.fusion.api.maths.vector.Vector2;
import me.thehutch.fusion.api.maths.vector.Vector3;

/**
 * @author thehutch
 */
public class LongTripleHash {
	private LongTripleHash() {
	}

	public static long hash(Vector2 vec) {
		return hash(vec.getFloorX(), vec.getFloorY(), 0);
	}

	public static long hash(Vector3 vec) {
		return hash(vec.getFloorX(), vec.getFloorY(), vec.getFloorZ());
	}

	public static long hash(int x, int y, int z) {
		return ((long) ((x >> 11) & 0x100000 | x & 0xFFFFF)) << 42 | ((long) ((y >> 11) & 0x100000 | y & 0xFFFFF)) << 21 | ((z >> 11) & 0x100000 | z & 0xFFFFF);
	}

	public static int key1(long hash) {
		return keyInt((hash >> 42) & 0x1FFFFF);
	}

	public static int key2(long hash) {
		return keyInt((hash >> 21) & 0x1FFFFF);
	}

	public static int key3(long hash) {
		return keyInt(hash & 0x1FFFFF);
	}

	private static int keyInt(long key) {
		return (int) (key - ((key & 0x100000) << 1));
	}
}
