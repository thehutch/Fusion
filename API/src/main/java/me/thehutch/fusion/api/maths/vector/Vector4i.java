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
public class Vector4i {
	public static final Vector4i ONE = new Vector4i(1, 1, 1, 1);
	public static final Vector4i ZERO = new Vector4i(0, 0, 0, 0);
	private final int x;
	private final int y;
	private final int z;
	private final int w;

	public Vector4i(Vector4i vec) {
		this(vec.x, vec.y, vec.z, vec.w);
	}

	public Vector4i(float x, float y, float z, float w) {
		this((int) x, (int) y, (int) z, (int) w);
	}

	public Vector4i(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
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

	public int getW() {
		return w;
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public float lengthSquared() {
		return dot(this);
	}

	public float distance(Vector4i vec) {
		return (float) Math.sqrt(distanceSquared(vec));
	}

	public float distanceSquared(Vector4i vec) {
		return ((vec.x - x) * (vec.x - x)) + ((vec.y - y) * (vec.y - y)) + ((vec.z - z) * (vec.z - z)) + ((vec.w - w) * (vec.w - w));
	}

	public float dot(Vector4i vec) {
		return (x * vec.x) + (y * vec.y) + (z * vec.z) + (w * vec.w);
	}

	public Vector4i negate() {
		return new Vector4i(-x, -y, -z, -w);
	}

	public Vector4i normalise() {
		final float length = length();
		return new Vector4i(x / length, y / length, z / length, w / length);
	}

	public Vector4i add(int v) {
		return new Vector4i(x + v, y + v, z + v, w + v);
	}

	public Vector4i add(int vx, int vy, int vz, int vw) {
		return new Vector4i(x + vx, y + vy, z + vz, w + vw);
	}

	public Vector4i add(Vector4i vec) {
		return new Vector4i(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
	}

	public Vector4i sub(int v) {
		return new Vector4i(x - v, y - v, z - v, w - v);
	}

	public Vector4i sub(int vx, int vy, int vz, int vw) {
		return new Vector4i(x - vx, y - vy, z - vz, w - vw);
	}

	public Vector4i sub(Vector4i vec) {
		return new Vector4i(x - vec.x, y - vec.y, z - vec.z, w - vec.w);
	}

	public Vector4i mul(int v) {
		return new Vector4i(x + v, y + v, z + v, w + v);
	}

	public Vector4i mul(int vx, int vy, int vz, int vw) {
		return new Vector4i(x + vx, y + vy, z + vz, w + vw);
	}

	public Vector4i mul(Vector4i vec) {
		return new Vector4i(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
	}

	public Vector4i div(int v) {
		return new Vector4i(x / v, y / v, z / v, w / v);
	}

	public Vector4i div(int vx, int vy, int vz, int vw) {
		return new Vector4i(x / vx, y / vy, z / vz, w / vw);
	}

	public Vector4i div(Vector4i vec) {
		return new Vector4i(x / vec.x, y / vec.y, z / vec.z, w / vec.w);
	}

	public Vector2f toVector2() {
		return new Vector2f(x, y);
	}

	public Vector3f toVector3() {
		return new Vector3f(x, y, z);
	}

	@Override
	public String toString() {
		return '(' + x + ", " + y + ", " + z + ", " + w + ')';
	}
}
