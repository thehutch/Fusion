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
package me.thehutch.fusion.api.maths.imaginary;

import me.thehutch.fusion.api.maths.MathsHelper;
import me.thehutch.fusion.api.maths.matrix.Matrix4;
import me.thehutch.fusion.api.maths.vector.Vector3f;

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

	public Quaternion mul(float v) {
		return new Quaternion(x * v, y * v, z * v, w * v);
	}

	public Quaternion mul(Quaternion q) {
		return mul(q.x, q.y, q.z, q.w);
	}

	public Quaternion mul(float x, float y, float z, float w) {
		return new Quaternion(this.w * x + this.x * w + this.y * z - this.z * y,
							  this.w * y + this.y * w + this.z * x - this.x * z,
							  this.w * z + this.z * w + this.x * y - this.y * x,
							  this.w * w - this.x * x - this.y * y - this.z * z);
	}

	public Quaternion mul(Vector3f vec) {
		final float x = getW() * vec.getX() + getY() * vec.getZ() - getZ() * vec.getY();
		final float y = getW() * vec.getY() + getZ() * vec.getX() - getX() * vec.getZ();
		final float z = getW() * vec.getZ() + getX() * vec.getY() - getY() * vec.getX();
		final float w = -getX() * vec.getX() - getY() * vec.getY() - getZ() * vec.getZ();
		return new Quaternion(x, y, z, w);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public Quaternion normalise() {
		final float length = length();
		return new Quaternion(x / length, y / length, z / length, w / length);
	}

	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}

	public Matrix4 toRotationMatrix() {
		final Vector3f forward = new Vector3f(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
		final Vector3f up = new Vector3f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
		return Matrix4.createRotation(forward, up);
	}

	public static Quaternion fromAxisAngleDeg(Vector3f axis, float angle) {
		return Quaternion.fromAxisAngleRad(axis.getX(), axis.getY(), axis.getZ(), MathsHelper.toRadians(angle));
	}

	public static Quaternion fromAxisAngleRad(Vector3f axis, float angle) {
		return Quaternion.fromAxisAngleRad(axis.getX(), axis.getY(), axis.getZ(), angle);
	}

	public static Quaternion fromAxisAngleDeg(float x, float y, float z, float angle) {
		return Quaternion.fromAxisAngleRad(x, y, z, MathsHelper.toRadians(angle));
	}

	public static Quaternion fromAxisAngleRad(float x, float y, float z, float angle) {
		final float sinHalfAngle = (float) Math.sin(angle / 2.0f);
		final float cosHalfAngle = (float) Math.cos(angle / 2.0f);
		return new Quaternion(x * sinHalfAngle, y * sinHalfAngle, z * sinHalfAngle, cosHalfAngle);
	}
}
