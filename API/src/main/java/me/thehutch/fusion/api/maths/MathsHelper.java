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
package me.thehutch.fusion.api.maths;

/**
 * @author thehutch
 */
public class MathsHelper {
	public static final float E = 2.718281828f;
	public static final float PI = 3.141592653f;
	private static final float DEG_TO_RAD = PI / 180.0f;
	private static final float RAD_TO_DEG = 180.0f / PI;

	private MathsHelper() {
	}

	/**
	 * Converts the angle given in degrees to radians.
	 *
	 * @param degrees The angle in degrees
	 *
	 * @return The angle in radians
	 */
	public static float toRadians(float degrees) {
		return degrees * DEG_TO_RAD;
	}

	/**
	 * Converts the angle given in radians to degrees.
	 *
	 * @param radians The angle in radians
	 *
	 * @return The angle in degrees
	 */
	public static float toDegrees(float radians) {
		return radians * RAD_TO_DEG;
	}

	/**
	 * Returns the absolute value of the value given.
	 *
	 * @param value The value
	 *
	 * @return The absolute value
	 */
	public static int abs(int value) {
		return value < 0 ? -value : value;
	}

	/**
	 * Returns the absolute value of the value given.
	 *
	 * @param value The value
	 *
	 * @return The absolute value
	 */
	public static float abs(float value) {
		return value < +0.0f ? -value : value;
	}

	/**
	 * Rounds down the value given.
	 *
	 * @param value The value
	 *
	 * @return The rounded down value
	 */
	public static int floor(float value) {
		final int x = (int) value;
		return value < x ? x - 1 : x;
	}

	/**
	 * Rounds up the value given.
	 *
	 * @param value The value
	 *
	 * @return The rounded up value
	 */
	public static int ceil(float value) {
		return floor(value) + 1;
	}

	/**
	 * Rounds the value given to the nearest integer.
	 *
	 * @param value The value
	 *
	 * @return The rounded value
	 */
	public static int round(float value) {
		return floor(value + 0.5f);
	}

	/**
	 * Returns the largest of the two values given
	 *
	 * @param v1 The first value
	 * @param v2 The second value
	 *
	 * @return The largest value
	 */
	public static int max(int v1, int v2) {
		return v1 > v2 ? v1 : v2;
	}

	/**
	 * Returns the smallest of the two values given
	 *
	 * @param v1 The first value
	 * @param v2 The second value
	 *
	 * @return The smallest value
	 */
	public static int min(int v1, int v2) {
		return v1 < v2 ? v1 : v2;
	}

	/**
	 * Returns the largest of the two values given
	 *
	 * @param v1 The first value
	 * @param v2 The second value
	 *
	 * @return The largest value
	 */
	public static float max(float v1, float v2) {
		return v1 > v2 ? v1 : v2;
	}

	/**
	 * Returns the smallest of the two values given
	 *
	 * @param v1 The first value
	 * @param v2 The second value
	 *
	 * @return The smallest value
	 */
	public static float min(float v1, float v2) {
		return v1 < v2 ? v1 : v2;
	}

	/**
	 * Clamps the value given between the min and max values.
	 *
	 * @param value The value
	 * @param min   The min boundary
	 * @param max   The max boundary
	 *
	 * @return The clamped value
	 */
	public static int clamp(int value, int min, int max) {
		return min(max(value, min), max);
	}

	/**
	 * Clamps the value given between the min and max values.
	 *
	 * @param value The value
	 * @param min   The min boundary
	 * @param max   The max boundary
	 *
	 * @return The clamped value
	 */
	public static float clamp(float value, float min, float max) {
		return min(max(value, min), max);
	}

	/**
	 * Wraps the value given between the min and max values.
	 *
	 * @param value The value
	 * @param min   The min boundary
	 * @param max   The max boundary
	 *
	 * @return The wrapped value
	 */
	public static int wrap(int value, int min, int max) {
		return value < min ? max : value > max ? min : value;
	}

	/**
	 * Wraps the value given between the min and max values.
	 *
	 * @param value The value
	 * @param min   The min boundary
	 * @param max   The max boundary
	 *
	 * @return The wrapped value
	 */
	public static float wrap(float value, float min, float max) {
		return value < min ? max : value > max ? min : value;
	}

	/**
	 * Remaps the value given from the current range to a new range.
	 *
	 * @param value The value to be remapped
	 * @param min1  The min boundary of the current range
	 * @param max1  The max boundary of the current range
	 * @param min2  The min boundary of the new range
	 * @param max2  The max boundary of the new range
	 *
	 * @return The remapped value
	 */
	public static int remap(int value, int min1, int max1, int min2, int max2) {
		return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
	}

	/**
	 * Remaps the value given from the current range to a new range.
	 *
	 * @param value The value to be remapped
	 * @param min1  The min boundary of the current range
	 * @param max1  The max boundary of the current range
	 * @param min2  The min boundary of the new range
	 * @param max2  The max boundary of the new range
	 *
	 * @return The remapped value
	 */
	public static float remap(float value, float min1, float max1, float min2, float max2) {
		return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
	}

	/**
	 * Interpolates between the two values given.
	 *
	 * @param a          The start value
	 * @param b          The end value
	 * @param percentage The amount of interpolate by
	 *
	 * @return The interpolated value
	 */
	public static float lerp(float a, float b, float percentage) {
		return a + (b - a) * percentage;
	}

	public static Quaternion slerp(Quaternion a, Quaternion b, float percent) {
		final float EPSILON = 1e-3f;
		final float inverted;
		float cos = a.dot(b);
		if (cos < 0.0f) {
			cos = -cos;
			inverted = -1.0f;
		} else {
			inverted = 1.0f;
		}

		if (1 - cos < EPSILON) {
			return a.mul(1 - percent).add(b.mul(percent * inverted));
		}

		final float theta = (float) Math.acos(cos);
		final float sinTheta = (float) Math.sin(theta);

		final float coeff1 = (float) (Math.sin((1 - percent) * theta) / sinTheta);
		final float coeff2 = (float) (Math.sin(percent * theta) / sinTheta * inverted);
		return a.mul(coeff1).add(b.mul(coeff2));
	}

	/**
	 * Calculates the square root of the given value. Uses a fast implementation
	 * to calculate the square root so accuracy is limited.
	 *
	 * @param x The value
	 *
	 * @return The square root of the value
	 */
	public static float sqrt(float x) {
		return x * inverseSqrt(x);
	}

	/**
	 * Calculates the inverse square root of the given value.
	 *
	 * @param value The value
	 *
	 * @return The inverse square root of the value
	 */
	private static float inverseSqrt(float value) {
		final float xhalves = 0.5f * value;
		final float x = Float.intBitsToFloat(0x5F3759DF - (Float.floatToRawIntBits(value) >> 1));
		return x * (1.5f - xhalves * x * x);
	}
}
