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
public final class Matrix4 {

	/**
	 * The column size.
	 */
	public static final int COL_SIZE = 4;
	/**
	 * The row size.
	 */
	public static final int ROW_SIZE = 4;
	/**
	 * The size of the matrix.
	 */
	public static final int SIZE = COL_SIZE * ROW_SIZE;
	/**
	 * The identity matrix.
	 */
	public static final Matrix4 IDENTITY = new Matrix4(1.0f, 0.0f, 0.0f, 0.0f,
													   0.0f, 1.0f, 0.0f, 0.0f,
													   0.0f, 0.0f, 1.0f, 0.0f,
													   0.0f, 0.0f, 0.0f, 1.0f);
	private final float m00, m01, m02, m03;
	private final float m10, m11, m12, m13;
	private final float m20, m21, m22, m23;
	private final float m30, m31, m32, m33;

	/**
	 * Default constructor for {@link Matrix4}.
	 *
	 * @param m00 The first element of the first column
	 * @param m01 The first element of the second column
	 * @param m02 The first element of the third column
	 * @param m03 The first element of the fourth column
	 * @param m10 The second element of the first column
	 * @param m11 The second element of the second column
	 * @param m12 The second element of the third column
	 * @param m13 The second element of the fourth column
	 * @param m20 The third element of the first column
	 * @param m21 The third element of the second column
	 * @param m22 The third element of the third column
	 * @param m23 The third element of the fourth column
	 * @param m30 The fourth element of the first column
	 * @param m31 The fourth element of the second column
	 * @param m32 The fourth element of the third column
	 * @param m33 The fourth element of the fourth column
	 */
	public Matrix4(float m00, float m01, float m02, float m03,
				   float m10, float m11, float m12, float m13,
				   float m20, float m21, float m22, float m23,
				   float m30, float m31, float m32, float m33) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	/**
	 * Copy constructor for {@link Matrix4}.
	 *
	 * @param m The matrix to copy
	 */
	public Matrix4(Matrix4 m) {
		this(m.m00, m.m01, m.m02, m.m03,
			 m.m10, m.m11, m.m12, m.m13,
			 m.m20, m.m21, m.m22, m.m23,
			 m.m30, m.m31, m.m32, m.m33);
	}

	/**
	 * Adds two {@link Matrix4} together.
	 *
	 * @param m The {@link Matrix4} to add to this {@link Matrix4}
	 *
	 * @return A new {@link Matrix4}
	 */
	public Matrix4 add(Matrix4 m) {
		return new Matrix4(m00 + m.m00, m01 + m.m01, m02 + m.m02, m03 + m.m03,
						   m10 + m.m10, m11 + m.m11, m12 + m.m12, m13 + m.m13,
						   m20 + m.m20, m21 + m.m21, m22 + m.m22, m23 + m.m23,
						   m30 + m.m30, m31 + m.m31, m32 + m.m32, m33 + m.m33);
	}

	/**
	 * Subtract two {@link Matrix4} from each other.
	 *
	 * @param m The {@link Matrix4} to subtract from this {@link Matrix4}
	 *
	 * @return A new {@link Matrix4}
	 */
	public Matrix4 sub(Matrix4 m) {
		return new Matrix4(m00 - m.m00, m01 - m.m01, m02 - m.m02, m03 - m.m03,
						   m10 - m.m10, m11 - m.m11, m12 - m.m12, m13 - m.m13,
						   m20 - m.m20, m21 - m.m21, m22 - m.m22, m23 - m.m23,
						   m30 - m.m30, m31 - m.m31, m32 - m.m32, m33 - m.m33);
	}

	/**
	 * Multiplies this {@link Matrix4} by a scalar.
	 *
	 * @param scalar The scalar to multiply by
	 *
	 * @return A new {@link Matrix4}
	 */
	public Matrix4 mul(float scalar) {
		return new Matrix4(m00 * scalar, m01 * scalar, m02 * scalar, m03 * scalar,
						   m10 * scalar, m11 * scalar, m12 * scalar, m13 * scalar,
						   m20 * scalar, m21 * scalar, m22 * scalar, m23 * scalar,
						   m30 * scalar, m31 * scalar, m32 * scalar, m33 * scalar);
	}

