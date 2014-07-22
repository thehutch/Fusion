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
package me.thehutch.fusion.engine.render.data;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import java.util.Iterator;

/**
 * @author thehutch
 */
public class UniformHolder implements Iterable<Uniform> {
	private final TMap<String, Uniform> uniforms = new THashMap<>();

	public UniformHolder() {
	}

	public <U extends Uniform> U get(String name) {
		final Uniform uniform = uniforms.get(name);
		if (uniform == null) {
			return null;
		}
		return (U) uniform;
	}

	public void add(Uniform uniform) {
		this.uniforms.put(uniform.name, uniform);
	}

	public void addAll(UniformHolder holder) {
		holder.forEach((Uniform uniform) -> {
			add(uniform);
		});
	}

	public void remove(Uniform uniform) {
		remove(uniform.name);
	}

	public void remove(String name) {
		this.uniforms.remove(name);
	}

	public boolean has(String name) {
		return uniforms.containsKey(name);
	}

	public void clear() {
		this.uniforms.clear();
	}

	@Override
	public Iterator<Uniform> iterator() {
		return uniforms.values().iterator();
	}
}
