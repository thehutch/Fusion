#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texcoord;
layout(location = 2) in vec3 normal;

uniform mat4 cameraMatrix;
uniform mat4 modelMatrix;

out vec3 position0;
out vec2 texcoord0;
out vec3 normal0;

void main()
{
	// Forward the data to the fragment shader
	position0 = position;
	texcoord0 = texcoord;
	normal0 = normal;

	// Apply transformation to the model
	gl_Position = cameraMatrix * modelMatrix * vec4(position, 1.0);
}
