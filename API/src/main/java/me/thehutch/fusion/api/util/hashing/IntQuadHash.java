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
/*
 * This file is part of FusionAPI.
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
import me.thehutch.fusion.api.maths.vector.Vector4;

/**
 * @author thehutch
 */
public class IntQuadHash {
	private IntQuadHash() {
	}

	public static int hash(Vector2 vec) {
		return hash(vec.getFloorX(), vec.getFloorY(), 0, 0);
	}

	public static int hash(Vector3 vec) {
		return hash(vec.getFloorX(), vec.getFloorY(), vec.getFloorZ(), 0);
	}

	public static int hash(Vector4 vec) {
		return hash(vec.getFloorX(), vec.getFloorY(), vec.getFloorZ(), vec.getFloorW());
	}

	public static int hash(int x, int y, int z, int w) {
		return (x & 0xFF) << 24 | (y & 0xFF) << 16 | (z & 0xFF) << 8 | w & 0xFF;
	}

	public static int key1(int hash) {
		return (hash >> 24) & 0xFF;
	}

	public static int key2(int hash) {
		return (hash >> 16) & 0xFF;
	}

	public static int key3(int hash) {
		return (hash >> 8) & 0xFF;
	}

	public static int key4(int hash) {
		return hash & 0xFF;
	}
}
