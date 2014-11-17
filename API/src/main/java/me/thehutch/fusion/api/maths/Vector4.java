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

	private final float mX;
	private final float mY;
	private final float mZ;
	private final float mW;

	/**
	 * Default constructor for {@link Vector4}.
	 * <p>
	 * @param x The mX-component
	 * @param y The mY-component
	 * @param z The mZ-component
	 * @param w The mW-component
	 */
	public Vector4(float x, float y, float z, float w) {
		mX = x;
		mY = y;
		mZ = z;
		mW = w;
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
	 * @return The mW-component
	 */
	public float getW() {
		return mW;
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
	 * @return The floored mW-component
	 */
	public int getFloorW() {
		return FastMaths.floor(mW);
	}

	/**
	 * The length of the {@link Vector4}.
	 * <p>
	 * @return The length
	 */
	public float length() {
		final float lenSq = mX * mX + mY * mY + mZ * mZ + mW * mW;
		return FastMaths.fastSqrt(lenSq);
	}

	/**
	 * The length squared of the {@link Vector4}.
	 * <p>
	 * @return The length squared
	 */
	public float lengthSquared() {
		return mX * mX + mY * mY + mZ * mZ + mW * mW;
	}

	/**
	 * Calculates the distance between the vectors.
	 * <p>
	 * @param vec The other vector
	 * <p>
	 * @return The distance between this vector and another
	 */
	public float distance(Vector4 vec) {
		final float diffX = mX - vec.mX;
		final float diffY = mY - vec.mY;
		final float diffZ = mZ - vec.mZ;
		final float diffW = mW - vec.mW;
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
		final float diffX = mX - vec.mX;
		final float diffY = mY - vec.mY;
		final float diffZ = mZ - vec.mZ;
		final float diffW = mW - vec.mW;
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
		return (mX * vec.mX) + (mY * vec.mY) + (mZ * vec.mZ) + (mW * vec.mW);
	}

	/**
	 * Creates a new normalised {@link Vector3}.
	 * <p>
	 * @return A new normalised {@link Vector3}
	 */
	public Vector4 normalise() {
		final float lenSq = mX * mX + mY * mY + mZ * mZ + mW * mW;
		if (lenSq == 1.0f || lenSq == 0.0f) {
			return this;
		}
		final float invLenSq = 1.0f / FastMaths.fastSqrt(lenSq);
		return new Vector4(mX * invLenSq, mY * invLenSq, mZ * invLenSq, mW * invLenSq);
	}

	/**
	 * Creates the negative {@link Vector3} of this vector.
	 * <p>
	 * @return A new negated {@link Vector3}
	 */
	public Vector4 negate() {
		return new Vector4(-mX, -mY, -mZ, -mW);
	}

	public Vector4 add(float v) {
		return new Vector4(mX + v, mY + v, mZ + v, mW + v);
	}

	public Vector4 add(float vx, float vy, float vz, float vw) {
		return new Vector4(mX + vx, mY + vy, mZ + vz, mW + vw);
	}

	public Vector4 add(Vector4 vec) {
		return new Vector4(mX + vec.mX, mY + vec.mY, mZ + vec.mZ, mW + vec.mW);
	}

	public Vector4 sub(float v) {
		return new Vector4(mX - v, mY - v, mZ - v, mW - v);
	}

	public Vector4 sub(float vx, float vy, float vz, float vw) {
		return new Vector4(mX - vx, mY - vy, mZ - vz, mW - vw);
	}

	public Vector4 sub(Vector4 vec) {
		return new Vector4(mX - vec.mX, mY - vec.mY, mZ - vec.mZ, mW - vec.mW);
	}

	public Vector4 mul(float v) {
		return new Vector4(mX * v, mY * v, mZ * v, mW * v);
	}

	public Vector4 mul(float vx, float vy, float vz, float vw) {
		return new Vector4(mX * vx, mY * vy, mZ * vz, mW * vw);
	}

	public Vector4 mul(Vector4 vec) {
		return new Vector4(mX * vec.mX, mY * vec.mY, mZ * vec.mZ, mW * vec.mW);
	}

	public Vector4 div(float v) {
		return new Vector4(mX / v, mY / v, mZ / v, mW / v);
	}

	public Vector4 div(float vx, float vy, float vz, float vw) {
		return new Vector4(mX / vx, mY / vy, mZ / vz, mW / vw);
	}

	public Vector4 div(Vector4 vec) {
		return new Vector4(mX / vec.mX, mY / vec.mY, mZ / vec.mZ, mW / vec.mW);
	}
}
