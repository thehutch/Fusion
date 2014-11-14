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
public final class Vector2 {
	public static final Vector2 ONE = new Vector2(1.0f, 1.0f);
	public static final Vector2 ZERO = new Vector2(0.0f, 0.0f);
	public static final Vector2 UNIT_X = new Vector2(1.0f, 0.0f);
	public static final Vector2 UNIT_Y = new Vector2(0.0f, 1.0f);

	private final float x;
	private final float y;

	/**
	 * Default constructor for {@link Vector2}.
	 * <p>
	 * @param x The x-component
	 * @param y The y-component
	 */
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
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
	 * The length of the {@link Vector2}.
	 * <p>
	 * @return The length
	 */
	public float length() {
		final float lenSq = x * x + y * y;
		return FastMaths.fastSqrt(lenSq);
	}

	/**
	 * The length squared of the {@link Vector2}.
	 * <p>
	 * @return The length squared
	 */
	public float lengthSquared() {
		return x * x + y * y;
	}

	/**
	 * Calculates the distance between the vectors.
	 * <p>
	 * @param vec The other vector
	 * <p>
	 * @return The distance between this vector and another
	 */
	public float distance(Vector2 vec) {
		final float diffX = x - vec.x;
		final float diffY = y - vec.y;
		return FastMaths.fastSqrt((diffX * diffX) + (diffY * diffY));
	}

	/**
	 * Calculates the distance squared between the vectors.
	 * <p>
	 * @param vec The other vector
	 * <p>
	 * @return The distance squared between this vector and another
	 */
	public float distanceSquared(Vector2 vec) {
		final float diffX = x - vec.x;
		final float diffY = y - vec.y;
		return (diffX * diffX) + (diffY * diffY);
	}

	/**
	 * Calculates the dot product of the vectors.
	 * <p>
	 * @param vec The vector to dot with
	 * <p>
	 * @return The dot product of the two vectors
	 */
	public float dot(Vector2 vec) {
		return (x * vec.x) + (y * vec.y);
	}

	/**
	 * Creates a new normalised {@link Vector2}.
	 * <p>
	 * @return A new normalised {@link Vector2}
	 */
	public Vector2 normalise() {
		final float lenSq = x * x + y * y;
		if (lenSq == 1.0f || lenSq == 0.0f) {
			return this;
		}
		final float invLenSq = 1.0f / FastMaths.fastSqrt(lenSq);
		return new Vector2(x * invLenSq, y * invLenSq);
	}

	/**
	 * Creates the negative {@link Vector2} of this vector.
	 * <p>
	 * @return A new negated {@link Vector2}
	 */
	public Vector2 negate() {
		return new Vector2(-x, -y);
	}

	public Vector2 add(float v) {
		return new Vector2(x + v, y + v);
	}

	public Vector2 add(float vx, float vy) {
		return new Vector2(x + vx, y + vy);
	}

	public Vector2 add(Vector2 vec) {
		return new Vector2(x + vec.x, y + vec.y);
	}

	public Vector2 sub(float v) {
		return new Vector2(x - v, y - v);
	}

	public Vector2 sub(float vx, float vy) {
		return new Vector2(x - vx, y - vy);
	}

	public Vector2 sub(Vector2 vec) {
		return new Vector2(x - vec.x, y - vec.y);
	}

	public Vector2 mul(float v) {
		return new Vector2(x * v, y * v);
	}

	public Vector2 mul(float vx, float vy) {
		return new Vector2(x * vx, y * vy);
	}

	public Vector2 mul(Vector2 vec) {
		return new Vector2(x * vec.x, y * vec.y);
	}

	public Vector2 div(float v) {
		return new Vector2(x / v, y / v);
	}

	public Vector2 div(float vx, float vy) {
		return new Vector2(x / vx, y / vy);
	}

	public Vector2 div(Vector2 vec) {
		return new Vector2(x / vec.x, y / vec.y);
	}
}
