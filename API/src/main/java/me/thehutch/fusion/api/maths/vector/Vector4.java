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
public class Vector4 {
	public static final Vector4 ONE = new Vector4(1.0f, 1.0f, 1.0f, 1.0f);
	public static final Vector4 ZERO = new Vector4(0.0f, 0.0f, 0.0f, 0.0f);
	private final float x;
	private final float y;
	private final float z;
	private final float w;

	public Vector4(Vector4 vec) {
		this(vec.x, vec.y, vec.z, vec.w);
	}

	public Vector4(float x, float y, float z, float w) {
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

	public float distance(Vector4 vec) {
		return (float) Math.sqrt(distanceSquared(vec));
	}

	public float distanceSquared(Vector4 vec) {
		return ((vec.x - x) * (vec.x - x)) + ((vec.y - y) * (vec.y - y)) + ((vec.z - z) * (vec.z - z)) + ((vec.w - w) * (vec.w - w));
	}

	public float dot(Vector4 vec) {
		return (x * vec.x) + (y * vec.y) + (z * vec.z) + (w * vec.w);
	}

	public Vector4 negate() {
		return new Vector4(-x, -y, -z, -w);
	}

	public Vector4 normalise() {
		final float length = length();
		return new Vector4(x / length, y / length, z / length, w / length);
	}

	public Vector4 add(float v) {
		return new Vector4(x + v, y + v, z + v, w + v);
	}

	public Vector4 add(float vx, float vy, float vz, float vw) {
		return new Vector4(x + vx, y + vy, z + vz, w + vw);
	}

	public Vector4 add(Vector4 vec) {
		return new Vector4(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
	}

	public Vector4 sub(float v) {
		return new Vector4(x - v, y - v, z - v, w - v);
	}

	public Vector4 sub(float vx, float vy, float vz, float vw) {
		return new Vector4(x - vx, y - vy, z - vz, w - vw);
	}

	public Vector4 sub(Vector4 vec) {
		return new Vector4(x - vec.x, y - vec.y, z - vec.z, w - vec.w);
	}

	public Vector4 mul(float v) {
		return new Vector4(x + v, y + v, z + v, w + v);
	}

	public Vector4 mul(float vx, float vy, float vz, float vw) {
		return new Vector4(x + vx, y + vy, z + vz, w + vw);
	}

	public Vector4 mul(Vector4 vec) {
		return new Vector4(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
	}

	public Vector4 div(float v) {
		return new Vector4(x / v, y / v, z / v, w / v);
	}

	public Vector4 div(float vx, float vy, float vz, float vw) {
		return new Vector4(x / vx, y / vy, z / vz, w / vw);
	}

	public Vector4 div(Vector4 vec) {
		return new Vector4(x / vec.x, y / vec.y, z / vec.z, w / vec.w);
	}

	public Vector2 toVector2() {
		return new Vector2(x, y);
	}

	public Vector3 toVector3() {
		return new Vector3(x, y, z);
	}

	@Override
	public String toString() {
		return '(' + x + ", " + y + ", " + z + ", " + w + ')';
	}
}