	/**
	 * Multiplies this matrix by a {@link Vector4}.
	 *
	 * @param vec The {@link Vector4} to multiply by
	 *
	 * @return A new {@link Vector4}
	 */
	public Vector4 mul(Vector4 vec) {
		return mul(vec.getX(), vec.getY(), vec.getZ(), vec.getW());
	}

	/**
	 * Multiples this matrix by four floats (A vector of size 4).
	 *
	 * @param x The x-component of the vector
	 * @param y The y-component of the vector
	 * @param z The z-component of the vector
	 * @param w The w-component of the vector
	 *
	 * @return A new {@link Vector4}
	 */
	public Vector4 mul(float x, float y, float z, float w) {
		return new Vector4(m00 * x + m01 * y + m02 * z + m03 * w,
						   m10 * x + m11 * y + m12 * z + m13 * w,
						   m20 * x + m21 * y + m22 * z + m23 * w,
						   m30 * x + m31 * y + m32 * z + m33 * w);
	}

	/**
	 * Multiples two {@link Matrix4} together.
	 *
	 * @param m The {@link Matrix4} to multiply with this {@link Matrix4}
	 *
	 * @return A new {@link Matrix4}
	 */
	public Matrix4 mul(Matrix4 m) {
		return new Matrix4(m00 * m.m00 + m01 * m.m10 + m02 * m.m20 + m03 * m.m30,
						   m00 * m.m01 + m01 * m.m11 + m02 * m.m21 + m03 * m.m31,
						   m00 * m.m02 + m01 * m.m12 + m02 * m.m22 + m03 * m.m32,
						   m00 * m.m03 + m01 * m.m13 + m02 * m.m23 + m03 * m.m33,
						   m10 * m.m00 + m11 * m.m10 + m12 * m.m20 + m13 * m.m30,
						   m10 * m.m01 + m11 * m.m11 + m12 * m.m21 + m13 * m.m31,
						   m10 * m.m02 + m11 * m.m12 + m12 * m.m22 + m13 * m.m32,
						   m10 * m.m03 + m11 * m.m13 + m12 * m.m23 + m13 * m.m33,
						   m20 * m.m00 + m21 * m.m10 + m22 * m.m20 + m23 * m.m30,
						   m20 * m.m01 + m21 * m.m11 + m22 * m.m21 + m23 * m.m31,
						   m20 * m.m02 + m21 * m.m12 + m22 * m.m22 + m23 * m.m32,
						   m20 * m.m03 + m21 * m.m13 + m22 * m.m23 + m23 * m.m33,
						   m30 * m.m00 + m31 * m.m10 + m32 * m.m20 + m33 * m.m30,
						   m30 * m.m01 + m31 * m.m11 + m32 * m.m21 + m33 * m.m31,
						   m30 * m.m02 + m31 * m.m12 + m32 * m.m22 + m33 * m.m32,
						   m30 * m.m03 + m31 * m.m13 + m32 * m.m23 + m33 * m.m33);
	}

	/**
	 * Divides this {@link Matrix4} by a scalar.
	 *
	 * @param scalar The scalr to divide by
	 *
	 * @return A new {@link Matrix4}
	 */
	public Matrix4 div(float scalar) {
		final float rScalar = 1.0f / scalar;
		return new Matrix4(m00 * rScalar, m01 * rScalar, m02 * rScalar, m03 * rScalar,
						   m10 * rScalar, m11 * rScalar, m12 * rScalar, m13 * rScalar,
						   m20 * rScalar, m21 * rScalar, m22 * rScalar, m23 * rScalar,
						   m30 * rScalar, m31 * rScalar, m32 * rScalar, m33 * rScalar);
	}

