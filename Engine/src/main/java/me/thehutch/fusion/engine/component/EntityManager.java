/*
 * This file is part of Engine, licensed under the Apache 2.0 License.
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
package me.thehutch.fusion.engine.component;

import java.util.BitSet;
import me.thehutch.fusion.api.component.IEntity;
import me.thehutch.fusion.api.component.Manager;
import me.thehutch.fusion.api.util.container.Bag;

/**
 * @author thehutch
 */
public final class EntityManager extends Manager {
	private final IdentifierPool mIdentifierPool;
	private final Bag<IEntity> mEntities;
	private final BitSet mDisabled;
	private long mCreated;
	private long mDeleted;
	private long mAdded;
	private int mActive;

	/**
	 * Default constructor for {@link EntityManager}.
	 */
	public EntityManager() {
		mIdentifierPool = new IdentifierPool();
		mEntities = new Bag<>();
		mDisabled = new BitSet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialise() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void added(IEntity e) {
		final int entityID = e.getId();

		mEntities.set(entityID, e);
		++mActive;
		++mAdded;
	}

	@Override
	public void deleted(IEntity e) {
		final int entityID = e.getId();
		mEntities.set(entityID, null);
		// Disable the entity
		mDisabled.clear(entityID);
		// Add the id back to the available pool
		mIdentifierPool.checkIn(entityID);
		// Update statistics
		--mActive;
		++mDeleted;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enabled(IEntity e) {
		mDisabled.clear(e.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disabled(IEntity e) {
		mDisabled.set(e.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changed(IEntity e) {
	}

	/**
	 * Returns true if the given entity id is active.
	 * <p>
	 * @param entityId The entity id
	 * <p>
	 * @return True if this entity is active
	 */
	public boolean isActive(int entityId) {
		return mEntities.get(entityId) != null;
	}

	/**
	 * Returns true if the given entity id is enabled.
	 * <p>
	 * @param entityId The entity id
	 * <p>
	 * @return True if this entity is enabled
	 */
	public boolean isEnabled(int entityId) {
		return !mDisabled.get(entityId);
	}

	/**
	 * @return The total number of entities added to the system
	 */
	public long getTotalAdded() {
		return mAdded;
	}

	/**
	 * @return The total number of entities created in the system
	 */
	public long getTotalCreated() {
		return mCreated;
	}

	/**
	 * @return The total number of entities deleted from the system
	 */
	public long getTotalDeleted() {
		return mDeleted;
	}

	/**
	 * @return The total number of currently active entities in the system
	 */
	public int getActiveEntityCount() {
		return mActive;
	}

	/**
	 * Returns the entity with the given id.
	 * <p>
	 * @param entityId The entity id
	 * <p>
	 * @return The entity, null if not found
	 */
	public IEntity getEntity(int entityId) {
		return mEntities.get(entityId);
	}

	/**
	 * Creates a new {@link Entity}.
	 * <p>
	 * @return A new {@link Entity}
	 */
	public Entity createEntityInstance() {
		// Get the component system
		final ComponentSystem componentSystem = (ComponentSystem) getSystem();
		// Get the entity's id
		final int entityID = mIdentifierPool.checkOut();

		// Increment the number of created entities
		++mCreated;

		// Return the new Entity
		return new Entity(componentSystem, entityID);
	}

	/**
	 * A pool of currently available and used entity id's.
	 */
	private static class IdentifierPool {
		private final Bag<Integer> mIDs;
		private int mNextAvailableID;

		/**
		 * Private constructor for {@link IdentifierPool}.
		 */
		private IdentifierPool() {
			mIDs = new Bag<>();
			mNextAvailableID = 0;
		}

		/**
		 * Used to return an entity id to the pool once
		 * that entity has been deleted.
		 * <p>
		 * @param id The id to return
		 */
		public void checkIn(int id) {
			mIDs.add(id);
		}

		/**
		 * Returns the next valid entity id to be used for
		 * a new {@link Entity}.
		 * <p>
		 * @return An entity id
		 */
		public int checkOut() {
			final Bag<Integer> ids = mIDs;
			if (ids.size() > 0) {
				return ids.removeLast();
			}
			return mNextAvailableID++;
		}
	}
}
