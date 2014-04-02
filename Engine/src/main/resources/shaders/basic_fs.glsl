#version 330

uniform sampler2D diffuse;

in vec2 textureUV;

out vec4 fragColour;

void main()
{
	fragColour = texture2D(diffuse, textureUV);;
}