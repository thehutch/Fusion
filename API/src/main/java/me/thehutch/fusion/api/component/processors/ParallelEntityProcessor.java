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
import me.thehutch.fusion.api.component.IEntity;
import me.thehutch.fusion.api.util.container.ImmutableBag;

/**
 * A typical entity processor. Use this to process entities in parallel.
 *
 * @author thehutch
 */
public abstract class ParallelEntityProcessor extends EntityProcessor {

	/**
	 * Default constructor for {@link ParallelEntityProcessor}.
	 * <p>
	 * @param aspect The aspect for this processor
	 */
	public ParallelEntityProcessor(Aspect aspect) {
		super(aspect);
	}

	/**
	 * Process an entity this processor is interested in.
	 *
	 * @param entity The entity to process
	 */
	public abstract void process(IEntity entity);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void processEntities(ImmutableBag<IEntity> entities) {
		entities.parallelStream().forEach(this::process);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean checkProcessing() {
		return true;
	}
}
