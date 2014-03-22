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

/**
 * @author thehutch
 */
public class Vector3i {
	public static final Vector3i ONE = new Vector3i(1, 1, 1);
	public static final Vector3i ZERO = new Vector3i(0, 0, 0);
	public static final Vector3i X_AXIS = new Vector3i(1, 0, 0);
	public static final Vector3i Y_AXIS = new Vector3i(0, 1, 0);
	public static final Vector3i Z_AXIS = new Vector3i(0, 0, 1);
	private final int x;
	private final int y;
	private final int z;

	public Vector3i(Vector3i vec) {
		this(vec.x, vec.y, vec.z);
	}

	public Vector3i(float x, float y, float z) {
		this((int) x, (int) y, (int) z);
	}

	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public float lengthSquared() {
		return dot(this);
	}

	public float distance(Vector3i vec) {
		return (float) Math.sqrt(distanceSquared(vec));
	}

	public float distanceSquared(Vector3i vec) {
		return ((vec.x - x) * (vec.x - x)) + ((vec.y - y) * (vec.y - y)) + ((vec.z - z) * (vec.z - z));
	}

	public float dot(Vector3i vec) {
		return (x * vec.x) + (y * vec.y) + (z * vec.z);
	}

	public Vector3i cross(Vector3i vec) {
		return new Vector3i(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
	}

	public Vector3i negate() {
		return new Vector3i(-x, -y, -z);
	}

	public Vector3i normalise() {
		final float length = length();
		return new Vector3i(x / length, y / length, z / length);
	}

	public Vector3i add(int v) {
		return new Vector3i(x + v, y + v, z + v);
	}

	public Vector3i add(int vx, int vy, int vz) {
		return new Vector3i(x + vx, y + vy, z + vz);
	}

	public Vector3i add(Vector3i vec) {
		return new Vector3i(x + vec.x, y + vec.y, z + vec.z);
	}

	public Vector3i sub(int v) {
		return new Vector3i(x - v, y - v, z - v);
	}

	public Vector3i sub(int vx, int vy, int vz) {
		return new Vector3i(x - vx, y - vy, z - vz);
	}

	public Vector3i sub(Vector3i vec) {
		return new Vector3i(x - vec.x, y - vec.y, z - vec.z);
	}

	public Vector3i mul(int v) {
		return new Vector3i(x * v, y * v, z * v);
	}

	public Vector3i mul(int vx, int vy, int vz) {
		return new Vector3i(x * vx, y * vy, z * vz);
	}

	public Vector3i mul(Vector3i vec) {
		return new Vector3i(x * vec.x, y * vec.y, z * vec.z);
	}

	public Vector3i div(int v) {
		return new Vector3i(x / v, y / v, z / v);
	}

	public Vector3i div(int vx, int vy, int vz) {
		return new Vector3i(x / vx, y / vy, z / vz);
	}

	public Vector3i div(Vector3i vec) {
		return new Vector3i(x / vec.x, y / vec.y, z / vec.z);
	}

	public Vector2f toVector2() {
		return new Vector2f(x, y);
	}

	public Vector4f toVector4(int w) {
		return new Vector4f(x, y, z, w);
	}

	@Override
	public String toString() {
		return '(' + x + ", " + y + ", " + z + ')';
	}
}
