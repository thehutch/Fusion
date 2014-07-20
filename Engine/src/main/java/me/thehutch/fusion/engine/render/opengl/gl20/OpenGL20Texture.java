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
package me.thehutch.fusion.engine.render.opengl.gl20;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL14.GL_COMPARE_R_TO_TEXTURE;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_COMPARE_FUNC;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_COMPARE_MODE;

import java.nio.ByteBuffer;
import me.thehutch.fusion.api.maths.MathsHelper;
import me.thehutch.fusion.api.util.GLVersion;
import me.thehutch.fusion.engine.render.opengl.Texture;
import me.thehutch.fusion.engine.render.texture.CompareFunc;
import me.thehutch.fusion.engine.render.texture.FilterMode;
import me.thehutch.fusion.engine.render.texture.InternalFormat;
import me.thehutch.fusion.engine.render.texture.WrapMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

/**
 * @author thehutch
 */
public class OpenGL20Texture extends Texture {

	@Override
	public void create() {
		ensureNotCreated("Texture is already created.");

		// Generate the texture
		this.id = GL11.glGenTextures();

		// Create the texture
		super.create();
	}

	@Override
	public void dispose() {
		ensureCreated("Texture must be created to dispose.");
		// Delete the texture
		GL11.glDeleteTextures(id);
		// Set the texture as not created
		super.dispose();
	}

	@Override
	public void bind() {
		ensureCreated("Texture must be created to bind.");
		// Bind the texture to the current texture unit
		GL11.glBindTexture(GL_TEXTURE_2D, id);
	}

	@Override
	public void bind(int unit) {
		ensureCreated("Texture must be created to bind.");
		// Active the texture unit
		if (unit >= 0) {
			GL13.glActiveTexture(GL_TEXTURE0 + unit);
		}
		GL11.glBindTexture(GL_TEXTURE_2D, id);
	}

	@Override
	public void unbind() {
		GL11.glBindTexture(GL_TEXTURE_2D, 0);
	}

	@Override
	public void setWrapMode(WrapMode wrapS, WrapMode wrapT) {
		ensureCreated("Texture must be created to set wrap mode.");
		// Set the texture wrap parameters
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS.getGLConstant());
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT.getGLConstant());
	}

	@Override
	public void setFiltering(FilterMode minFilter, FilterMode magFilter) {
		ensureCreated("Texture must be created to set filtering.");
		// Set the texture filter parameters
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter.getGLConstant());
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter.getGLConstant());
	}

	@Override
	public void setCompareFunc(CompareFunc compareFunc) {
		ensureCreated("Texture must be created to set the compare function.");
		// Set the texture compare function
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, compareFunc.getGLConstant());
		GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_R_TO_TEXTURE);
	}

	@Override
	public void setAnisotropicFiltering(float value) {
		ensureCreated("Texture must be created to set the anisotropic filtering.");
		// Check if the driver is capable of anisotropic filtering
		if (value > 0.0f && GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
			final float maxFiltering = GL11.glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
			GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, MathsHelper.clamp(value, 0.0f, maxFiltering));
		}
	}

	@Override
	public void setPixelData(ByteBuffer pixels, InternalFormat format, int width, int height, boolean generateMipmaps) {
		ensureCreated("Texture must be created to set pixel data.");
		// Check if mipmaps should be generated for this texture
		if (generateMipmaps) {
			GLU.gluBuild2DMipmaps(GL_TEXTURE_2D, format.getFormat().getNumComponents(), width, height, format.getFormat().getGLConstant(), format.getDataType().getGLConstant(), pixels);
		} else {
			GL11.glTexImage2D(GL_TEXTURE_2D, 0, format.getGLConstant(), width, height, 0, format.getFormat().getGLConstant(), format.getDataType().getGLConstant(), pixels);
		}
	}

	@Override
	public GLVersion getGLVersion() {
		return GLVersion.GL20;
	}
}
