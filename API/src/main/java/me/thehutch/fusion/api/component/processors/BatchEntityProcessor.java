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
package me.thehutch.fusion.api.component.processors;

import me.thehutch.fusion.api.component.Aspect;
import me.thehutch.fusion.api.component.EntityProcessor;

/**
 * An {@link EntityProcessor} which can process the entities multiple times.
 * <p>
 * A useful example to use this would be in rendering for multi-pass rendering.
 * <p>
 * @author thehutch
 */
public abstract class BatchEntityProcessor extends EntityProcessor {

	/**
	 * Default constructor for {@link BatchEntityProcessor}.
	 * <p>
	 * @param aspect The aspect for this processor
	 */
	public BatchEntityProcessor(Aspect aspect) {
		super(aspect);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean checkProcessing() {
		return true;
	}
}
