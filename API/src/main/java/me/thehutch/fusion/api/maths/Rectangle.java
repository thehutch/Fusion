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
public class Rectangle {
	private final int x;
	private final int y;
	private final int width;
	private final int height;

	public Rectangle(int width, int height) {
		this(0, 0, width, height);
	}

	public Rectangle(Vector2 position, Vector2 size) {
		this(position.getFloorX(), position.getFloorY(), size.getFloorX(), size.getFloorY());
	}

	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Vector2 getPosition() {
		return new Vector2(x, y);
	}

	public Vector2 getSize() {
		return new Vector2(width, height);
	}

	public int getArea() {
		return width * height;
	}

	public float getAspectRatio() {
		return (float) width / height;
	}
}
