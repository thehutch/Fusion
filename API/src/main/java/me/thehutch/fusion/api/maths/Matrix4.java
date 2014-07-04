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
public class Matrix4 {
	public static final Matrix4 IDENTITY = new Matrix4(1, 0, 0, 0,
													   0, 1, 0, 0,
													   0, 0, 1, 0,
													   0, 0, 0, 1);
	private final float m00, m01, m02, m03;
	private final float m10, m11, m12, m13;
	private final float m20, m21, m22, m23;
	private final float m30, m31, m32, m33;

	public Matrix4(Matrix4 m) {
		this(m.m00, m.m01, m.m02, m.m03,
			 m.m10, m.m11, m.m12, m.m13,
			 m.m20, m.m21, m.m22, m.m23,
			 m.m30, m.m31, m.m32, m.m33);
	}

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

	public Matrix4 add(Matrix4 m) {
		return new Matrix4(m00 + m.m00, m01 + m.m01, m02 + m.m02, m03 + m.m03,
						   m10 + m.m10, m11 + m.m11, m12 + m.m12, m13 + m.m13,
						   m20 + m.m20, m21 + m.m21, m22 + m.m22, m23 + m.m23,
						   m30 + m.m30, m31 + m.m31, m32 + m.m32, m33 + m.m33);
	}

	public Matrix4 sub(Matrix4 m) {
		return new Matrix4(m00 - m.m00, m01 - m.m01, m02 - m.m02, m03 - m.m03,
						   m10 - m.m10, m11 - m.m11, m12 - m.m12, m13 - m.m13,
						   m20 - m.m20, m21 - m.m21, m22 - m.m22, m23 - m.m23,
						   m30 - m.m30, m31 - m.m31, m32 - m.m32, m33 - m.m33);
	}

	public Matrix4 mul(float a) {
		return new Matrix4(m00 * a, m01 * a, m02 * a, m03 * a,
						   m10 * a, m11 * a, m12 * a, m13 * a,
						   m20 * a, m21 * a, m22 * a, m23 * a,
						   m30 * a, m31 * a, m32 * a, m33 * a);
	}

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

	public Matrix4 div(float a) {
		return new Matrix4(m00 / a, m01 / a, m02 / a, m03 / a,
						   m10 / a, m11 / a, m12 / a, m13 / a,
						   m20 / a, m21 / a, m22 / a, m23 / a,
						   m30 / a, m31 / a, m32 / a, m33 / a);
	}

	public Matrix4 div(Matrix4 m) {
		return mul(m.invert());
	}

	public Matrix4 translate(Vector3 v) {
		return translate(v.getX(), v.getY(), v.getZ());
	}

	public Matrix4 translate(float x, float y, float z) {
		return createTranslation(x, y, z).mul(this);
	}

	public Matrix4 scale(float scale) {
		return scale(scale, scale, scale, scale);
	}

	public Matrix4 scale(Vector4 v) {
		return scale(v.getX(), v.getY(), v.getZ(), v.getW());
	}

	public Matrix4 scale(float x, float y, float z, float w) {
		return createScale(x, y, z, w).mul(this);
	}

	public Matrix4 rotate(Quaternion rot) {
		return createRotation(rot).mul(this);
	}

	public Vector4 transform(Vector4 v) {
		return transform(v.getX(), v.getY(), v.getZ(), v.getW());
	}

	public Vector4 transform(float x, float y, float z, float w) {
		return new Vector4(m00 * x + m01 * y + m02 * z + m03 * w,
						   m10 * x + m11 * y + m12 * z + m13 * w,
						   m20 * x + m21 * y + m22 * z + m23 * w,
						   m30 * x + m31 * y + m32 * z + m33 * w);
	}

	public Matrix4 transpose() {
		return new Matrix4(m00, m10, m20, m30,
						   m01, m11, m21, m31,
						   m02, m12, m22, m32,
						   m03, m13, m23, m33);
	}

	public float trace() {
		return m00 + m11 + m22 + m33;
	}

	public float determinant() {
		return m00 * (m11 * m22 * m33 + m21 * m32 * m13 + m31 * m12 * m23 - m31 * m22 * m13 - m11 * m32 * m23 - m21 * m12 * m33)
			   - m10 * (m01 * m22 * m33 + m21 * m32 * m03 + m31 * m02 * m23 - m31 * m22 * m03 - m01 * m32 * m23 - m21 * m02 * m33)
			   + m20 * (m01 * m12 * m33 + m11 * m32 * m03 + m31 * m02 * m13 - m31 * m12 * m03 - m01 * m32 * m13 - m11 * m02 * m33)
			   - m30 * (m01 * m12 * m23 + m11 * m22 * m03 + m21 * m02 * m13 - m21 * m12 * m03 - m01 * m22 * m13 - m11 * m02 * m23);
	}

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

