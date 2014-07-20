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

import java.nio.ByteBuffer;
import me.thehutch.fusion.api.util.Creatable;
import me.thehutch.fusion.engine.filesystem.loaders.ImageLoader.ImageData;
import me.thehutch.fusion.engine.render.texture.CompareFunc;
import me.thehutch.fusion.engine.render.texture.FilterMode;
import me.thehutch.fusion.engine.render.texture.InternalFormat;
import me.thehutch.fusion.engine.render.texture.WrapMode;

/**
 * @author thehutch
 */
public abstract class Texture extends Creatable implements GLVersioned {
	protected int id;

	public final int getId() {
		return id;
	}

	public abstract void bind();

	public abstract void bind(int unit);

	public abstract void unbind();

	public abstract void setWrapMode(WrapMode wrapS, WrapMode wrapT);

	public abstract void setFiltering(FilterMode minFilter, FilterMode magFilter);

	public abstract void setCompareFunc(CompareFunc compareFunc);

	public abstract void setAnisotropicFiltering(float value);

	public final void setPixelData(ImageData data, boolean generateMipmap) {
		setPixelData(data.pixels, data.format, data.width, data.height, generateMipmap);
	}

	public abstract void setPixelData(ByteBuffer pixels, InternalFormat format, int width, int height, boolean generateMipmaps);
}
