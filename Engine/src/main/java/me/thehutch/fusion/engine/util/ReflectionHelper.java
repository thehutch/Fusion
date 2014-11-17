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
package me.thehutch.fusion.engine.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * @author thehutch
 */
public final class ReflectionHelper {

	private ReflectionHelper() {
	}

	public static <T> Class<T> getGenericClass() {
		return new ClassType<T>().getClassType();
	}

	private static class ClassType<T> {
		private final Class<T> mClassType;

		private ClassType() {
			try {
				final Field field = getClass().getDeclaredField("mClassType");
				final ParameterizedType type = (ParameterizedType) field.getGenericType();
				mClassType = (Class<T>) type.getActualTypeArguments()[0].getClass();
			} catch (NoSuchFieldException | SecurityException ex) {
				throw new IllegalStateException("Unable to locate field", ex);
			}
		}

		public Class<T> getClassType() {
			return mClassType;
		}
	}
}
