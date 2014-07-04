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

import java.util.BitSet;
import me.thehutch.fusion.api.util.container.Bag;

/**
 * @author thehutch
 */
public class EntityManager extends Manager {
	private final IdentifierPool identifierPool = new IdentifierPool();
	private final Bag<Entity> entities = new Bag<>();
	private final BitSet disabled = new BitSet();

	private long created;
	private long deleted;
	private long added;
	private int active;

	@Override
	public void added(Entity e) {
		this.entities.set(e.getId(), e);
		++active;
		++added;
	}

	@Override
	public void deleted(Entity e) {
		this.entities.set(e.getId(), null);
		// Disable the entity
		this.disabled.clear(e.getId());
		// Add the id back to the available pool
		this.identifierPool.checkIn(e.getId());
		// Update statistics
		--active;
		++deleted;
	}

	@Override
	public void enabled(Entity e) {
		this.disabled.clear(e.getId());
	}

	@Override
	public void disabled(Entity e) {
		this.disabled.set(e.getId());
	}

	@Override
	public void changed(Entity e) {
	}

	public boolean isActive(int entityId) {
		return entities.get(entityId) != null;
	}

	public boolean isEnabled(int entityId) {
		return !disabled.get(entityId);
	}

	public long getTotalAdded() {
		return added;
	}

	public long getTotalCreated() {
		return created;
	}

	public long getTotalDeleted() {
		return deleted;
	}

	public int getActiveEntityCount() {
		return active;
	}

	protected Entity getEntity(int entityId) {
		return entities.get(entityId);
	}

	protected Entity createEntityInstance() {
		final Entity e = new Entity(getSystem(), identifierPool.checkOut());
		++created;
		return e;
	}

	@Override
	protected void initialise() {
	}

	private class IdentifierPool {
		private final Bag<Integer> ids = new Bag<>();
		private int nextAvailableId;

		private IdentifierPool() {
			this.nextAvailableId = 0;
		}

		public void checkIn(int id) {
			this.ids.add(id);
		}

		public int checkOut() {
			if (ids.size() > 0) {
				return ids.removeLast();
			}
			return nextAvailableId++;
		}
	}
}
