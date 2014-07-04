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
package me.thehutch.fusion.api.component;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.util.BitSet;
import me.thehutch.fusion.api.util.container.Bag;
import me.thehutch.fusion.api.util.container.ImmutableBag;

/**
 * @author thehutch
 */
public abstract class EntityProcessor implements EntityObserver {
	private final Bag<Entity> actives = new Bag<>();
	private final Aspect aspect;
	private final int index;
	private boolean passive;
	protected ComponentSystem system;

	public EntityProcessor(Aspect aspect) {
		this.index = ProcessorIndexManager.getIndexFor(getClass());
		this.aspect = aspect;
	}

	@Override
	public final void added(Entity e) {
		check(e);
	}

	@Override
	public final void deleted(Entity e) {
		if (e.getProcessorBits().get(index)) {
			removeFromProcessor(e);
		}
	}

	@Override
	public final void changed(Entity e) {
		check(e);
	}

	@Override
	public final void enabled(Entity e) {
		check(e);
	}

	@Override
	public final void disabled(Entity e) {
		if (e.getProcessorBits().get(index)) {
			removeFromProcessor(e);
		}
	}

	public final void process() {
		if (checkProcessing()) {
			begin();
			processEntities(actives);
			end();
		}
	}

	protected final boolean isPassive() {
		return passive;
	}

	protected final void setPassive(boolean passive) {
		this.passive = passive;
	}

	public final ImmutableBag<Entity> getActives() {
		return actives;
	}

	protected abstract void initialise();

	protected abstract void begin();

	protected abstract void end();

	protected abstract boolean checkProcessing();

	protected abstract void processEntities(ImmutableBag<Entity> entities);

	protected abstract void inserted(Entity e);

	protected abstract void removed(Entity e);

	protected final void check(Entity e) {
		final boolean contains = e.getProcessorBits().get(index);
		boolean interested = true;

		final BitSet componentBits = e.getComponentBits();

		// Check if the entity possesses all of the components defined in the aspect.
		final BitSet allSet = aspect.getAllSet();
		if (!allSet.isEmpty()) {
			for (int i = allSet.nextSetBit(0); i >= 0; i = allSet.nextSetBit(i + 1)) {
				if (!componentBits.get(i)) {
					interested = false;
					break;
				}
			}
		}

		// Check if the entity possesses ANY of the exclusion components, it it does then the processor is not interested.
		final BitSet exclusionSet = aspect.getExclusionSet();
		if (!exclusionSet.isEmpty() && interested) {
			interested = !exclusionSet.intersects(componentBits);
		}

		// Check if the entity possesses ANY of the components in the one set. If so, the processor is interested.
		final BitSet oneSet = aspect.getOneSet();
		if (!oneSet.isEmpty()) {
			interested = oneSet.intersects(componentBits);
		}

		if (interested && !contains) {
			insertToProcessor(e);
		} else if (!interested && contains) {
			removeFromProcessor(e);
		}
	}

	protected final void setSystem(ComponentSystem system) {
		this.system = system;
	}

	private void insertToProcessor(Entity e) {
		e.getProcessorBits().set(index);
		this.actives.add(e);
		inserted(e);
	}

	private void removeFromProcessor(Entity e) {
		e.getProcessorBits().clear(index);
		this.actives.remove(e);
		removed(e);
	}

	private static class ProcessorIndexManager {
		private static final TObjectIntMap<Class<? extends EntityProcessor>> INDICES = new TObjectIntHashMap<>(4, 0.85f, -1);
		private static int INDEX;

		private ProcessorIndexManager() {
		}

		private static int getIndexFor(Class<? extends EntityProcessor> es) {
			int index = INDICES.get(es);
			if (index == INDICES.getNoEntryValue()) {
				index = INDEX++;
				INDICES.put(es, index);
			}
			return index;
		}
	}
}
