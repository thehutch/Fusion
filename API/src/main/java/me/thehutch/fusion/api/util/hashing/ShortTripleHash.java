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

/**
 * @author thehutch
 */
public class ShortTripleHash {
	private ShortTripleHash() {
	}

	public static short hash(Vector2 vec) {
		return hash(vec.getFloorX(), vec.getFloorY(), 0);
	}

	public static short hash(Vector3 vec) {
		return hash(vec.getFloorX(), vec.getFloorY(), vec.getFloorZ());
	}

	public static short hash(int x, int y, int z) {
		return (short) ((x & 0x1F) << 11 | (y & 0x1F) << 6 | z & 0x1F);
	}

	public static int key1(int hash) {
		return (hash >> 11) & 0x1F;
	}

	public static int key2(int hash) {
		return (hash >> 6) & 0x1F;
	}

	public static int key3(int hash) {
		return hash & 0x1F;
	}
}
