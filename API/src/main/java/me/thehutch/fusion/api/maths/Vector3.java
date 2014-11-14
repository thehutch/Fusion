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
public final class Vector3 {
	public static final Vector3 ONE = new Vector3(1.0f, 1.0f, 1.0f);
	public static final Vector3 ZERO = new Vector3(0.0f, 0.0f, 0.0f);
	public static final Vector3 UNIT_X = new Vector3(1.0f, 0.0f, 0.0f);
	public static final Vector3 UNIT_Y = new Vector3(0.0f, 1.0f, 0.0f);
	public static final Vector3 UNIT_Z = new Vector3(0.0f, 0.0f, 1.0f);

	private final float x;
	private final float y;
	private final float z;

	/**
	 * Default constructor for {@link Vector3}.
	 * <p>
	 * @param x The x-component
	 * @param y The y-component
	 * @param z The z-component
	 */
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
	 * The length of the {@link Vector3}.
	 * <p>
	 * @return The length
	 */
	public float length() {
		final float lenSq = x * x + y * y + z * z;
		return FastMaths.fastSqrt(lenSq);
	}

	/**
	 * The length squared of the {@link Vector3}.
	 * <p>
	 * @return The length squared
	 */
	public float lengthSquared() {
		return x * x + y * y + z * z;
	}

	/**
	 * Calculates the distance between the vectors.
	 * <p>
	 * @param vec The other vector
	 * <p>
	 * @return The distance between this vector and another
	 */
	public float distance(Vector3 vec) {
		final float diffX = x - vec.x;
		final float diffY = y - vec.y;
		final float diffZ = z - vec.z;
		final float distSquared = (diffX * diffX) + (diffY * diffY) + (diffZ * diffZ);
		return FastMaths.fastSqrt(distSquared);
	}

	/**
	 * Calculates the distance squared between the vectors.
	 * <p>
	 * @param vec The other vector
	 * <p>
	 * @return The distance squared between this vector and another
	 */
	public float distanceSquared(Vector3 vec) {
		final float diffX = x - vec.x;
		final float diffY = y - vec.y;
		final float diffZ = z - vec.z;
		return (diffX * diffX) + (diffY * diffY) + (diffZ * diffZ);
	}

	/**
	 * Calculates the dot product of the vectors.
	 * <p>
	 * @param vec The vector to dot with
	 * <p>
	 * @return The dot product of the two vectors
	 */
	public float dot(Vector3 vec) {
		return (x * vec.x) + (y * vec.y) + (z * vec.z);
	}

	/**
	 * Calculates the cross product of the two vectors.
	 * <p>
	 * @param vec The vector to cross with
	 * <p>
	 * @return A new cross product {@link Vector3}
	 */
	public Vector3 cross(Vector3 vec) {
		final float vecX = vec.x;
		final float vecY = vec.y;
		final float vecZ = vec.z;
		final float newX = (y * vecZ) - (vecY - z);
		final float newY = (z * vecX) - (vecZ - x);
		final float newZ = (x * vecY) - (vecX - y);
		return new Vector3(newX, newY, newZ);
	}

	/**
	 * Creates a new rotated {@link Vector3} using the provided rotation.
	 * <p>
	 * @param rotation The quaternion used to rotate this vector
	 * <p>
	 * @return A new rotated {@link Vector3}
	 */
	public Vector3 rotate(Quaternion rotation) {
		final Quaternion conjugate = rotation.conjugate();
		final Quaternion multiply = rotation.mul(this);
		final Quaternion q = multiply.mul(conjugate);
		return new Vector3(q.getX(), q.getY(), q.getZ());
	}

	/**
	 * Creates a new normalised {@link Vector3}.
	 * <p>
	 * @return A new normalised {@link Vector3}
	 */
	public Vector3 normalise() {
		final float lenSq = x * x + y * y + z * z;
		if (lenSq == 1.0f || lenSq == 0.0f) {
			return this;
		}
		final float invLenSq = 1.0f / FastMaths.fastSqrt(lenSq);
		return new Vector3(x * invLenSq, y * invLenSq, z * invLenSq);
	}

	/**
	 * Creates the negative {@link Vector3} of this vector.
	 * <p>
	 * @return A new negated {@link Vector3}
	 */
	public Vector3 negate() {
		return new Vector3(-x, -y, -z);
	}

	public Vector3 add(float v) {
		return new Vector3(x + v, y + v, z + v);
	}

	public Vector3 add(float vx, float vy, float vz) {
		return new Vector3(x + vx, y + vy, z + vz);
	}

	public Vector3 add(Vector3 vec) {
		return new Vector3(x + vec.x, y + vec.y, z + vec.z);
	}

	public Vector3 sub(float v) {
		return new Vector3(x - v, y - v, z - v);
	}

	public Vector3 sub(float vx, float vy, float vz) {
		return new Vector3(x - vx, y - vy, z - vz);
	}

	public Vector3 sub(Vector3 vec) {
		return new Vector3(x - vec.x, y - vec.y, z - vec.z);
	}

	public Vector3 mul(float v) {
		return new Vector3(x * v, y * v, z * v);
	}

	public Vector3 mul(float vx, float vy, float vz) {
		return new Vector3(x * vx, y * vy, z * vz);
	}

	public Vector3 mul(Vector3 vec) {
		return new Vector3(x * vec.x, y * vec.y, z * vec.z);
	}

	public Vector3 div(float v) {
		return new Vector3(x / v, y / v, z / v);
	}

	public Vector3 div(float vx, float vy, float vz) {
		return new Vector3(x / vx, y / vy, z / vz);
	}

	public Vector3 div(Vector3 vec) {
		return new Vector3(x / vec.x, y / vec.y, z / vec.z);
	}
}
