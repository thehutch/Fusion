#version 140

attribute vec3 vPosition;
attribute vec2 vTextureUV;

varying vec2 textureUV;

uniform mat4 transform;

void main()
{
	gl_Position = transform * vec4(vPosition, 1);
	textureUV = vTextureUV;
}