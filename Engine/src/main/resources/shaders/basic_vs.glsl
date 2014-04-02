#version 330

layout(location = 0) in vec3 vPosition;
layout(location = 1) in vec2 vTextureUV;

uniform mat4 transform;

out vec2 textureUV;

void main()
{
	gl_Position = transform * vec4(vPosition, 1);
	textureUV = vTextureUV;
}