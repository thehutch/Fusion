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
/*
 * This file is part of FusionEngine.
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
package me.thehutch.fusion.engine.world;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import java.util.logging.Level;
import me.thehutch.fusion.api.maths.vector.Vector3;
import me.thehutch.fusion.api.util.hashing.LongTripleHash;
import me.thehutch.fusion.api.world.IWorld;
import me.thehutch.fusion.engine.Engine;

public class World implements IWorld {
	private final TLongObjectMap<Chunk> loadedChunks = new TLongObjectHashMap<>(3 * 3 * 3, 0.5f);
	private final Engine engine;
	private final String name;

	public World(String name, Engine engine) {
		if (!name.matches("^[A-Za-z0-9_]+")) {
			final String newName = Long.toHexString(System.nanoTime());
			engine.getLogger().log(Level.WARNING, "World name '{0}' is invalid, using '{1}' instead.", new Object[] { name, newName });
			this.name = newName;
		} else {
			this.name = name;
		}
		this.engine = engine;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public final Block getBlock(Vector3 vec) {
		return getBlock(vec.getFloorX(), vec.getFloorY(), vec.getFloorZ());
	}

	@Override
	public final Block getBlock(int wx, int wy, int wz) {
		final int shift = Chunk.BLOCKS.BITS;
		final int mod = Chunk.BLOCKS.SIZE;
		final Chunk chunk = getChunk(wx >> shift, wy >> shift, wx >> shift);
		return chunk.getBlockAt(wx % mod, wy % mod, wz % mod);
	}

	@Override
	public final Chunk getChunk(Vector3 position) {
		return getChunk(position.getFloorX(), position.getFloorY(), position.getFloorZ());
	}

	@Override
	public final Chunk getChunk(int cx, int cy, int cz) {
		Chunk chunk = loadedChunks.get(LongTripleHash.hash(cx, cy, cz));
		if (chunk != null) {
			// Chunk is already loaded
			return chunk;
		}
		// Create a new chunk
		return loadChunk(cx, cy, cz);
	}

	@Override
	public Engine getEngine() {
		return engine;
	}

	public final Chunk[] getChunks() {
		return loadedChunks.values(new Chunk[loadedChunks.size()]);
	}

	private Chunk loadChunk(int cx, int cy, int cz) {
		final Block[] blocks = new Block[Chunk.BLOCKS.VOLUME];
		final int shift = Chunk.BLOCKS.BITS;
		final int doubleShift = Chunk.BLOCKS.DOUBLE_BITS;
		final int mask = Chunk.BLOCKS.MASK;
		for (int i = 0; i < Chunk.BLOCKS.VOLUME; ++i) {
			final int x = (i >> doubleShift) & mask;
			final int y = (i >> shift) & mask;
			final int z = i & mask;

			System.out.println("New Block At: [" + x + ", " + y + ", " + z + "]");

			//blocks[i] = new Block();
			//blocks[i].setMaterial(Material.AIR);
		}
		final Chunk chunk = new Chunk(blocks);
		this.loadedChunks.put(LongTripleHash.hash(cx, cy, cz), chunk);
		return chunk;
	}
}
