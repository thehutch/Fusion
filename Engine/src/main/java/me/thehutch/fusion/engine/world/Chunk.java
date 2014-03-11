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

import me.thehutch.fusion.api.world.IChunk;

/**
 * @author thehutch
 */
public class Chunk implements IChunk {
	private final Block[] blocks;

	public Chunk() {
		this.blocks = new Block[BLOCKS.VOLUME];
	}

	protected Chunk(Block[] blocks) {
		this.blocks = blocks;
	}

	@Override
	public Block getBlockAt(int bx, int by, int bz) {
		return blocks[(bx << BLOCKS.DOUBLE_BITS) | (by << BLOCKS.BITS) | bz];
	}

	public Block[] getBlocks() {
		return blocks;
	}
}
