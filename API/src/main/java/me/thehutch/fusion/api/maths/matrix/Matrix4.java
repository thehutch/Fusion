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

import me.thehutch.fusion.api.maths.MathsHelper;
import me.thehutch.fusion.api.maths.vector.Vector3;
import me.thehutch.fusion.api.maths.vector.Vector4;

/**
 * @author thehutch
 */
public class Matrix4 {
	public static final Matrix4 IDENTITY = new Matrix4(1, 0, 0, 0,
													   0, 1, 0, 0,
													   0, 0, 1, 0,
													   0, 0, 0, 1);
	private static final int SIZE = 4;
	private final float[][] m;

	public Matrix4(float[][] matrix) {
		if (matrix.length != SIZE && matrix[0].length == SIZE) {
			throw new IllegalArgumentException("A Matrix4f can only be assigned a float[][] of equal size.");
		}
		this.m = matrix;
	}

	public Matrix4(float m00, float m10, float m20, float m30,
				   float m01, float m11, float m21, float m31,
				   float m02, float m12, float m22, float m32,
				   float m03, float m13, float m23, float m33) {
		this.m = new float[SIZE][SIZE];

		this.m[0][0] = m00;
		this.m[0][1] = m01;
		this.m[0][2] = m02;
		this.m[0][3] = m03;
		this.m[1][0] = m10;
		this.m[1][1] = m11;
		this.m[1][2] = m12;
		this.m[1][3] = m13;
		this.m[2][0] = m20;
		this.m[2][1] = m21;
		this.m[2][2] = m22;
		this.m[2][3] = m23;
		this.m[3][0] = m30;
		this.m[3][1] = m31;
		this.m[3][2] = m32;
		this.m[3][3] = m33;
	}

	public float get(int col, int row) {
		return m[col][row];
	}

	public Vector4 getRow(int row) {
		return new Vector4(get(0, row), get(1, row), get(2, row), get(3, row));
	}

	public Vector4 getColumn(int col) {
		return new Vector4(get(col, 0), get(col, 1), get(col, 2), get(col, 3));
	}

	public Matrix4 mul(Matrix4 m) {
		final float[][] matrix = new float[SIZE][SIZE];
		for (int col = 0; col < SIZE; ++col) {
			for (int row = 0; row < SIZE; ++row) {
				matrix[col][row] = getColumn(row).dot(m.getRow(col));
			}
		}
		return new Matrix4(matrix);
	}

	public Matrix4 transpose() {
		final float[][] matrix = new float[SIZE][SIZE];
		for (int col = 0; col < SIZE; ++col) {
			for (int row = 0; row < SIZE; ++row) {
				matrix[row][col] = get(col, row);
			}
		}
		return new Matrix4(matrix);
	}

	public float[] toArray() {
		return new float[] {
			m[0][0], m[1][0], m[2][0], m[3][0],
			m[0][1], m[1][1], m[2][1], m[3][1],
			m[0][2], m[1][2], m[2][2], m[3][2],
			m[0][3], m[1][3], m[2][3], m[3][3]
		};
	}

	public static Matrix4 createTranslation(Vector3 vec) {
		return createTranslation(vec.getX(), vec.getY(), vec.getZ());
	}

	public static Matrix4 createTranslation(float x, float y, float z) {
		return new Matrix4(1, 0, 0, x,
						   0, 1, 0, y,
						   0, 0, 1, z,
						   0, 0, 0, 1);
	}

	public static Matrix4 createRotation(Vector3 forward, Vector3 up) {
		final Vector3 right = forward.cross(up);
		return new Matrix4(right.getX(), right.getY(), right.getZ(), 0,
						   up.getX(), up.getY(), up.getZ(), 0,
						   forward.getX(), forward.getY(), forward.getZ(), 0,
						   0, 0, 0, 1);
	}

	public static Matrix4 createScale(float scale) {
		return createScale(scale, scale, scale, scale);
	}

	public static Matrix4 createScale(Vector3 vec) {
		return createScale(vec.getX(), vec.getY(), vec.getZ(), 1.0f);
	}

	public static Matrix4 createScale(Vector4 vec) {
		return createScale(vec.getX(), vec.getY(), vec.getZ(), vec.getW());
	}

	public static Matrix4 createScale(float x, float y, float z, float w) {
		return new Matrix4(x, 0, 0, 0,
						   0, y, 0, 0,
						   0, 0, z, 0,
						   0, 0, 0, w);
	}

	public static Matrix4 createPerspective(float fov, float aspectRatio, float near, float far) {
		final float yScale = (float) (1.0f / Math.tan(MathsHelper.toRadians(fov / 2.0f)));
		final float xScale = yScale / aspectRatio;
		final float frustumLength = far - near;
		return new Matrix4(xScale, 0, 0, 0,
						   0, yScale, 0, 0,
						   0, 0, ((far + near) / frustumLength), (2.0f * far * near) / frustumLength,
						   0, 0, 1, 0);
	}

	public static Matrix4 createOrthographic(float right, float left, float top, float bottom, float near, float far) {
		return new Matrix4(2.0f / (right - left), 0, 0, -(right + left) / (right - left),
						   0, 2.0f / (top - bottom), 0, -(top + bottom) / (top - bottom),
						   0, 0, -2.0f / (far - near), -(far + near) / (far - near),
						   0, 0, 0, 1);
	}
}
