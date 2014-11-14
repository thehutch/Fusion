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
package me.thehutch.fusion.api.maths.geometry;

import me.thehutch.fusion.api.maths.Vector2;

/**
 * @author thehutch
 */
public final class Rectangle {
	private final int x;
	private final int y;
	private final int width;
	private final int height;

	/**
	 * The default constructor for {@link Rectangle}.
	 * <p>
	 * The rectangle's position is at (0, 0).
	 *
	 * @param width  The width of the rectangle
	 * @param height The height of the rectangle
	 */
	public Rectangle(int width, int height) {
		this(0, 0, width, height);
	}

	/**
	 * Constructor for {@link Rectangle}.
	 *
	 * @param position The position of the rectangle (x, y)
	 * @param size     The size of the rectangle (width, height)
	 */
	public Rectangle(Vector2 position, Vector2 size) {
		this(position.getFloorX(), position.getFloorY(), size.getFloorX(), size.getFloorY());
	}

	/**
	 * Constructor for {@link Rectangle}.
	 *
	 * @param x      The x-coordinate of the rectangle
	 * @param y      The y-coordinate of the rectangle
	 * @param width  The width of the rectangle
	 * @param height The height of the rectangle
	 */
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * @return The x-coordinate of the rectangle
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return The x-coordinate of the rectangle
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return The width of the rectangle
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return The height of the rectangle
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * A new {@link Vector2} containing the x, y coordinates of rectangle.
	 *
	 * @return The position of the rectangle
	 */
	public Vector2 getPosition() {
		return new Vector2(x, y);
	}

	/**
	 * A new {@link Vector2} containing the width and height of rectangle.
	 *
	 * @return The size of the rectangle
	 */
	public Vector2 getSize() {
		return new Vector2(width, height);
	}

	/**
	 * Returns the area of the rectangle.
	 * <p>
	 * {@code area = width * height}
	 *
	 * @return The area of the rectangle
	 */
	public int getArea() {
		return width * height;
	}

	/**
	 * Returns the aspect ratio of the rectangle.
	 * <p>
	 * {@code aspect ratio = width / height}
	 *
	 * @return The aspect ratio of the rectangle
	 */
	public float getAspectRatio() {
		return (float) width / height;
	}
}
