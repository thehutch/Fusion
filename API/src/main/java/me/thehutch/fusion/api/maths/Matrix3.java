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
public final class Matrix3 {

	/**
	 * The column size.
	 */
	public static final int COL_SIZE = 3;
	/**
	 * The row size.
	 */
	public static final int ROW_SIZE = 3;
	/**
	 * The size of the matrix.
	 */
	public static final int SIZE = COL_SIZE * ROW_SIZE;
	/**
	 * The identity matrix.
	 */
	public static final Matrix3 IDENTITY = new Matrix3(1, 0, 0,
													   0, 1, 0,
													   0, 0, 1);
	private final float m00, m01, m02;
	private final float m10, m11, m12;
	private final float m20, m21, m22;

	/**
	 * Default constructor for {@link Matrix4}.
	 *
	 * @param m00 The first element of the first column
	 * @param m01 The first element of the second column
	 * @param m02 The first element of the third column
	 * @param m10 The second element of the first column
	 * @param m11 The second element of the second column
	 * @param m12 The second element of the third column
	 * @param m20 The third element of the first column
	 * @param m21 The third element of the second column
	 * @param m22 The third element of the third column
	 */
	public Matrix3(float m00, float m01, float m02,
				   float m10, float m11, float m12,
				   float m20, float m21, float m22) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
	}

	/**
	 * Copy constructor for {@link Matrix4}.
	 *
	 * @param m The matrix to copy
	 */
	public Matrix3(Matrix3 m) {
		this(m.m00, m.m01, m.m02,
			 m.m10, m.m11, m.m12,
			 m.m20, m.m21, m.m22);
	}

	/**
	 * Adds two {@link Matrix3} together.
	 *
	 * @param m The {@link Matrix3} to add to this {@link Matrix3}
	 *
	 * @return A new {@link Matrix3}
	 */
	public Matrix3 add(Matrix3 m) {
		return new Matrix3(m00 + m.m00, m01 + m.m01, m02 + m.m02,
						   m10 + m.m10, m11 + m.m11, m12 + m.m12,
						   m20 + m.m20, m21 + m.m21, m22 + m.m22);
	}

	/**
	 * Subtract two {@link Matrix3} from each other.
	 *
	 * @param m The {@link Matrix3} to subtract from this {@link Matrix3}
	 *
	 * @return A new {@link Matrix3}
	 */
	public Matrix3 sub(Matrix3 m) {
		return new Matrix3(m00 - m.m00, m01 - m.m01, m02 - m.m02,
						   m10 - m.m10, m11 - m.m11, m12 - m.m12,
						   m20 - m.m20, m21 - m.m21, m22 - m.m22);
	}

	/**
	 * Multiplies this {@link Matrix3} by a scalar.
	 *
	 * @param scalar The scalar to multiply by
	 *
	 * @return A new {@link Matrix3}
	 */
	public Matrix3 mul(float scalar) {
		return new Matrix3(m00 * scalar, m01 * scalar, m02 * scalar,
						   m10 * scalar, m11 * scalar, m12 * scalar,
						   m20 * scalar, m21 * scalar, m22 * scalar);
	}

	/**
	 * Multiplies this matrix by a {@link Vector3}.
	 *
	 * @param vec The {@link Vector3} to multiply by
	 *
	 * @return A new {@link Vector3}
	 */
	public Vector3 mul(Vector3 vec) {
		return mul(vec.getX(), vec.getY(), vec.getZ());
	}

	/**
	 * Multiplies this matrix by three floats (A vector of size 3).
	 *
	 * @param x The x-component of the vector
	 * @param y The y-component of the vector
	 * @param z The z-component of the vector
	 *
	 * @return A new {@link Vector3}
	 */
	public Vector3 mul(float x, float y, float z) {
		return new Vector3(m00 * x + m01 * y + m02 * z,
						   m10 * x + m11 * y + m12 * z,
						   m20 * x + m21 * y + m22 * z);
	}

	/**
	 * Multiples two {@link Matrix3} together.
	 *
	 * @param m The {@link Matrix3} to multiply with this {@link Matrix3}
	 *
	 * @return A new {@link Matrix3}
	 */
	public Matrix3 mul(Matrix3 m) {
		return new Matrix3(m00 * m.m00 + m01 * m.m10 + m02 * m.m20,
						   m00 * m.m01 + m01 * m.m11 + m02 * m.m21,
						   m00 * m.m02 + m01 * m.m12 + m02 * m.m22,
						   m10 * m.m00 + m11 * m.m10 + m12 * m.m20,
						   m10 * m.m01 + m11 * m.m11 + m12 * m.m21,
						   m10 * m.m02 + m11 * m.m12 + m12 * m.m22,
						   m20 * m.m00 + m21 * m.m10 + m22 * m.m20,
						   m20 * m.m01 + m21 * m.m11 + m22 * m.m21,
						   m20 * m.m02 + m21 * m.m12 + m22 * m.m22);
	}

	/**
	 * Divides this {@link Matrix3} by a scalar.
	 *
	 * @param scalar The scalr to divide by
	 *
	 * @return A new {@link Matrix3}
	 */
	public Matrix3 div(float scalar) {
		final float rScalar = 1.0f / scalar;
		return new Matrix3(m00 * rScalar, m01 * rScalar, m02 * rScalar,
						   m10 * rScalar, m11 * rScalar, m12 * rScalar,
						   m20 * rScalar, m21 * rScalar, m22 * rScalar);
	}

	/**
	 * Creates the transpose of this matrix.
	 *
	 * @return A new transposed {@link Matrix4}
	 */
	public Matrix3 transpose() {
		return new Matrix3(m00, m10, m20,
						   m01, m11, m21,
						   m02, m12, m22);
	}

	/**
	 * @return The determinant of this matrix
	 */
	public float determinant() {
		return m00 * ((m11 * m22) - (m12 * m21))
			   - m01 * ((m10 * m22) - (m12 * m20))
			   + m02 * ((m10 * m21) - (m11 * m20));
	}

	/**
	 * Creates the inverse of this matrix. If the determinant is zero then this
	 * method will fail and return null.
	 *
	 * @return A new inverse {@link Matrix3}
	 */
	public Matrix3 invert() {
		final float det = determinant();
		if (det == 0.0f) {
			return null;
		}
		return new Matrix3((m11 * m22 - m21 * m12) / det, -(m01 * m22 - m21 * m02) / det, (m01 * m12 - m02 * m11) / det,
						   -(m10 * m22 - m20 * m12) / det, (m00 * m22 - m20 * m02) / det, -(m00 * m12 - m10 * m02) / det,
						   (m10 * m21 - m20 * m11) / det, -(m00 * m21 - m20 * m01) / det, (m00 * m11 - m01 * m10) / det);
	}

	/**
	 * Inserts the matrix into the provided float[]. The matrix is inserted the
	 * beginning of the array.
	 *
	 * @param array       The array to inser the matrix into
	 * @param columnMajor True if to insert the matrix in column order
	 */
	public void toArray(float[] array, boolean columnMajor) {
		// Check if the array is not null and has space
		if (array == null || array.length < SIZE) {
			throw new IllegalArgumentException("Array null or too small to hold Matrix4");
		}

		// Insert the matrix into the array in the correct order
		if (columnMajor) {
			array[0] = m00;
			array[1] = m10;
			array[2] = m20;
			array[3] = m01;
			array[4] = m11;
			array[5] = m21;
			array[6] = m02;
			array[7] = m12;
			array[8] = m22;
		} else {
			array[0] = m00;
			array[1] = m01;
			array[2] = m02;
			array[3] = m10;
			array[4] = m11;
			array[5] = m12;
			array[6] = m20;
			array[7] = m21;
			array[8] = m22;
		}
	}

	/**
	 * Creates a new float[] and inserts the matrix into it.
	 *
	 * @param columnMajor True if to insert the matrix in column order
	 *
	 * @return A new float[]
	 */
	public float[] toArray(boolean columnMajor) {
		if (columnMajor) {
			return new float[] {
				m00, m10, m20,
				m01, m11, m21,
				m02, m12, m22
			};
		} else {
			return new float[] {
				m00, m01, m02,
				m10, m11, m12,
				m20, m21, m22
			};
		}
	}

	/**
	 * Create a new translation matrix.
	 *
	 * @param vec The vector to translate by
	 *
	 * @return A new translation {@link Matrix3}
	 */
	public static Matrix3 newTranslation(Vector2 vec) {
		return new Matrix3(1.0f, 0.0f, 0.0f,
						   0.0f, 1.0f, 0.0f,
						   vec.getX(), vec.getY(), 1.0f);
	}

	/**
	 * Creates a new translation matrix.
	 *
	 * @param x The x-component of the translation
	 * @param y The y-component of the translation
	 *
	 * @return A new translation {@link Matrix3}
	 */
	public static Matrix3 newTranslation(float x, float y) {
		return new Matrix3(1.0f, 0.0f, 0.0f,
						   0.0f, 1.0f, 0.0f,
						   x, y, 1.0f);
	}

	/**
	 * Creates a new scale matrix.
	 *
	 * @param scale The amount to scale each axis by
	 *
	 * @return A new scale {@link Matrix3}
	 */
	public static Matrix3 newScale(float scale) {
		return new Matrix3(scale, 0.0f, 0.0f,
						   0.0f, scale, 0.0f,
						   0.0f, 0.0f, 1.0f);
	}

	/**
	 * Creates a new scale matrix.
	 *
	 * @param scale A vector to scale each component by
	 *
	 * @return A new scale {@link Matrix3}
	 */
	public static Matrix3 newScale(Vector2 scale) {
		return new Matrix3(scale.getX(), 0.0f, 0.0f,
						   0.0f, scale.getY(), 0.0f,
						   0.0f, 0.0f, 1.0f);
	}

	/**
	 * Creates a new scale matrix
	 *
	 * @param scaleX The x-component of the scale
	 * @param scaleY The y-component of the scale
	 *
	 * @return A new scale {@link Matrix3}
	 */
	public static Matrix3 newScale(float scaleX, float scaleY) {
		return new Matrix3(scaleX, 0.0f, 0.0f,
						   0.0f, scaleY, 0.0f,
						   0.0f, 0.0f, 1.0f);
	}

	/**
	 * Creates a new rotation matrix.
	 *
	 * @param rot The quaternion to rotate by
	 *
	 * @return A new rotation {@link Matrix3}
	 */
	public static Matrix3 newRotation(Quaternion rot) {
		final Quaternion normRot = rot.normalise();
		final float x = normRot.getX();
		final float y = normRot.getY();
		final float z = normRot.getZ();
		final float w = normRot.getW();
		return new Matrix3(1.0f - (2.0f * y * y) - (2.0f * z * z), (2.0f * x * y) - (2.0f * w * z), (2.0f * x * z) + (2.0f * w * y),
						   (2.0f * x * y) + (2.0f * w * z), 1.0f - (2.0f * x * x) - (2.0f * z * z), 2.0f * y * z - 2.0f * w * x,
						   (2.0f * x * z) - (2.0f * w * y), (2.0f * y * z) + (2.0f * x * w), 1.0f - (2.0f * x * x) - (2.0f * y * y));
	}
}