	/**
	 * Creates the transpose of this matrix.
	 *
	 * @return A new transposed {@link Matrix4}
	 */
	public Matrix4 transpose() {
		return new Matrix4(m00, m10, m20, m30,
						   m01, m11, m21, m31,
						   m02, m12, m22, m32,
						   m03, m13, m23, m33);
	}

	/**
	 * @return The determinant of this matrix
	 */
	public float determinant() {
		return m00 * (m11 * m22 * m33 + m21 * m32 * m13 + m31 * m12 * m23 - m31 * m22 * m13 - m11 * m32 * m23 - m21 * m12 * m33)
			   - m10 * (m01 * m22 * m33 + m21 * m32 * m03 + m31 * m02 * m23 - m31 * m22 * m03 - m01 * m32 * m23 - m21 * m02 * m33)
			   + m20 * (m01 * m12 * m33 + m11 * m32 * m03 + m31 * m02 * m13 - m31 * m12 * m03 - m01 * m32 * m13 - m11 * m02 * m33)
			   - m30 * (m01 * m12 * m23 + m11 * m22 * m03 + m21 * m02 * m13 - m21 * m12 * m03 - m01 * m22 * m13 - m11 * m02 * m23);
	}

	/**
	 * Creates the inverse of this matrix. If the determinant is zero then this
	 * method will fail and return null.
	 *
	 * @return A new inverse {@link Matrix4}
	 */
	public Matrix4 invert() {
		final float det = determinant();
		if (det == 0.0f) {
			return null;
		}
		return new Matrix4(det3(m11, m21, m31, m12, m22, m32, m13, m23, m33) / det, -det3(m01, m21, m31, m02, m22, m32, m03, m23, m33) / det,
						   det3(m01, m11, m31, m02, m12, m32, m03, m13, m33) / det, -det3(m01, m11, m21, m02, m12, m22, m03, m13, m23) / det,
						   -det3(m10, m20, m30, m12, m22, m32, m13, m23, m33) / det, det3(m00, m20, m30, m02, m22, m32, m03, m23, m33) / det,
						   -det3(m00, m10, m30, m02, m12, m32, m03, m13, m33) / det, det3(m00, m10, m20, m02, m12, m22, m03, m13, m23) / det,
						   det3(m10, m20, m30, m11, m21, m31, m13, m23, m33) / det, -det3(m00, m20, m30, m01, m21, m31, m03, m23, m33) / det,
						   det3(m00, m10, m30, m01, m11, m31, m03, m13, m33) / det, -det3(m00, m10, m20, m01, m11, m21, m03, m13, m23) / det,
						   -det3(m10, m20, m30, m11, m21, m31, m12, m22, m32) / det, det3(m00, m20, m30, m01, m21, m31, m02, m22, m32) / det,
						   -det3(m00, m10, m30, m01, m11, m31, m02, m12, m32) / det, det3(m00, m10, m20, m01, m11, m21, m02, m12, m22) / det);
	}

