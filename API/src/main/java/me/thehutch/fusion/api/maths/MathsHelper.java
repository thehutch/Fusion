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
package me.thehutch.fusion.api.maths;

/**
 * @author thehutch
 */
public class MathsHelper {
	public static final float PI = 3.141592653f;
	private static final float DEG_TO_RAD = PI / 180.0f;
	private static final float RAD_TO_DEG = 180.0f / PI;

	private MathsHelper() {
	}

	public static float toRadians(float degrees) {
		return degrees * DEG_TO_RAD;
	}

	public static float toDegrees(float radians) {
		return radians * RAD_TO_DEG;
	}

	public static int abs(int value) {
		return value < 0 ? -value : value;
	}

	public static float abs(float value) {
		return value < +0.0f ? -value : value;
	}

	public static int floor(float value) {
		final int x = (int) value;
		return value < x ? x - 1 : x;
	}

	public static int ceil(float value) {
		return floor(value) + 1;
	}

	public static int round(float value) {
		return floor(value + 0.5f);
	}

	public static int max(int v1, int v2) {
		return v1 > v2 ? v1 : v2;
	}

	public static int min(int v1, int v2) {
		return v1 < v2 ? v1 : v2;
	}

	public static float max(float v1, float v2) {
		return v1 > v2 ? v1 : v2;
	}

	public static float min(float v1, float v2) {
		return v1 < v2 ? v1 : v2;
	}

	public static int clamp(int value, int min, int max) {
		return min(max(value, min), max);
	}

	public static float clamp(float value, float min, float max) {
		return min(max(value, min), max);
	}

	public static float lerp(float a, float b, float percentage) {
		return a + (b - a) * percentage;
	}
}
