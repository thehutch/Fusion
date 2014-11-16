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
import java.util.UUID;
import me.thehutch.fusion.api.component.ComponentType;
import me.thehutch.fusion.api.component.IComponent;
import me.thehutch.fusion.api.component.IComponentSystem;
import me.thehutch.fusion.api.component.IEntity;
import me.thehutch.fusion.api.util.container.Bag;

/**
 * @author thehutch
 */
public final class Entity implements IEntity {
	private final ComponentManager mComponentManager;
	private final EntityManager mEntityManager;
	private final IComponentSystem mSystem;
	private final BitSet mComponentBits;
	private final BitSet mProcessorBits;
	private final UUID mUUID;
	private final int mID;

	public Entity(ComponentSystem system, int id) {
		mComponentManager = system.getComponentManager();
		mEntityManager = system.getEntityManager();
		mSystem = system;

		mComponentBits = new BitSet();
		mProcessorBits = new BitSet();

		mUUID = UUID.randomUUID();
		mID = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getId() {
		return mID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID getUUID() {
		return mUUID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActive() {
		return mEntityManager.isActive(mID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled() {
		return mEntityManager.isEnabled(mID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BitSet getComponentBits() {
		return mComponentBits;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BitSet getProcessorBits() {
		return mProcessorBits;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enable() {
		mSystem.enable(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disable() {
		mSystem.disable(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IComponent getComponent(ComponentType type) {
		return mComponentManager.getComponent(this, type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends IComponent> T getComponent(Class<T> type) {
		return type.cast(getComponent(ComponentType.getTypeFor(type)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getComponents(Bag<IComponent> fillbag) {
		mComponentManager.getComponentsFor(this, fillbag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Entity addComponent(IComponent component) {
		return addComponent(component, ComponentType.getTypeFor(component.getClass()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Entity addComponent(IComponent component, ComponentType type) {
		mComponentManager.addComponent(this, type, component);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Entity removeComponent(IComponent component) {
		return removeComponent(ComponentType.getTypeFor(component.getClass()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Entity removeComponent(Class<? extends IComponent> type) {
		return removeComponent(ComponentType.getTypeFor(type));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Entity removeComponent(ComponentType type) {
		mComponentManager.removeComponent(this, type);
		return this;
	}

//	private void addToSystem() {
//		mSystem.addEntity(this);
//	}
//
//	private void deleteFromSystem() {
//		mSystem.deleteEntity(this);
//	}
//
//	private void changedInSystem() {
//		mSystem.changeEntity(this);
//	}
}
