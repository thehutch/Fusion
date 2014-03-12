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
package me.thehutch.fusion.api.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author thehutch
 */
public class ReflectionHelper {

	private ReflectionHelper() {
	}

	/**
	 * Gets all the declared methods found in the class with the specified annotation.
	 *
	 * @param clazz The class containing the methods
	 * @param annotationClass The annotation to test for
	 *
	 * @return A collection of methods with the annotation
	 */
	public static Collection<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		final Collection<Method> annotatedMethods = new ArrayList<>();
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getAnnotation(annotationClass) != null) {
				annotatedMethods.add(method);
			}
		}
		return annotatedMethods;
	}

	/**
	 * Checks to see if the method given has the specified parameters.
	 * The parameters have to be in the same order as you expect them to be in the method.
	 *
	 * This method allows for subclasses to be passed in a parameters, therefore passing in
	 * {@code Object.class} will allow any parameter.
	 *
	 * @param method The method to test
	 * @param parameterClasses The classes of the parameter types
	 *
	 * @return True if the method parameters match
	 */
	public static boolean hasExactParameters(Method method, Class<?>... parameterClasses) {
		final Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterClasses.length != parameterTypes.length) {
			return false;
		}
		for (int i = 0; i < parameterClasses.length; ++i) {
			if (!parameterClasses[i].isAssignableFrom(parameterTypes[i])) {
				return false;
			}
		}
		return true;
	}
}
