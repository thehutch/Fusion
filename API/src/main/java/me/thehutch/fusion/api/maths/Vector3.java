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
public class Vector3 {
	public static final Vector3 ONE = new Vector3(1.0f, 1.0f, 1.0f);
	public static final Vector3 ZERO = new Vector3(0.0f, 0.0f, 0.0f);
	public static final Vector3 UNIT_X = new Vector3(1.0f, 0.0f, 0.0f);
	public static final Vector3 UNIT_Y = new Vector3(0.0f, 1.0f, 0.0f);
	public static final Vector3 UNIT_Z = new Vector3(0.0f, 0.0f, 1.0f);
	private final float x;
	private final float y;
	private final float z;

	public Vector3(float x, float y, float z) {
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
		return x * x + y * y + z * z;
	}

	public float distance(Vector3 vec) {
		return (float) Math.sqrt(distanceSquared(vec));
	}

	public float distanceSquared(Vector3 vec) {
		return ((x - vec.x) * (x - vec.x)) + ((y - vec.y) * (y - vec.y)) + ((z - vec.z) * (z - vec.z));
	}

	public float dot(Vector3 vec) {
		return (x * vec.x) + (y * vec.y) + (z * vec.z);
	}

	public Vector3 cross(Vector3 vec) {
		return new Vector3(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
	}

	public Vector3 rotate(Quaternion rotation) {
		final Quaternion q = rotation.mul(this).mul(rotation.conjugate());
		return new Vector3(q.getX(), q.getY(), q.getZ());
	}

	public Vector3 normalise() {
		final float length = length();
		return new Vector3(x / length, y / length, z / length);
	}

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

	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}
}
