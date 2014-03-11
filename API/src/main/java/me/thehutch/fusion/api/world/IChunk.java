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
package me.thehutch.fusion.api.world;

import me.thehutch.fusion.api.maths.BitSize;

/**
 * @author thehutch
 */
public interface IChunk {
	public static final BitSize BLOCKS = new BitSize(4);

	/**
	 * Gets the {@link IBlock} at this position in block coordinates.<\br>
	 * Coordinates range between 0 -> BLOCKS.SIZE - 1.
	 *
	 * @param bx x-axis block coordinate
	 * @param by y-axis block coordinate
	 * @param bz z-axis block coordinate
	 *
	 * @return The block at this position
	 */
	public IBlock getBlockAt(int bx, int by, int bz);
}
