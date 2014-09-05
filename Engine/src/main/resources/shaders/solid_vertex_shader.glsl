#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texcoord;
layout(location = 2) in vec3 normal;

out vec2 texcoord0;

uniform mat4 cameraMatrix;
uniform mat4 modelMatrix;

void main()
{
	texcoord0 = texcoord;
	gl_Position = cameraMatrix * modelMatrix * vec4(position, 1.0);
}