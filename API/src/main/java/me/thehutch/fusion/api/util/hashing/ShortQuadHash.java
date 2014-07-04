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
package me.thehutch.fusion.api.util.hashing;

import me.thehutch.fusion.api.maths.Vector2;
import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.maths.Vector4;

/**
 * @author thehutch
 */
public class ShortQuadHash {
	private ShortQuadHash() {
	}

	public static short hash(Vector2 vec) {
		return hash(vec.getFloorX(), vec.getFloorY(), 0, 0);
	}

	public static short hash(Vector3 vec) {
		return hash(vec.getFloorX(), vec.getFloorY(), vec.getFloorZ(), 0);
	}

	public static short hash(Vector4 vec) {
		return hash(vec.getFloorX(), vec.getFloorY(), vec.getFloorZ(), vec.getFloorW());
	}

	public static short hash(int x, int y, int z, int w) {
		return (short) ((x & 0xF) << 12 | (y & 0xF) << 8 | (z & 0xF) << 4 | w & 0xF);
	}

	public static int key1(int hash) {
		return (hash >> 12) & 0xF;
	}

	public static int key2(int hash) {
		return (hash >> 8) & 0xF;
	}

	public static int key3(int hash) {
		return (hash >> 4) & 0xF;
	}

	public static int key4(int hash) {
		return hash & 0xF;
	}
}
