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
public abstract class EntityProcessor implements IEntityObserver {
	private final Bag<IEntity> mActives = new Bag<>();
	private final Aspect mAspect;
	private final int mIndex;
	private boolean mIsPassive;
	protected IComponentSystem mSystem;

	/**
	 * Default constructor for {@link EntityProcessor}.
	 * <p>
	 * @param aspect The aspect for this processor
	 */
	public EntityProcessor(Aspect aspect) {
		mIndex = ProcessorIndexManager.getIndexFor(getClass());
		mAspect = aspect;
		mSystem = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void added(IEntity e) {
		check(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void deleted(IEntity e) {
		final BitSet processorBits = e.getProcessorBits();
		if (processorBits.get(mIndex)) {
			// Remove the entity from the processor
			processorBits.clear(mIndex);
			mActives.remove(e);
			removed(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void changed(IEntity e) {
		check(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void enabled(IEntity e) {
		check(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void disabled(IEntity e) {
		final BitSet processorBits = e.getProcessorBits();
		if (processorBits.get(mIndex)) {
			// Remove the entity from the processor
			processorBits.clear(mIndex);
			mActives.remove(e);
			removed(e);
		}
	}

	/**
	 * Called when this {@link EntityProcessor} is processed.
	 */
	public final void process() {
		if (checkProcessing()) {
			begin();
			processEntities(mActives);
			end();
		}
	}

	/**
	 * @return True if this {@link EntityProcessor} is mIsPassive
	 */
	public final boolean isPassive() {
		return mIsPassive;
	}

	/**
	 * Sets the mIsPassive state of this {@link EntityProcessor}.
	 * <p>
	 * @param passive True to set this {@link EntityProcessor} as mIsPassive
	 */
	public final void setPassive(boolean passive) {
		mIsPassive = passive;
	}

	/**
	 * Sets the {@link IComponentSystem} of this {@link EntityProcessor}.
	 * <p>
	 * @param system The component system
	 */
	public final void setSystem(IComponentSystem system) {
		if (mSystem != null) {
			throw new IllegalStateException("Can not set processor system more than once");
		}
		mSystem = system;
	}

	/**
	 * Returns an {@link ImmutableBag} of all the currently active entities
	 * being processed by this {@link EntityProcessor}.
	 * <p>
	 * @return An {@link ImmutableBag} of entities
	 */
	public final ImmutableBag<IEntity> getActives() {
		return mActives;
	}

	/**
	 * Called when this {@link EntityProcessor} is initialied.
	 */
	public abstract void initialise();

	/**
	 * Called when this {@link EntityProcessor} starts it's processing.
	 */
	public abstract void begin();

	/**
	 * Called when this {@link EntityProcessor} ends it's processing.
	 */
	public abstract void end();

	/**
	 * Called to process the entities in this {@link EntityProcessor}.
	 * <p>
	 * @param entities The entities to process
	 */
	public abstract void processEntities(ImmutableBag<IEntity> entities);

	/**
	 * Called to check if this {@link EntityProcessor} should process.
	 * <p>
	 * @return True if this {@link EntityProcessor} should process
	 */
	public abstract boolean checkProcessing();

	/**
	 * Called when an entity is inserted into this {@link EntityProcessor}.
	 * <p>
	 * @param e The entity inserted
	 */
	public abstract void inserted(IEntity e);

	/**
	 * Called when an entity us removed from this {@link EntityProcessor}.
	 * <p>
	 * @param e The entity removed
	 */
	public abstract void removed(IEntity e);

	/**
	 * Checks whether an Entity should be inserted or removed from the {@link EntityProcessor}.
	 * <p>
	 * @param e The entity to check
	 */
	private void check(IEntity e) {
		final boolean contains = e.getProcessorBits().get(mIndex);
		boolean interested = true;

		final BitSet componentBits = e.getComponentBits();

		// Check if the entity possesses all of the components defined in the aspect.
		final BitSet allSet = mAspect.getAllSet();
		if (!allSet.isEmpty()) {
			for (int i = allSet.nextSetBit(0); i >= 0; i = allSet.nextSetBit(i + 1)) {
				if (!componentBits.get(i)) {
					interested = false;
					break;
				}
			}
		}

		// Check if the entity possesses ANY of the exclusion components, it it does then the processor is not interested.
		final BitSet exclusionSet = mAspect.getExclusionSet();
		if (!exclusionSet.isEmpty() && interested) {
			interested = !exclusionSet.intersects(componentBits);
		}

		// Check if the entity possesses ANY of the components in the one set. If so, the processor is interested.
		final BitSet oneSet = mAspect.getOneSet();
		if (!oneSet.isEmpty()) {
			interested = oneSet.intersects(componentBits);
		}

		if (interested && !contains) {
			// Insert the entity into the processor
			e.getProcessorBits().set(mIndex);
			mActives.add(e);
			inserted(e);
		} else if (!interested && contains) {
			// Remove the entity from the processor
			e.getProcessorBits().clear(mIndex);
			mActives.remove(e);
			removed(e);
		}
	}

	/**
	 * Class used to help with the id of the {@link EntityProcessor}'s.
	 */
	private static class ProcessorIndexManager {
		private static final TObjectIntMap<Class<? extends EntityProcessor>> INDICES = new TObjectIntHashMap<>(17, 0.85f, -1);
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
