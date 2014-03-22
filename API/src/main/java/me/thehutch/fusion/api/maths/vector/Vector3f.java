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
public class Vector3f {
	public static final Vector3f ONE = new Vector3f(1f, 1f, 1f);
	public static final Vector3f ZERO = new Vector3f(0f, 0f, 0f);
	public static final Vector3f X_AXIS = new Vector3f(1f, 0f, 0f);
	public static final Vector3f Y_AXIS = new Vector3f(0f, 1f, 0f);
	public static final Vector3f Z_AXIS = new Vector3f(0f, 0f, 1f);
	private final float x;
	private final float y;
	private final float z;

	public Vector3f(Vector3f vec) {
		this(vec.x, vec.y, vec.z);
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public int getFloorX() {
		return MathsHelper.floor(x);
	}

	public int getFloorY() {
		return MathsHelper.floor(y);
	}

	public int getFloorZ() {
		return MathsHelper.floor(z);
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public float lengthSquared() {
		return dot(this);
	}

	public float distance(Vector3f vec) {
		return (float) Math.sqrt(distanceSquared(vec));
	}

	public float distanceSquared(Vector3f vec) {
		return ((vec.x - x) * (vec.x - x)) + ((vec.y - y) * (vec.y - y)) + ((vec.z - z) * (vec.z - z));
	}

	public float dot(Vector3f vec) {
		return (x * vec.x) + (y * vec.y) + (z * vec.z);
	}

	public Vector3f cross(Vector3f vec) {
		return new Vector3f(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
	}

	public Vector3f negate() {
		return new Vector3f(-x, -y, -z);
	}

	public Vector3f normalise() {
		final float length = length();
		return new Vector3f(x / length, y / length, z / length);
	}

	public Vector3f add(float v) {
		return new Vector3f(x + v, y + v, z + v);
	}

	public Vector3f add(float vx, float vy, float vz) {
		return new Vector3f(x + vx, y + vy, z + vz);
	}

	public Vector3f add(Vector3f vec) {
		return new Vector3f(x + vec.x, y + vec.y, z + vec.z);
	}

	public Vector3f sub(float v) {
		return new Vector3f(x - v, y - v, z - v);
	}

	public Vector3f sub(float vx, float vy, float vz) {
		return new Vector3f(x - vx, y - vy, z - vz);
	}

	public Vector3f sub(Vector3f vec) {
		return new Vector3f(x - vec.x, y - vec.y, z - vec.z);
	}

	public Vector3f mul(float v) {
		return new Vector3f(x * v, y * v, z * v);
	}

	public Vector3f mul(float vx, float vy, float vz) {
		return new Vector3f(x * vx, y * vy, z * vz);
	}

	public Vector3f mul(Vector3f vec) {
		return new Vector3f(x * vec.x, y * vec.y, z * vec.z);
	}

	public Vector3f div(float v) {
		return new Vector3f(x / v, y / v, z / v);
	}

	public Vector3f div(float vx, float vy, float vz) {
		return new Vector3f(x / vx, y / vy, z / vz);
	}

	public Vector3f div(Vector3f vec) {
		return new Vector3f(x / vec.x, y / vec.y, z / vec.z);
	}

	public Vector2f toVector2() {
		return new Vector2f(x, y);
	}

	public Vector4f toVector4(float w) {
		return new Vector4f(x, y, z, w);
	}

	@Override
	public String toString() {
		return '(' + x + ", " + y + ", " + z + ')';
	}
}
