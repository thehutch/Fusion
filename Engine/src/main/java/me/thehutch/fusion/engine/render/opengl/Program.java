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
package me.thehutch.fusion.engine.render.opengl;

import me.thehutch.fusion.api.maths.Matrix3;
import me.thehutch.fusion.api.maths.Matrix4;
import me.thehutch.fusion.api.maths.Vector2;
import me.thehutch.fusion.api.maths.Vector3;
import me.thehutch.fusion.api.maths.Vector4;
import me.thehutch.fusion.api.util.Creatable;
import me.thehutch.fusion.engine.render.data.Uniform;
import me.thehutch.fusion.engine.render.data.UniformHolder;

public abstract class Program extends Creatable implements GLVersioned {
	protected int id;

	public abstract void bind();

	public abstract void unbind();

	public abstract void attachShader(CharSequence source, int type);

	public abstract void link();

	public abstract void bindSampler(int unit);

	public abstract void setUniform(String name, boolean b);

	public abstract void setUniform(String name, int i);

	public abstract void setUniform(String name, float f);

	public abstract void setUniform(String name, Vector2 vec);

	public abstract void setUniform(String name, Vector3 vec);

	public abstract void setUniform(String name, Vector4 vec);

	public abstract void setUniform(String name, Matrix3 matrix);

	public abstract void setUniform(String name, Matrix3[] matrix);

	public abstract void setUniform(String name, Matrix4 matrix);

	public abstract void setUniform(String name, Matrix4[] matrix);

	public final int getId() {
		return id;
	}

	public final void upload(Uniform uniform) {
		uniform.upload(this);
	}

	public final void upload(UniformHolder holder) {
		holder.forEach((Uniform uniform) -> {
			uniform.upload(this);
		});
	}
}
