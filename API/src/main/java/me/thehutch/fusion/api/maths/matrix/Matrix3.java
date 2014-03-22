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

import me.thehutch.fusion.api.maths.vector.Vector3f;

/**
 * @author thehutch
 */
public class Matrix3 {
	public static final Matrix3 IDENTITY = new Matrix3(1, 0, 0,
													   0, 1, 0,
													   0, 0, 1);
	private static final int SIZE = 3;
	private final float[][] data;

	public Matrix3(float[][] matrix) {
		if (matrix.length != SIZE && matrix[0].length == SIZE) {
			throw new IllegalArgumentException("A Matrix3 can only be assigned a float[][] of equal size.");
		}
		this.data = matrix;
	}

	public Matrix3(float m00, float m10, float m20,
				   float m01, float m11, float m21,
				   float m02, float m12, float m22) {
		this.data = new float[SIZE][SIZE];

		this.data[0][0] = m00;
		this.data[0][1] = m01;
		this.data[0][2] = m02;
		this.data[1][0] = m10;
		this.data[1][1] = m11;
		this.data[1][2] = m12;
		this.data[2][0] = m20;
		this.data[2][1] = m21;
		this.data[2][2] = m22;
	}

	public float get(int col, int row) {
		return data[col][row];
	}

	public Vector3f getRow(int row) {
		return new Vector3f(get(0, row), get(1, row), get(2, row));
	}

	public Vector3f getColumn(int col) {
		return new Vector3f(get(col, 0), get(col, 1), get(col, 2));
	}

	public Matrix3 mul(Matrix3 m) {
		final float[][] matrix = new float[SIZE][SIZE];
		for (int col = 0; col < SIZE; ++col) {
			for (int row = 0; row < SIZE; ++row) {
				matrix[col][row] = getColumn(row).dot(m.getRow(col));
			}
		}
		return new Matrix3(matrix);
	}

	public Matrix3 transpose() {
		final float[][] matrix = new float[SIZE][SIZE];
		for (int col = 0; col < SIZE; ++col) {
			for (int row = 0; row < SIZE; ++row) {
				matrix[row][col] = get(col, row);
			}
		}
		return new Matrix3(matrix);
	}

	public float[] toArray() {
		return new float[] {
			data[0][0], data[1][0], data[2][0],
			data[0][1], data[1][1], data[2][1],
			data[0][2], data[1][2], data[2][2]
		};
	}

	public static Matrix3 createTranslation(Vector3f vec) {
		return createTranslation(vec.getX(), vec.getY());
	}

	public static Matrix3 createTranslation(float x, float y) {
		return new Matrix3(1, 0, x,
						   0, 1, y,
						   0, 0, 1);
	}

	public static Matrix3 createScale(float scale) {
		return createScale(scale, scale, scale);
	}

	public static Matrix3 createScale(Vector3f vec) {
		return createScale(vec.getX(), vec.getY(), vec.getZ());
	}

	public static Matrix3 createScale(float x, float y, float z) {
		return new Matrix3(x, 0, 0,
						   0, y, 0,
						   0, 0, z);
	}
}
