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
public class Quaternion {
	public static final Quaternion IDENTITY = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
	private final float x;
	private final float y;
	private final float z;
	private final float w;

	public Quaternion(float x, float y, float z, float w) {
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

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public float lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}

	public float dot(Quaternion q) {
		return x * q.x + y * q.y + z * q.z + w * q.w;
	}

	public Quaternion normalise() {
		final float length = length();
		return new Quaternion(x / length, y / length, z / length, w / length);
	}

	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}

	public Quaternion invert() {
		final float lengthSquared = lengthSquared();
		return new Quaternion(-x / lengthSquared, -y / lengthSquared, -z / lengthSquared, w / lengthSquared);
	}

	public Quaternion add(Quaternion q) {
		return new Quaternion(x + q.x, y + q.y, z + q.z, w + q.w);
	}

	public Quaternion sub(Quaternion q) {
		return new Quaternion(x - q.x, y - q.y, z - q.z, w - q.w);
	}

	public Quaternion mul(float scalar) {
		return new Quaternion(x * scalar, y * scalar, z * scalar, w * scalar);
	}

	public Quaternion mul(Quaternion q) {
		return new Quaternion(w * q.x + x * q.w + y * q.z - z * q.y,
							  w * q.y + y * q.w + z * q.x - x * q.z,
							  w * q.z + z * q.w + x * q.y - y * q.x,
							  w * q.w - x * q.x - y * q.y - z * q.z);
	}

	public Quaternion div(float scalar) {
		return new Quaternion(x / scalar, y / scalar, z / scalar, w / scalar);
	}

	public static Quaternion fromAxisAngleDeg(Vector3 axis, float angle) {
		return fromAxisAngleRad(axis.getX(), axis.getY(), axis.getZ(), MathsHelper.toRadians(angle));
	}

	public static Quaternion fromAxisAngleDeg(float x, float y, float z, float angle) {
		return fromAxisAngleRad(x, y, z, MathsHelper.toRadians(angle));
	}

	public static Quaternion fromAxisAngleRad(Vector3 axis, float angle) {
		return fromAxisAngleRad(axis.getX(), axis.getY(), axis.getZ(), angle);
	}

	public static Quaternion fromAxisAngleRad(float x, float y, float z, float angle) {
		final float halfAngle = angle / 2.0f;
		final float q = (float) (Math.sin(halfAngle) / Math.sqrt(x * x + y * y + z * z));
		return new Quaternion(x * q, y * q, z * q, (float) Math.cos(halfAngle));
	}
}
