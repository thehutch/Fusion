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
package me.thehutch.fusion.api.maths.vector;

import me.thehutch.fusion.api.maths.MathsHelper;

/**
 * @author thehutch
 */
public class Vector2f {
	public static final Vector2f ONE = new Vector2f(1.0f, 1.0f);
	public static final Vector2f ZERO = new Vector2f(0.0f, 0.0f);
	private final float x;
	private final float y;

	public Vector2f(Vector2f vec) {
		this(vec.x, vec.y);
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getFloorX() {
		return MathsHelper.floor(x);
	}

	public int getFloorY() {
		return MathsHelper.floor(y);
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public float lengthSquared() {
		return dot(this);
	}

	public float distance(Vector2f vec) {
		return (float) Math.sqrt(distanceSquared(vec));
	}

	public float distanceSquared(Vector2f vec) {
		return ((vec.x - x) * (vec.x - x)) + ((vec.y - y) * (vec.y - y));
	}

	public float dot(Vector2f vec) {
		return (x * vec.x) + (y * vec.y);
	}

	public Vector2f negate() {
		return new Vector2f(-x, -y);
	}

	public Vector2f normalise() {
		final float length = length();
		return new Vector2f(x / length, y / length);
	}

	public Vector2f add(float v) {
		return new Vector2f(x + v, y + v);
	}

	public Vector2f add(float vx, float vy) {
		return new Vector2f(x + vx, y + vy);
	}

	public Vector2f add(Vector2f vec) {
		return new Vector2f(x + vec.x, y + vec.y);
	}

	public Vector2f sub(float v) {
		return new Vector2f(x - v, y - v);
	}

	public Vector2f sub(float vx, float vy) {
		return new Vector2f(x - vx, y - vy);
	}

	public Vector2f sub(Vector2f vec) {
		return new Vector2f(x - vec.x, y - vec.y);
	}

	public Vector2f mul(float v) {
		return new Vector2f(x * v, y * v);
	}

	public Vector2f mul(float vx, float vy) {
		return new Vector2f(x * vx, y * vy);
	}

	public Vector2f mul(Vector2f vec) {
		return new Vector2f(x * vec.x, y * vec.y);
	}

	public Vector2f div(float v) {
		return new Vector2f(x / v, y / v);
	}

	public Vector2f div(float vx, float vy) {
		return new Vector2f(x / vx, y / vy);
	}

	public Vector2f div(Vector2f vec) {
		return new Vector2f(x / vec.x, y / vec.y);
	}

	public Vector3f toVector3(float z) {
		return new Vector3f(x, y, z);
	}

	public Vector4f toVector4(float z, float w) {
		return new Vector4f(x, y, z, w);
	}

	@Override
	public String toString() {
		return '(' + x + ", " + y + ')';
	}
}
