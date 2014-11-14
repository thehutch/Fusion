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
public final class Vector4 {
	public static final Vector4 ONE = new Vector4(1.0f, 1.0f, 1.0f, 1.0f);
	public static final Vector4 ZERO = new Vector4(0.0f, 0.0f, 0.0f, 0.0f);
	public static final Vector4 UNIT_X = new Vector4(1.0f, 0.0f, 0.0f, 0.0f);
	public static final Vector4 UNIT_Y = new Vector4(0.0f, 1.0f, 0.0f, 0.0f);
	public static final Vector4 UNIT_Z = new Vector4(0.0f, 0.0f, 1.0f, 0.0f);
	public static final Vector4 UNIT_W = new Vector4(0.0f, 0.0f, 0.0f, 1.0f);

	private final float x;
	private final float y;
	private final float z;
	private final float w;

	/**
	 * Default constructor for {@link Vector4}.
	 * <p>
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component
	 * @param w The w-component
	 */
	public Vector4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * @return The x-component
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return The y-component
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return The z-component
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @return The w-component
	 */
	public float getW() {
		return w;
	}

	/**
	 * @return The floored x-component
	 */
	public int getFloorX() {
		return FastMaths.floor(x);
	}

	/**
	 * @return The floored y-component
	 */
	public int getFloorY() {
		return FastMaths.floor(y);
	}

	/**
	 * @return The floored z-component
	 */
	public int getFloorZ() {
		return FastMaths.floor(z);
	}

	/**
	 * @return The floored w-component
	 */
	public int getFloorW() {
		return FastMaths.floor(w);
	}

	/**
	 * The length of the {@link Vector4}.
	 * <p>
	 * @return The length
	 */
	public float length() {
		final float lenSq = x * x + y * y + z * z + w * w;
		return FastMaths.fastSqrt(lenSq);
	}

	/**
	 * The length squared of the {@link Vector4}.
	 * <p>
	 * @return The length squared
	 */
	public float lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}

	/**
	 * Calculates the distance between the vectors.
	 * <p>
	 * @param vec The other vector
	 * <p>
	 * @return The distance between this vector and another
	 */
	public float distance(Vector4 vec) {
		final float diffX = x - vec.x;
		final float diffY = y - vec.y;
		final float diffZ = z - vec.z;
		final float diffW = w - vec.w;
		final float distSquared = (diffX * diffX) + (diffY * diffY) + (diffZ * diffZ) + (diffW * diffW);
		return FastMaths.fastSqrt(distSquared);
	}

	/**
	 * Calculates the distance squared between the vectors.
	 * <p>
	 * @param vec The other vector
	 * <p>
	 * @return The distance squared between this vector and another
	 */
	public float distanceSquared(Vector4 vec) {
		final float diffX = x - vec.x;
		final float diffY = y - vec.y;
		final float diffZ = z - vec.z;
		final float diffW = w - vec.w;
		return (diffX * diffX) + (diffY * diffY) + (diffZ * diffZ) + (diffW * diffW);
	}

	/**
	 * Calculates the dot product of the vectors.
	 * <p>
	 * @param vec The vector to dot with
	 * <p>
	 * @return The dot product of the two vectors
	 */
	public float dot(Vector4 vec) {
		return (x * vec.x) + (y * vec.y) + (z * vec.z) + (w * vec.w);
	}

	/**
	 * Creates a new normalised {@link Vector3}.
	 * <p>
	 * @return A new normalised {@link Vector3}
	 */
	public Vector4 normalise() {
		final float lenSq = x * x + y * y + z * z + w * w;
		if (lenSq == 1.0f || lenSq == 0.0f) {
			return this;
		}
		final float invLenSq = 1.0f / FastMaths.fastSqrt(lenSq);
		return new Vector4(x * invLenSq, y * invLenSq, z * invLenSq, w * invLenSq);
	}

	/**
	 * Creates the negative {@link Vector3} of this vector.
	 * <p>
	 * @return A new negated {@link Vector3}
	 */
	public Vector4 negate() {
		return new Vector4(-x, -y, -z, -w);
	}

	public Vector4 add(float v) {
		return new Vector4(x + v, y + v, z + v, w + v);
	}

	public Vector4 add(float vx, float vy, float vz, float vw) {
		return new Vector4(x + vx, y + vy, z + vz, w + vw);
	}

	public Vector4 add(Vector4 vec) {
		return new Vector4(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
	}

	public Vector4 sub(float v) {
		return new Vector4(x - v, y - v, z - v, w - v);
	}

	public Vector4 sub(float vx, float vy, float vz, float vw) {
		return new Vector4(x - vx, y - vy, z - vz, w - vw);
	}

	public Vector4 sub(Vector4 vec) {
		return new Vector4(x - vec.x, y - vec.y, z - vec.z, w - vec.w);
	}

	public Vector4 mul(float v) {
		return new Vector4(x * v, y * v, z * v, w * v);
	}

	public Vector4 mul(float vx, float vy, float vz, float vw) {
		return new Vector4(x * vx, y * vy, z * vz, w * vw);
	}

	public Vector4 mul(Vector4 vec) {
		return new Vector4(x * vec.x, y * vec.y, z * vec.z, w * vec.w);
	}

	public Vector4 div(float v) {
		return new Vector4(x / v, y / v, z / v, w / v);
	}

	public Vector4 div(float vx, float vy, float vz, float vw) {
		return new Vector4(x / vx, y / vy, z / vz, w / vw);
	}

	public Vector4 div(Vector4 vec) {
		return new Vector4(x / vec.x, y / vec.y, z / vec.z, w / vec.w);
	}
}
