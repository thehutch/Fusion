/*
 * This file is part of Engine, licensed under the Apache 2.0 License.
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
package me.thehutch.fusion.engine.render.data;

import me.thehutch.fusion.api.maths.Matrix3;
import me.thehutch.fusion.api.maths.Matrix4;
import me.thehutch.fusion.api.maths.Vector2;
import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.maths.Vector4;
import me.thehutch.fusion.engine.render.opengl.Program;

/**
 * A shader uniform which has a name and a value.
 *
 * @author thehutch
 */
public abstract class Uniform {
	protected final String name;

	protected Uniform(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the uniform.
	 *
	 * @return The uniform name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Uploads this uniform to the program.
	 *
	 * @param program The program
	 */
	public abstract void upload(Program program);

	/**
	 * A boolean shader uniform.
	 */
	public static class BooleanUniform extends Uniform {
		private boolean value;

		public BooleanUniform(String name, boolean value) {
			super(name);
			this.value = value;
		}

		public boolean get() {
			return value;
		}

		public void set(boolean value) {
			this.value = value;
		}

		@Override
		public void upload(Program program) {
			program.setUniform(name, value);
		}
	}

	/**
	 * An integer shader uniform.
	 */
	public static class IntegerUniform extends Uniform {
		private int value;

		public IntegerUniform(String name, int value) {
			super(name);
			this.value = value;
		}

		public int get() {
			return value;
		}

		public void set(int value) {
			this.value = value;
		}

		@Override
		public void upload(Program program) {
			program.setUniform(name, value);
		}
	}

	/**
	 * A float shader uniform.
	 */
	public static class FloatUniform extends Uniform {
		private float value;

		public FloatUniform(String name, float value) {
			super(name);
			this.value = value;
		}

		public float get() {
			return value;
		}

		public void set(float value) {
			this.value = value;
		}

		@Override
		public void upload(Program program) {
			program.setUniform(name, value);
		}
	}

	/**
	 * A Vector2 shader uniform.
	 */
	public static class Vector2Uniform extends Uniform {
		private Vector2 value;

		public Vector2Uniform(String name, Vector2 value) {
			super(name);
			this.value = value;
		}

		public Vector2 get() {
			return value;
		}

		public void set(Vector2 value) {
			this.value = value;
		}

		@Override
		public void upload(Program program) {
			program.setUniform(name, value);
		}
	}

	/**
	 * A Vector3 shader uniform.
	 */
	public static class Vector3Uniform extends Uniform {
		private Vector3 value;

		public Vector3Uniform(String name, Vector3 value) {
			super(name);
			this.value = value;
		}

		public Vector3 get() {
			return value;
		}

		public void set(Vector3 value) {
			this.value = value;
		}

		@Override
		public void upload(Program program) {
			program.setUniform(name, value);
		}
	}

	/**
	 * A Vector4 shader uniform.
	 */
	public static class Vector4Uniform extends Uniform {
		private Vector4 value;

		public Vector4Uniform(String name, Vector4 value) {
			super(name);
			this.value = value;
		}

		public Vector4 get() {
			return value;
		}

		public void set(Vector4 value) {
			this.value = value;
		}

		@Override
		public void upload(Program program) {
			program.setUniform(name, value);
		}
	}

	/**
	 * A Matrix3 shader uniform.
	 */
	public static class Matrix3Uniform extends Uniform {
		private Matrix3 value;

		public Matrix3Uniform(String name, Matrix3 value) {
			super(name);
			this.value = value;
		}

		public Matrix3 get() {
			return value;
		}

		public void set(Matrix3 value) {
			this.value = value;
		}

		@Override
		public void upload(Program program) {
			program.setUniform(name, value);
		}
	}

	/**
	 * A Matrix4 shader uniform.
	 */
	public static class Matrix4Uniform extends Uniform {
		private Matrix4 value;

		public Matrix4Uniform(String name, Matrix4 value) {
			super(name);
			this.value = value;
		}

		public Matrix4 get() {
			return value;
		}

		public void set(Matrix4 value) {
			this.value = value;
		}

		@Override
		public void upload(Program program) {
			program.setUniform(name, value);
		}
	}
}
