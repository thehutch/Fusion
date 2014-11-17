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

	private final float mX;
	private final float mY;
	private final float mZ;

	/**
	 * Default constructor for {@link Vector3}.
	 * <p>
	 * @param x The mX-component
	 * @param y The mY-component
	 * @param z The mZ-component
	 */
	public Vector3(float x, float y, float z) {
		mX = x;
		mY = y;
		mZ = z;
	}

	/**
	 * @return The mX-component
	 */
	public float getX() {
		return mX;
	}

	/**
	 * @return The mY-component
	 */
	public float getY() {
		return mY;
	}

	/**
	 * @return The mZ-component
	 */
	public float getZ() {
		return mZ;
	}

	/**
	 * @return The floored mX-component
	 */
	public int getFloorX() {
		return FastMaths.floor(mX);
	}

	/**
	 * @return The floored mY-component
	 */
	public int getFloorY() {
		return FastMaths.floor(mY);
	}

	/**
	 * @return The floored mZ-component
	 */
	public int getFloorZ() {
		return FastMaths.floor(mZ);
	}

	/**
	 * The length of the {@link Vector3}.
	 * <p>
	 * @return The length
	 */
	public float length() {
		final float lenSq = mX * mX + mY * mY + mZ * mZ;
		return FastMaths.fastSqrt(lenSq);
	}

	/**
	 * The length squared of the {@link Vector3}.
	 * <p>
	 * @return The length squared
	 */
	public float lengthSquared() {
		return mX * mX + mY * mY + mZ * mZ;
	}

	/**
	 * Calculates the distance between the vectors.
	 * <p>
	 * @param vec The other vector
	 * <p>
	 * @return The distance between this vector and another
	 */
	public float distance(Vector3 vec) {
		final float diffX = mX - vec.mX;
		final float diffY = mY - vec.mY;
		final float diffZ = mZ - vec.mZ;
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
		final float diffX = mX - vec.mX;
		final float diffY = mY - vec.mY;
		final float diffZ = mZ - vec.mZ;
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
		return (mX * vec.mX) + (mY * vec.mY) + (mZ * vec.mZ);
	}

	/**
	 * Calculates the cross product of the two vectors.
	 * <p>
	 * @param vec The vector to cross with
	 * <p>
	 * @return A new cross product {@link Vector3}
	 */
	public Vector3 cross(Vector3 vec) {
		final float vecX = vec.mX;
		final float vecY = vec.mY;
		final float vecZ = vec.mZ;
		final float newX = (mY * vecZ) - (vecY - mZ);
		final float newY = (mZ * vecX) - (vecZ - mX);
		final float newZ = (mX * vecY) - (vecX - mY);
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
		final float lenSq = mX * mX + mY * mY + mZ * mZ;
		if (lenSq == 1.0f || lenSq == 0.0f) {
			return this;
		}
		final float invLenSq = 1.0f / FastMaths.fastSqrt(lenSq);
		return new Vector3(mX * invLenSq, mY * invLenSq, mZ * invLenSq);
	}

	/**
	 * Creates the negative {@link Vector3} of this vector.
	 * <p>
	 * @return A new negated {@link Vector3}
	 */
	public Vector3 negate() {
		return new Vector3(-mX, -mY, -mZ);
	}

	public Vector3 add(float v) {
		return new Vector3(mX + v, mY + v, mZ + v);
	}

	public Vector3 add(float vx, float vy, float vz) {
		return new Vector3(mX + vx, mY + vy, mZ + vz);
	}

	public Vector3 add(Vector3 vec) {
		return new Vector3(mX + vec.mX, mY + vec.mY, mZ + vec.mZ);
	}

	public Vector3 sub(float v) {
		return new Vector3(mX - v, mY - v, mZ - v);
	}

	public Vector3 sub(float vx, float vy, float vz) {
		return new Vector3(mX - vx, mY - vy, mZ - vz);
	}

	public Vector3 sub(Vector3 vec) {
		return new Vector3(mX - vec.mX, mY - vec.mY, mZ - vec.mZ);
	}

	public Vector3 mul(float v) {
		return new Vector3(mX * v, mY * v, mZ * v);
	}

	public Vector3 mul(float vx, float vy, float vz) {
		return new Vector3(mX * vx, mY * vy, mZ * vz);
	}

	public Vector3 mul(Vector3 vec) {
		return new Vector3(mX * vec.mX, mY * vec.mY, mZ * vec.mZ);
	}

	public Vector3 div(float v) {
		return new Vector3(mX / v, mY / v, mZ / v);
	}

	public Vector3 div(float vx, float vy, float vz) {
		return new Vector3(mX / vx, mY / vy, mZ / vz);
	}

	public Vector3 div(Vector3 vec) {
		return new Vector3(mX / vec.mX, mY / vec.mY, mZ / vec.mZ);
	}
}