	/**
	 * Inserts the matrix into the provided float[]. The matrix is inserted the
	 * beginning of the array.
	 *
	 * @param array      The array to inser the matrix into
	 * @param columMajor True if to insert the matrix in column order
	 */
	public void toArray(float[] array, boolean columMajor) {
		// Check if the array is not null and has space
		if (array == null || array.length < SIZE) {
			throw new IllegalArgumentException("Array null or too small to hold Matrix4");
		}

		// Insert the matrix into the array in the correct order
		if (columMajor) {
			array[0] = m00;
			array[1] = m10;
			array[2] = m20;
			array[3] = m30;
			array[4] = m01;
			array[5] = m11;
			array[6] = m21;
			array[7] = m31;
			array[8] = m02;
			array[9] = m12;
			array[10] = m22;
			array[11] = m32;
			array[12] = m03;
			array[13] = m13;
			array[14] = m23;
			array[15] = m33;
		} else {
			array[0] = m00;
			array[1] = m01;
			array[2] = m02;
			array[3] = m03;
			array[4] = m10;
			array[5] = m11;
			array[6] = m12;
			array[7] = m13;
			array[8] = m20;
			array[9] = m21;
			array[10] = m22;
			array[11] = m23;
			array[12] = m30;
			array[13] = m31;
			array[14] = m32;
			array[15] = m33;
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
				m00, m10, m20, m30,
				m01, m11, m21, m31,
				m02, m12, m22, m32,
				m03, m13, m23, m33
			};
		} else {
			return new float[] {
				m00, m01, m02, m03,
				m10, m11, m12, m13,
				m20, m21, m22, m23,
				m30, m31, m32, m33
			};
		}
	}

	/**
	 * Converts this matrix into a new {@link Matrix3}. The third row and column
	 * are removed.
	 *
	 * @return A new {@link Matrix3}
	 */
	public Matrix3 toMatrix3() {
		return new Matrix3(m00, m01, m02,
						   m10, m11, m12,
						   m20, m21, m22);
	}

	/**
	 * Creates a new translation matrix.
	 *
	 * @param vec The vector to translate by
	 *
	 * @return A new translation {@link Matrix4}
	 */
	public static Matrix4 newTranslation(Vector3 vec) {
		return new Matrix4(1.0f, 0.0f, 0.0f, 0.0f,
						   0.0f, 1.0f, 0.0f, 0.0f,
						   0.0f, 0.0f, 1.0f, 0.0f,
						   vec.getX(), vec.getY(), vec.getZ(), 1.0f);
	}

	/**
	 * Creates a new translation matrix.
	 *
	 * @param x The x-component of the translation
	 * @param y The y-component of the translation
	 * @param z The z-component of the translation
	 *
	 * @return A new translation {@link Matrix4}
	 */
	public static Matrix4 newTranslation(float x, float y, float z) {
		return new Matrix4(1.0f, 0.0f, 0.0f, 0.0f,
						   0.0f, 1.0f, 0.0f, 0.0f,
						   0.0f, 0.0f, 1.0f, 0.0f,
						   x, y, z, 1.0f);
	}

	/**
	 * Creates a new scale matrix.
	 *
	 * @param scale The amount to scale each axis by
	 *
	 * @return A new scale {@link Matrix4}
	 */
	public static Matrix4 newScale(float scale) {
		return new Matrix4(scale, 0.0f, 0.0f, 0.0f,
						   0.0f, scale, 0.0f, 0.0f,
						   0.0f, 0.0f, scale, 0.0f,
						   0.0f, 0.0f, 0.0f, 1.0f);
	}

	/**
	 * Creates a new scale matrix.
	 *
	 * @param scale A vector to scale each component by
	 *
	 * @return A new scale {@link Matrix4}
	 */
	public static Matrix4 newScale(Vector3 scale) {
		return new Matrix4(scale.getX(), 0.0f, 0.0f, 0.0f,
						   0.0f, scale.getY(), 0.0f, 0.0f,
						   0.0f, 0.0f, scale.getZ(), 0.0f,
						   0.0f, 0.0f, 0.0f, 1.0f);
	}

	/**
	 * Creates a new scale matrix
	 *
	 * @param scaleX The x-component of the scale
	 * @param scaleY The y-component of the scale
	 * @param scaleZ The z-component of the scale
	 *
	 * @return A new scale {@link Matrix4}
	 */
	public static Matrix4 newScale(float scaleX, float scaleY, float scaleZ) {
		return new Matrix4(scaleX, 0.0f, 0.0f, 0.0f,
						   0.0f, scaleY, 0.0f, 0.0f,
						   0.0f, 0.0f, scaleZ, 0.0f,
						   0.0f, 0.0f, 0.0f, 1.0f);
	}

	/**
	 * Creates a new rotation matrix.
	 *
	 * @param rot The quaternion to rotate by
	 *
	 * @return A new rotation {@link Matrix4}
	 */
	public static Matrix4 newRotation(Quaternion rot) {
		final Quaternion normRot = rot.normalise();
		final float x = normRot.getX();
		final float y = normRot.getY();
		final float z = normRot.getZ();
		final float w = normRot.getW();
		return new Matrix4(1.0f - (2.0f * y * y) - (2.0f * z * z), (2.0f * x * y) - (2.0f * w * z), (2.0f * x * z) + (2.0f * w * y), 0.0f,
						   (2.0f * x * y) + (2.0f * w * z), 1.0f - (2.0f * x * x) - (2.0f * z * z), 2.0f * y * z - 2.0f * w * x, 0.0f,
						   (2.0f * x * z) - (2.0f * w * y), (2.0f * y * z) + (2.0f * x * w), 1.0f - (2.0f * x * x) - (2.0f * y * y), 0.0f,
						   0.0f, 0.0f, 0.0f, 1.0f);
	}

	/**
	 * Creates a new lookAt matrix. The new matrix has normalised local axis and
	 * will be at the position of the eye.
	 *
	 * @param eye The current position
	 * @param at  The position to look at
	 * @param up  The up vector
	 *
	 * @return A new lookAt {@link Matrix4}
	 */
	public static Matrix4 newLookAt(Vector3 eye, Vector3 at, Vector3 up) {
		final Vector3 localZ = at.sub(eye).normalise();
		final Vector3 localX = up.cross(localZ).normalise();
		final Vector3 localY = localZ.cross(localX);
		return new Matrix4(localX.getX(), localX.getY(), localX.getZ(), 0.0f,
						   localY.getX(), localY.getY(), localY.getZ(), 0.0f,
						   -localZ.getX(), -localZ.getY(), -localZ.getZ(), 0.0f,
						   eye.getX(), eye.getY(), eye.getZ(), 1.0f);
	}

	/**
	 * Creates a new perspective matrix.
	 *
	 * @param fov    The field of view
	 * @param aspect The aspect ratio
	 * @param near   The near distance
	 * @param far    The far distance
	 *
	 * @return A new perspective {@link Matrix4}
	 */
	public static Matrix4 newPerspective(float fov, float aspect, float near, float far) {
		final float scale = (float) (1.0f / Math.tan(FastMaths.toRadians(fov * 0.5f)));
		return new Matrix4(scale / aspect, 0.0f, 0.0f, 0.0f,
						   0.0f, scale, 0.0f, 0.0f,
						   0.0f, 0.0f, (far + near) / (near - far), (2 * far * near) / (near - far),
						   0.0f, 0.0f, -1, 0.0f);
	}

	/**
	 * Creates a new orthographic matrix.
	 *
	 * @param left   The left value
	 * @param right  The right value
	 * @param bottom The bottom value
	 * @param top    The top value
	 * @param near   The near distance
	 * @param far    The far distance
	 *
	 * @return A new orthographic {@link Matrix4}
	 */
	public static Matrix4 newOrthographic(float left, float right, float bottom, float top, float near, float far) {
		return new Matrix4(2.0f / (right - left), 0.0f, 0.0f, -(right + left) / (right - left),
						   0.0f, 2.0f / (top - bottom), 0.0f, -(top + bottom) / (top - bottom),
						   0.0f, 0.0f, -2.0f / (far - near), -(far + near) / (far - near),
						   0.0f, 0.0f, 0.0f, 1.0f);
	}

	/**
	 * Internal method used in calculating the matrix inverse.
	 *
	 * @param m00 A value
	 * @param m01 A value
	 * @param m02 A value
	 * @param m10 A value
	 * @param m11 A value
	 * @param m12 A value
	 * @param m20 A value
	 * @param m21 A value
	 * @param m22 A value
	 *
	 * @return A value
	 */
	private static float det3(float m00, float m01, float m02,
							  float m10, float m11, float m12,
							  float m20, float m21, float m22) {
		return m00 * (m11 * m22 - m12 * m21) - m01 * (m10 * m22 - m12 * m20) + m02 * (m10 * m21 - m11 * m20);
	}
}
