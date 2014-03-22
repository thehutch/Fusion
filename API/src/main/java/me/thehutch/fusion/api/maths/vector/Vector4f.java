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
public class Vector4f {
	public static final Vector4f ONE = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
	public static final Vector4f ZERO = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
	private final float x;
	private final float y;
	private final float z;
	private final float w;

	public Vector4f(Vector4f vec) {
		this(vec.x, vec.y, vec.z, vec.w);
	}

	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
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

	public float getW() {
		return w;
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

	public int getFloorW() {
		return MathsHelper.floor(w);
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public float lengthSquared() {
		return dot(this);
	}

	public float distance(Vector4f vec) {
		return (float) Math.sqrt(distanceSquared(vec));
	}

	public float distanceSquared(Vector4f vec) {
		return ((vec.x - x) * (vec.x - x)) + ((vec.y - y) * (vec.y - y)) + ((vec.z - z) * (vec.z - z)) + ((vec.w - w) * (vec.w - w));
	}

	public float dot(Vector4f vec) {
		return (x * vec.x) + (y * vec.y) + (z * vec.z) + (w * vec.w);
	}

	public Vector4f negate() {
		return new Vector4f(-x, -y, -z, -w);
	}

	public Vector4f normalise() {
		final float length = length();
		return new Vector4f(x / length, y / length, z / length, w / length);
	}

	public Vector4f add(float v) {
		return new Vector4f(x + v, y + v, z + v, w + v);
	}

	public Vector4f add(float vx, float vy, float vz, float vw) {
		return new Vector4f(x + vx, y + vy, z + vz, w + vw);
	}

	public Vector4f add(Vector4f vec) {
		return new Vector4f(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
	}

	public Vector4f sub(float v) {
		return new Vector4f(x - v, y - v, z - v, w - v);
	}

	public Vector4f sub(float vx, float vy, float vz, float vw) {
		return new Vector4f(x - vx, y - vy, z - vz, w - vw);
	}

	public Vector4f sub(Vector4f vec) {
		return new Vector4f(x - vec.x, y - vec.y, z - vec.z, w - vec.w);
	}

	public Vector4f mul(float v) {
		return new Vector4f(x + v, y + v, z + v, w + v);
	}

	public Vector4f mul(float vx, float vy, float vz, float vw) {
		return new Vector4f(x + vx, y + vy, z + vz, w + vw);
	}

	public Vector4f mul(Vector4f vec) {
		return new Vector4f(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
	}

	public Vector4f div(float v) {
		return new Vector4f(x / v, y / v, z / v, w / v);
	}

	public Vector4f div(float vx, float vy, float vz, float vw) {
		return new Vector4f(x / vx, y / vy, z / vz, w / vw);
	}

	public Vector4f div(Vector4f vec) {
		return new Vector4f(x / vec.x, y / vec.y, z / vec.z, w / vec.w);
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