	public Matrix2 toMatrix2() {
		return new Matrix2(m00, m01,
						   m10, m11);
	}

	public Matrix3 toMatrix3() {
		return new Matrix3(m00, m01, m02,
						   m10, m11, m12,
						   m20, m21, m22);
	}

	@Override
	public String toString() {
		return m00 + " " + m01 + " " + m02 + " " + m03 + "\n"
			   + m10 + " " + m11 + " " + m12 + " " + m13 + "\n"
			   + m20 + " " + m21 + " " + m22 + " " + m23 + "\n"
			   + m30 + " " + m31 + " " + m32 + " " + m33 + "\n";
	}

	public static Matrix4 createScale(float scale) {
		return createScale(scale, scale, scale);
	}

	public static Matrix4 createScale(Vector3 v) {
		return createScale(v.getX(), v.getY(), v.getZ());
	}

	public static Matrix4 createScale(Vector4 v) {
		return createScale(v.getX(), v.getY(), v.getZ(), v.getW());
	}

	public static Matrix4 createScale(float x, float y, float z) {
		return new Matrix4(x, 0, 0, 0,
						   0, y, 0, 0,
						   0, 0, z, 0,
						   0, 0, 0, 1);
	}

	public static Matrix4 createScale(float x, float y, float z, float w) {
		return new Matrix4(x, 0, 0, 0,
						   0, y, 0, 0,
						   0, 0, z, 0,
						   0, 0, 0, w);
	}

	public static Matrix4 createTranslation(Vector3 v) {
		return createTranslation(v.getX(), v.getY(), v.getZ());
	}

	public static Matrix4 createTranslation(float x, float y, float z) {
		return new Matrix4(1, 0, 0, x,
						   0, 1, 0, y,
						   0, 0, 1, z,
						   0, 0, 0, 1);
	}

	public static Matrix4 createRotation(Quaternion rot) {
		rot = rot.normalise();
		return new Matrix4(1 - 2 * rot.getY() * rot.getY() - 2 * rot.getZ() * rot.getZ(),
						   2 * rot.getX() * rot.getY() - 2 * rot.getW() * rot.getZ(),
						   2 * rot.getX() * rot.getZ() + 2 * rot.getW() * rot.getY(), 0,
						   2 * rot.getX() * rot.getY() + 2 * rot.getW() * rot.getZ(),
						   1 - 2 * rot.getX() * rot.getX() - 2 * rot.getZ() * rot.getZ(),
						   2 * rot.getY() * rot.getZ() - 2 * rot.getW() * rot.getX(), 0,
						   2 * rot.getX() * rot.getZ() - 2 * rot.getW() * rot.getY(),
						   2 * rot.getY() * rot.getZ() + 2 * rot.getX() * rot.getW(),
						   1 - 2 * rot.getX() * rot.getX() - 2 * rot.getY() * rot.getY(), 0,
						   0, 0, 0, 1);
	}

	public static Matrix4 createLookAt(Vector3 eye, Vector3 at, Vector3 up) {
		final Vector3 f = at.sub(eye).normalise();
		final Vector3 s = f.cross(up).normalise();
		final Vector3 u = s.cross(f);
		final Matrix4 mat = new Matrix4(s.getX(), s.getY(), s.getZ(), 0,
										u.getX(), u.getY(), u.getZ(), 0,
										-f.getX(), -f.getY(), -f.getZ(), 0,
										0, 0, 0, 1);
		return mat.translate(eye.negate());
	}

	public static Matrix4 createPerspective(float fov, float aspect, float near, float far) {
		final float scale = (float) (1.0f / Math.tan(MathsHelper.toRadians(fov * 0.5f)));
		return new Matrix4(scale / aspect, 0, 0, 0,
						   0, scale, 0, 0,
						   0, 0, (far + near) / (near - far), 2 * far * near / (near - far),
						   0, 0, -1, 0);
	}

	public static Matrix4 createOrthographic(float left, float right, float bottom, float top, float near, float far) {
		return new Matrix4(2.0f / (right - left), 0, 0, -(right + left) / (right - left),
						   0, 2.0f / (top - bottom), 0, -(top + bottom) / (top - bottom),
						   0, 0, -2.0f / (far - near), -(far + near) / (far - near),
						   0, 0, 0, 1.0f);
	}

	private static float det3(float m00, float m01, float m02,
							  float m10, float m11, float m12,
							  float m20, float m21, float m22) {
		return m00 * (m11 * m22 - m12 * m21) - m01 * (m10 * m22 - m12 * m20) + m02 * (m10 * m21 - m11 * m20);
	}
}
