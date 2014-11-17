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
	private final int mX;
	private final int mY;
	private final int mWidth;
	private final int mHeight;

	/**
	 * The default constructor for {@link Rectangle}.
	 * <p>
	 * The rectangle's position is at (0, 0).
	 *
	 * @param width  The mWidth of the rectangle
	 * @param height The mHeight of the rectangle
	 */
	public Rectangle(int width, int height) {
		this(0, 0, width, height);
	}

	/**
	 * Constructor for {@link Rectangle}.
	 *
	 * @param position The position of the rectangle (mX, mY)
	 * @param size     The size of the rectangle (mWidth, mHeight)
	 */
	public Rectangle(Vector2 position, Vector2 size) {
		this(position.getFloorX(), position.getFloorY(), size.getFloorX(), size.getFloorY());
	}

	/**
	 * Constructor for {@link Rectangle}.
	 *
	 * @param x      The mX-coordinate of the rectangle
	 * @param y      The mY-coordinate of the rectangle
	 * @param width  The mWidth of the rectangle
	 * @param height The mHeight of the rectangle
	 */
	public Rectangle(int x, int y, int width, int height) {
		mX = x;
		mY = y;
		mWidth = width;
		mHeight = height;
	}

	/**
	 * @return The mX-coordinate of the rectangle
	 */
	public int getX() {
		return mX;
	}

	/**
	 * @return The mX-coordinate of the rectangle
	 */
	public int getY() {
		return mY;
	}

	/**
	 * @return The mWidth of the rectangle
	 */
	public int getWidth() {
		return mWidth;
	}

	/**
	 * @return The mHeight of the rectangle
	 */
	public int getHeight() {
		return mHeight;
	}

	/**
	 * A new {@link Vector2} containing the mX, mY coordinates of rectangle.
	 *
	 * @return The position of the rectangle
	 */
	public Vector2 getPosition() {
		return new Vector2(mX, mY);
	}

	/**
	 * A new {@link Vector2} containing the mWidth and mHeight of rectangle.
	 *
	 * @return The size of the rectangle
	 */
	public Vector2 getSize() {
		return new Vector2(mWidth, mHeight);
	}

	/**
	 * Returns the area of the rectangle.
	 * <p>
	 * {@code area = mWidth * mHeight}
	 *
	 * @return The area of the rectangle
	 */
	public int getArea() {
		return mWidth * mHeight;
	}

	/**
	 * Returns the aspect ratio of the rectangle.
	 * <p>
	 * {@code aspect ratio = mWidth / mHeight}
	 *
	 * @return The aspect ratio of the rectangle
	 */
	public float getAspectRatio() {
		return (float) mWidth / mHeight;
	}
}
