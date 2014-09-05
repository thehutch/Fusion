#version 330

in vec2 texcoord0;

out vec4 fragColour;

uniform sampler2D diffuse;

void main()
{
	fragColour = texture(diffuse, texcoord0.xy);
}
