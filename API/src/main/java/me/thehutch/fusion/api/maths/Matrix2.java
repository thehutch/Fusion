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
public class Matrix2 {
	public static final Matrix2 IDENTITY = new Matrix2(1, 0,
													   0, 1);
	private final float m00, m01;
	private final float m10, m11;

	public Matrix2(Matrix2 m) {
		this(m.m00, m.m01,
			 m.m10, m.m11);
	}

	public Matrix2(float m00, float m01,
				   float m10, float m11) {
		this.m00 = m00;
		this.m01 = m01;
		this.m10 = m10;
		this.m11 = m11;
	}

	public Matrix2 add(Matrix2 m) {
		return new Matrix2(m00 + m.m00, m01 + m.m01,
						   m10 + m.m10, m11 + m.m11);
	}

	public Matrix2 sub(Matrix2 m) {
		return new Matrix2(m00 - m.m00, m01 - m.m01,
						   m10 - m.m10, m11 - m.m11);
	}

	public Matrix2 mul(float a) {
		return new Matrix2(m00 * a, m01 * a,
						   m10 * a, m11 * a);
	}

	public Matrix2 mul(Matrix2 m) {
		return new Matrix2(m00 * m.m00 + m01 * m.m10,
						   m00 * m.m01 + m01 * m.m11,
						   m10 * m.m00 + m11 * m.m10,
						   m10 * m.m01 + m11 * m.m11);
	}

	public Matrix2 div(float a) {
		return new Matrix2(m00 / a, m01 / a,
						   m10 / a, m11 / a);
	}

	public Matrix2 div(Matrix2 m) {
		return mul(m.invert());
	}

	public Matrix2 translate(float x) {
		return createTranslation(x).mul(this);
	}

	public Matrix2 scale(float scale) {
		return scale(scale, scale);
	}

	public Matrix2 scale(Vector2 v) {
		return scale(v.getX(), v.getY());
	}

	public Matrix2 scale(float x, float y) {
		return createScale(x, y).mul(this);
	}

	public Vector2 transform(Vector2 v) {
		return transform(v.getX(), v.getY());
	}

	public Vector2 transform(float x, float y) {
		return new Vector2(m00 * x + m01 * y,
						   m10 * x + m11 * y);
	}

	public Matrix2 transpose() {
		return new Matrix2(m00, m10,
						   m01, m11);
	}

	public float trace() {
		return m00 + m11;
	}

	public float determinant() {
		return m00 * m11 - m01 * m10;
	}

	public Matrix2 invert() {
		final float det = determinant();
		if (det == 0.0f) {
			return null;
		}
		return new Matrix2(m11 / det, -m01 / det,
						   -m10 / det, m00 / det);
	}

	public float[] toArray(boolean columnMajor) {
		if (columnMajor) {
			return new float[] {
				m00, m10,
				m01, m11
			};
		} else {
			return new float[] {
				m00, m01,
				m10, m11
			};
		}
	}

	@Override
	public String toString() {
		return m00 + " " + m01 + "\n"
			   + m10 + " " + m11 + "\n";
	}

	public static Matrix2 createScale(float scale) {
		return createScale(scale, scale);
	}

	public static Matrix2 createScale(Vector2 v) {
		return createScale(v.getX(), v.getY());
	}

	public static Matrix2 createScale(float x, float y) {
		return new Matrix2(x, 0,
						   0, y);
	}

	public static Matrix2 createTranslation(float x) {
		return new Matrix2(1, x,
						   0, 1);
	}
}
