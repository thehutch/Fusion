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
package me.thehutch.fusion.api.scene;

import com.flowpowered.math.imaginary.Quaternionf;
import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector4f;

/**
 * @author thehutch
 */
public interface IModel extends ISceneNode {
	public Vector3f getPosition();

	public void setPosition(Vector3f position);

	public Vector4f getScale();

	public void setScale(Vector4f scale);

	public Quaternionf getRotation();

	public void setRotation(Quaternionf rotation);

	public void moveX(float dx);

	public void moveY(float dy);

	public void moveZ(float dz);

	public void move(float dx, float dy, float dz);

	public void scaleX(float scale);

	public void scaleY(float scale);

	public void scaleZ(float scale);

	public void scale(float scale);

	public void scale(float scaleX, float scaleY, float scaleZ);

	public void rotateX(float angle);

	public void rotateY(float angle);

	public void rotateZ(float angle);

	public void rotate(Quaternionf rotation);
}
