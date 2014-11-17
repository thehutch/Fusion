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
public final class Quaternion {
	/**
	 * The identity rotation.
	 */
	public static final Quaternion IDENTITY = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);

	private final float mX;
	private final float mY;
	private final float mZ;
	private final float mW;

	/**
	 * Default constructor for {@link Quaternion}.
	 *
	 * @param x The mX-component
	 * @param y The mY-component
	 * @param z The mZ-component
	 * @param w The mW-component
	 */
	public Quaternion(float x, float y, float z, float w) {
		mX = x;
		mY = y;
		mZ = z;
		mW = w;
	}

	/**
	 * @return The mX-component
	 */
	public float getX() {
		return mX;
	}

	/**
	 * @return The mY-component
	 */
	public float getY() {
		return mY;
	}

	/**
	 * @return The mZ-component
	 */
	public float getZ() {
		return mZ;
	}

	/**
	 * @return The mW-component
	 */
	public float getW() {
		return mW;
	}

	/**
	 * Calculates the dot product of this quaternion with the provided quaternion.
	 * <p>
	 * @param q The quaternion to dot with
	 * <p>
	 * @return The resultant dot product
	 */
	public float dot(Quaternion q) {
		return mX * q.mX + mY * q.mY + mZ * q.mZ + mW * q.mW;
	}

	/**
	 * Creates a new normalised {@link Quaternion}.
	 *
	 * @return A new normalised {@link Quaternion}
	 */
	public Quaternion normalise() {
		final float lenSq = mX * mX + mY * mY + mZ * mZ + mW * mW;
		if (lenSq == 0.0f || lenSq == 1.0f) {
			return this;
		}
		final float invLength = 1.0f / FastMaths.fastSqrt(lenSq);
		return new Quaternion(mX * invLength, mY * invLength, mZ * invLength, mW * invLength);
	}

	/**
	 * Creates a new conjugated {@link Quaternion}.
	 *
	 * @return A new conjugated {@link Quaternion}
	 */
	public Quaternion conjugate() {
		return new Quaternion(-mX, -mY, -mZ, mW);
	}

	/**
	 * Creates a new inverted {@link Quaternion}.
	 *
	 * @return A new inverted {@link Quaternion}
	 */
	public Quaternion invert() {
		final float lenSq = mX * mX + mY * mY + mZ * mZ + mW * mW;
		if (lenSq > 0.0f) {
			final float invLenSq = 1.0f / lenSq;
			return new Quaternion(-mX * invLenSq, -mY * invLenSq, -mZ * invLenSq, mW * invLenSq);
		}
		return null;
	}

	/**
	 * Adds two quaternions together.
	 *
	 * @param q The quaternion to add to this quaternion
	 *
	 * @return A new {@link Quaternion}
	 */
	public Quaternion add(Quaternion q) {
		return new Quaternion(mX + q.mX, mY + q.mY, mZ + q.mZ, mW + q.mW);
	}

	/**
	 * Subtracts two quaternions from each other.
	 *
	 * @param q The quaternion to subtract from this quaternion
	 *
	 * @return A new {@link Quaternion}
	 */
	public Quaternion sub(Quaternion q) {
		return new Quaternion(mX - q.mX, mY - q.mY, mZ - q.mZ, mW - q.mW);
	}

	/**
	 * Multiples this {@link Quaternion} by a scalar value.
	 *
	 * @param scalar The scalar to multiply by
	 *
	 * @return A new {@link Quaternion}
	 */
	public Quaternion mul(float scalar) {
		return new Quaternion(mX * scalar, mY * scalar, mZ * scalar, mW * scalar);
	}

	/**
	 * Multiples this {@link Quaternion} by a {@link Vector3}.
	 *
	 * @param vec The vector to multiply by
	 *
	 * @return A new {@link Quaternion}
	 */
	public Quaternion mul(Vector3 vec) {
		return new Quaternion(mW * vec.getX() + mY * vec.getZ() - mZ * vec.getY(),
							  mW * vec.getY() + mZ * vec.getX() - mX * vec.getZ(),
							  mW * vec.getZ() + mX * vec.getY() - mY * vec.getX(),
							  -mX * vec.getX() - mY * vec.getY() - mZ * vec.getZ());
	}

	/**
	 * Multiples two quaternions together.
	 *
	 * @param q The {@link Quaternion} to multiply with
	 *
	 * @return A new {@link Quaternion}
	 */
	public Quaternion mul(Quaternion q) {
		return new Quaternion(mW * q.mX + mX * q.mW + mY * q.mZ - mZ * q.mY,
							  mW * q.mY + mY * q.mW + mZ * q.mX - mX * q.mZ,
							  mW * q.mZ + mZ * q.mW + mX * q.mY - mY * q.mX,
							  mW * q.mW - mX * q.mX - mY * q.mY - mZ * q.mZ);
	}

	/**
	 * Divides this {@link Quaternion} by a scalar.
	 *
	 * @param scalar The scalar value to divide by
	 *
	 * @return A new {@link Quaternion}
	 */
	public Quaternion div(float scalar) {
		final float inverseScalar = 1.0f / scalar;
		return new Quaternion(mX * inverseScalar, mY * inverseScalar, mZ * inverseScalar, mW * inverseScalar);
	}

	/**
	 * Returns the direction this quaternion is facing.
	 * The returned {@link Vector3} is not normalised.
	 *
	 * @return A new direction {@link Quaternion}
	 */
	public Vector3 getDirection() {
		final Matrix3 asRotation = Matrix3.newRotation(this);
		return asRotation.mul(Vector3.UNIT_Z);
	}

	/**
	 * Converts this quaternion rotation into a new {@link Matrix4} rotation matrix
	 *
	 * @return A new rotation {@link Matrix4}
	 */
	public Matrix4 toRotationMatrix() {
		return Matrix4.newRotation(this);
	}

	/**
	 * Creates a new rotation needed to rotate {@code from} to {@code to}.
	 *
	 * @param from The starting vector
	 * @param to   The end vector
	 *
	 * @return A new {@link Quaternion} rotation
	 */
	public static Quaternion fromRotationTo(Vector3 from, Vector3 to) {
		return Quaternion.fromAxisAngleRad(from.cross(to), (float) Math.acos(from.dot(to) / (from.length() * to.length())));
	}

	/**
	 * Creates a new {@link Quaternion} using the given axis and angle in degrees.
	 *
	 * @param axis  The axis this rotation rotates around
	 * @param angle The angle in degrees
	 *
	 * @return A new {@link Quaternion}
	 */
	public static Quaternion fromAxisAngleDeg(Vector3 axis, float angle) {
		return fromAxisAngleRad(axis.getX(), axis.getY(), axis.getZ(), FastMaths.toRadians(angle));
	}

	/**
	 * Creates a new {@link Quaternion} using the given axis and angle in degrees.
	 *
	 * @param x     The mX-component of the axis
	 * @param y     The mY-component of the axis
	 * @param z     The mZ-component of the axis
	 * @param angle The angle in degrees
	 *
	 * @return A new {@link Quaternion}
	 */
	public static Quaternion fromAxisAngleDeg(float x, float y, float z, float angle) {
		return fromAxisAngleRad(x, y, z, FastMaths.toRadians(angle));
	}

	/**
	 * Creates a new {@link Quaternion} using the given axis and angle in radians.
	 *
	 * @param axis  The axis this rotation rotates around
	 * @param angle The angle in radians
	 *
	 * @return A new {@link Quaternion}
	 */
	public static Quaternion fromAxisAngleRad(Vector3 axis, float angle) {
		return fromAxisAngleRad(axis.getX(), axis.getY(), axis.getZ(), angle);
	}

	/**
	 * Creates a new {@link Quaternion} using the given axis and angle in radians.
	 *
	 * @param x     The mX-component of the axis
	 * @param y     The mY-component of the axis
	 * @param z     The mZ-component of the axis
	 * @param angle The angle in radians
	 *
	 * @return A new {@link Quaternion}
	 */
	public static Quaternion fromAxisAngleRad(float x, float y, float z, float angle) {
		final float halfAngle = angle / 2.0f;
		final float q = (float) (Math.sin(halfAngle) / FastMaths.fastSqrt(x * x + y * y + z * z));
		return new Quaternion(x * q, y * q, z * q, (float) Math.cos(halfAngle));
	}
}
