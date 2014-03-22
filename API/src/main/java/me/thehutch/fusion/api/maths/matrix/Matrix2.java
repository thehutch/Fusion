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
package me.thehutch.fusion.api.maths.matrix;

import me.thehutch.fusion.api.maths.vector.Vector2f;

/**
 * @author thehutch
 */
public class Matrix2 {
	public static final Matrix2 IDENTITY = new Matrix2(1, 0,
													   0, 1);
	private static final int SIZE = 2;
	private final float[][] data;

	public Matrix2(float[][] matrix) {
		if (matrix.length != SIZE && matrix[0].length == SIZE) {
			throw new IllegalArgumentException("A Matrix2 can only be assigned a float[][] of equal size.");
		}
		this.data = matrix;
	}

	public Matrix2(float m00, float m10,
				   float m01, float m11) {
		this.data = new float[SIZE][SIZE];

		this.data[0][0] = m00;
		this.data[0][1] = m01;
		this.data[1][0] = m10;
		this.data[1][1] = m11;
	}

	public float get(int col, int row) {
		return data[col][row];
	}

	public Vector2f getRow(int row) {
		return new Vector2f(get(0, row), get(1, row));
	}

	public Vector2f getColumn(int col) {
		return new Vector2f(get(col, 0), get(col, 1));
	}

	public Matrix2 mul(Matrix2 m) {
		final float[][] matrix = new float[SIZE][SIZE];
		for (int col = 0; col < SIZE; ++col) {
			for (int row = 0; row < SIZE; ++row) {
				matrix[col][row] = getColumn(row).dot(m.getRow(col));
			}
		}
		return new Matrix2(matrix);
	}

	public Matrix2 transpose() {
		final float[][] matrix = new float[SIZE][SIZE];
		for (int col = 0; col < SIZE; ++col) {
			for (int row = 0; row < SIZE; ++row) {
				matrix[row][col] = get(col, row);
			}
		}
		return new Matrix2(matrix);
	}

	public float[] toArray() {
		return new float[] {
			data[0][0], data[1][0],
			data[0][1], data[1][1]
		};
	}

	public static Matrix2 createScale(float scale) {
		return createScale(scale, scale);
	}

	public static Matrix2 createScale(Vector2f vec) {
		return createScale(vec.getX(), vec.getY());
	}

	public static Matrix2 createScale(float x, float y) {
		return new Matrix2(x, 0,
						   0, y);
	}
}
