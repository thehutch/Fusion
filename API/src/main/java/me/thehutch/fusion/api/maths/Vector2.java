/*
 * This file is part of API, licensed under the Apache 2.0 License.
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
public class Vector2 {
	public static final Vector2 ONE = new Vector2(1.0f, 1.0f);
	public static final Vector2 ZERO = new Vector2(0.0f, 0.0f);
	public static final Vector2 UNIT_X = new Vector2(1.0f, 0.0f);
	public static final Vector2 UNIT_Y = new Vector2(0.0f, 1.0f);
	private final float x;
	private final float y;

	public Vector2(float x, float y) {
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
		return x * x + y * y;
	}

	public float distance(Vector2 vec) {
		return (float) Math.sqrt(distanceSquared(vec));
	}

	public float distanceSquared(Vector2 vec) {
		return ((x - vec.x) * (x - vec.x)) + ((y - vec.y) * (y - vec.y));
	}

	public float dot(Vector2 vec) {
		return (x * vec.x) + (y * vec.y);
	}

	public Vector2 normalise() {
		final float length = length();
		return new Vector2(x / length, y / length);
	}

	public Vector2 negate() {
		return new Vector2(-x, -y);
	}

	public Vector2 add(float v) {
		return new Vector2(x + v, y + v);
	}

	public Vector2 add(float vx, float vy) {
		return new Vector2(x + vx, y + vy);
	}

	public Vector2 add(Vector2 vec) {
		return new Vector2(x + vec.x, y + vec.y);
	}

	public Vector2 sub(float v) {
		return new Vector2(x - v, y - v);
	}

	public Vector2 sub(float vx, float vy) {
		return new Vector2(x - vx, y - vy);
	}

	public Vector2 sub(Vector2 vec) {
		return new Vector2(x - vec.x, y - vec.y);
	}

	public Vector2 mul(float v) {
		return new Vector2(x * v, y * v);
	}

	public Vector2 mul(float vx, float vy) {
		return new Vector2(x * vx, y * vy);
	}

	public Vector2 mul(Vector2 vec) {
		return new Vector2(x * vec.x, y * vec.y);
	}

	public Vector2 div(float v) {
		return new Vector2(x / v, y / v);
	}

	public Vector2 div(float vx, float vy) {
		return new Vector2(x / vx, y / vy);
	}

	public Vector2 div(Vector2 vec) {
		return new Vector2(x / vec.x, y / vec.y);
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
}
