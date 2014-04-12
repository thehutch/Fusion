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
package me.thehutch.fusion.api.maths;

/**
 * @author thehutch
 */
public class Matrix3 {
	public static final Matrix3 IDENTITY = new Matrix3(1, 0, 0,
													   0, 1, 0,
													   0, 0, 1);
	private final float m00, m01, m02;
	private final float m10, m11, m12;
	private final float m20, m21, m22;

	public Matrix3(Matrix3 m) {
		this(m.m00, m.m01, m.m02,
			 m.m10, m.m11, m.m12,
			 m.m20, m.m21, m.m22);
	}

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

	public Matrix3 add(Matrix3 m) {
		return new Matrix3(m00 + m.m00, m01 + m.m01, m02 + m.m02,
						   m10 + m.m10, m11 + m.m11, m12 + m.m12,
						   m20 + m.m20, m21 + m.m21, m22 + m.m22);
	}

	public Matrix3 sub(Matrix3 m) {
		return new Matrix3(m00 - m.m00, m01 - m.m01, m02 - m.m02,
						   m10 - m.m10, m11 - m.m11, m12 - m.m12,
						   m20 - m.m20, m21 - m.m21, m22 - m.m22);
	}

	public Matrix3 mul(float a) {
		return new Matrix3(m00 * a, m01 * a, m02 * a,
						   m10 * a, m11 * a, m12 * a,
						   m20 * a, m21 * a, m22 * a);
	}

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

	public Matrix3 div(float a) {
		return new Matrix3(m00 / a, m01 / a, m02 / a,
						   m10 / a, m11 / a, m12 / a,
						   m20 / a, m21 / a, m22 / a);
	}

	public Matrix3 div(Matrix3 m) {
		return mul(m.invert());
	}

	public Matrix3 translate(Vector2 v) {
		return translate(v.getX(), v.getY());
	}

	public Matrix3 translate(float x, float y) {
		return createTranslation(x, y).mul(this);
	}

	public Matrix3 scale(float scale) {
		return scale(scale, scale, scale);
	}

	public Matrix3 scale(Vector3 v) {
		return scale(v.getX(), v.getY(), v.getZ());
	}

	public Matrix3 scale(float x, float y, float z) {
		return createScale(x, y, z).mul(this);
	}

	public Matrix3 rotate(Quaternion rot) {
		return createRotation(rot).mul(this);
	}

	public Vector3 transform(Vector3 v) {
		return transform(v.getX(), v.getY(), v.getZ());
	}

	public Vector3 transform(float x, float y, float z) {
		return new Vector3(m00 * x + m01 * y + m02 * z,
						   m10 * x + m11 * y + m12 * z,
						   m20 * x + m21 * y + m22 * z);
	}

	public Matrix3 transpose() {
		return new Matrix3(m00, m10, m20,
						   m01, m11, m21,
						   m02, m12, m22);
	}

	public float trace() {
		return m00 + m11 + m22;
	}

	public float determinant() {
		return m00 * ((m11 * m22) - (m12 * m21))
			   - m01 * ((m10 * m22) - (m12 * m20))
			   + m02 * ((m10 * m21) - (m11 * m20));
	}

	public Matrix3 invert() {
		final float det = determinant();
		if (det == 0.0f) {
			return null;
		}
		return new Matrix3((m11 * m22 - m21 * m12) / det, -(m01 * m22 - m21 * m02) / det, (m01 * m12 - m02 * m11) / det,
						   -(m10 * m22 - m20 * m12) / det, (m00 * m22 - m20 * m02) / det, -(m00 * m12 - m10 * m02) / det,
						   (m10 * m21 - m20 * m11) / det, -(m00 * m21 - m20 * m01) / det, (m00 * m11 - m01 * m10) / det);
	}

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

	public Matrix2 toMatrix2() {
		return new Matrix2(m00, m01,
						   m10, m11);
	}

	@Override
	public String toString() {
		return m00 + " " + m01 + " " + m02 + "\n"
			   + m10 + " " + m11 + " " + m12 + "\n"
			   + m20 + " " + m21 + " " + m22 + "\n";
	}

	public static Matrix3 createScale(float scale) {
		return createScale(scale, scale, scale);
	}

	public static Matrix3 createScale(Vector3 v) {
		return createScale(v.getX(), v.getY(), v.getZ());
	}

	public static Matrix3 createScale(float x, float y, float z) {
		return new Matrix3(x, 0, 0,
						   0, y, 0,
						   0, 0, z);
	}

	public static Matrix3 createTranslation(Vector2 v) {
		return createTranslation(v.getX(), v.getY());
	}

	public static Matrix3 createTranslation(float x, float y) {
		return new Matrix3(1, 0, x,
						   0, 1, y,
						   0, 0, 1);
	}

	public static Matrix3 createRotation(Quaternion rot) {
		rot = rot.normalise();
		return new Matrix3(1 - 2 * rot.getY() * rot.getY() - 2 * rot.getZ() * rot.getZ(),
						   2 * rot.getX() * rot.getY() - 2 * rot.getW() * rot.getZ(),
						   2 * rot.getX() * rot.getZ() + 2 * rot.getW() * rot.getY(),
						   2 * rot.getX() * rot.getY() + 2 * rot.getW() * rot.getZ(),
						   1 - 2 * rot.getX() * rot.getX() - 2 * rot.getZ() * rot.getZ(),
						   2 * rot.getY() * rot.getZ() - 2 * rot.getW() * rot.getX(),
						   2 * rot.getX() * rot.getZ() - 2 * rot.getW() * rot.getY(),
						   2 * rot.getY() * rot.getZ() + 2 * rot.getX() * rot.getW(),
						   1 - 2 * rot.getX() * rot.getX() - 2 * rot.getY() * rot.getY());
	}
}
