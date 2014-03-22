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
public class Vector2i {
	public static final Vector2i ONE = new Vector2i(1, 1);
	public static final Vector2i ZERO = new Vector2i(0, 0);
	private final int x;
	private final int y;

	public Vector2i(Vector2i vec) {
		this(vec.x, vec.y);
	}

	public Vector2i(float x, float y) {
		this((int) x, (int) y);
	}

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public float lengthSquared() {
		return dot(this);
	}

	public float distance(Vector2i vec) {
		return (float) Math.sqrt(distanceSquared(vec));
	}

	public float distanceSquared(Vector2i vec) {
		return ((vec.x - x) * (vec.x - x)) + ((vec.y - y) * (vec.y - y));
	}

	public float dot(Vector2i vec) {
		return (x * vec.x) + (y * vec.y);
	}

	public Vector2i negate() {
		return new Vector2i(-x, -y);
	}

	public Vector2i normalise() {
		final float length = length();
		return new Vector2i(x / length, y / length);
	}

	public Vector2i add(int v) {
		return new Vector2i(x + v, y + v);
	}

	public Vector2i add(int vx, int vy) {
		return new Vector2i(x + vx, y + vy);
	}

	public Vector2i add(Vector2i vec) {
		return new Vector2i(x + vec.x, y + vec.y);
	}

	public Vector2i sub(int v) {
		return new Vector2i(x - v, y - v);
	}

	public Vector2i sub(int vx, int vy) {
		return new Vector2i(x - vx, y - vy);
	}

	public Vector2i sub(Vector2i vec) {
		return new Vector2i(x - vec.x, y - vec.y);
	}

	public Vector2i mul(int v) {
		return new Vector2i(x * v, y * v);
	}

	public Vector2i mul(int vx, int vy) {
		return new Vector2i(x * vx, y * vy);
	}

	public Vector2i mul(Vector2i vec) {
		return new Vector2i(x * vec.x, y * vec.y);
	}

	public Vector2i div(int v) {
		return new Vector2i(x / v, y / v);
	}

	public Vector2i div(int vx, int vy) {
		return new Vector2i(x / vx, y / vy);
	}

	public Vector2i div(Vector2i vec) {
		return new Vector2i(x / vec.x, y / vec.y);
	}

	public Vector3i toVector3(int z) {
		return new Vector3i(x, y, z);
	}

	public Vector4i toVector4(int z, int w) {
		return new Vector4i(x, y, z, w);
	}

	@Override
	public String toString() {
		return '(' + x + ", " + y + ')';
	}
}
