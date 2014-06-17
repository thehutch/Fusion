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
package me.thehutch.fusion.api.component;

import java.util.BitSet;
import java.util.UUID;
import me.thehutch.fusion.api.util.container.Bag;

/**
 * @author thehutch
 */
public final class Entity {
	private final ComponentManager componentManager;
	private final EntityManager entityManager;
	private final System system;

	private final BitSet componentBits;
	private final BitSet processorBits;

	private final int id;
	private UUID uuid;

	protected Entity(System system, int id) {
		this.componentManager = system.getComponentManager();
		this.entityManager = system.getEntityManager();
		this.system = system;

		this.componentBits = new BitSet();
		this.processorBits = new BitSet();

		this.id = id;

		reset();
	}

	public int getId() {
		return id;
	}

	public UUID getUUID() {
		return uuid;
	}

	public System getSystem() {
		return system;
	}

	public boolean isActive() {
		return entityManager.isActive(id);
	}

	public boolean isEnabled() {
		return entityManager.isEnabled(id);
	}

	public void addToSystem() {
		this.system.addEntity(this);
	}

	public void deleteFromSystem() {
		this.system.deleteEntity(this);
	}

	public void changedInSystem() {
		this.system.changeEntity(this);
	}

	public void enable() {
		this.system.enable(this);
	}

	public void disable() {
		this.system.disable(this);
	}

	public Component getComponent(ComponentType type) {
		return componentManager.getComponent(this, type);
	}

	public <T extends Component> T getComponent(Class<T> type) {
		return type.cast(getComponent(ComponentType.getTypeFor(type)));
	}

	public Bag<Component> getComponents(Bag<Component> fillbag) {
		return componentManager.getComponentsFor(this, fillbag);
	}

	public Entity addComponent(Component component) {
		return addComponent(component, ComponentType.getTypeFor(component.getClass()));
	}

	public Entity addComponent(Component component, ComponentType type) {
		this.componentManager.addComponent(this, type, component);
		return this;
	}

	public Entity removeComponent(Component component) {
		return removeComponent(ComponentType.getTypeFor(component.getClass()));
	}

	public Entity removeComponent(Class<? extends Component> type) {
		return removeComponent(ComponentType.getTypeFor(type));
	}

	public Entity removeComponent(ComponentType type) {
		this.componentManager.removeComponent(this, type);
		return this;
	}

	protected BitSet getComponentBits() {
		return componentBits;
	}

	protected BitSet getProcessorBits() {
		return processorBits;
	}

	protected void reset() {
		this.uuid = UUID.randomUUID();
		this.componentBits.clear();
		this.processorBits.clear();
	}

	@Override
	public String toString() {
		return "Entity[" + id + "]";
	}
}
