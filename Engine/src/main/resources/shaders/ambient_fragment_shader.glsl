#version 330

in vec2 texcoord0;

out vec4 fragColour;

uniform sampler2D material;
uniform float ambient;

void main()
{
	vec4 textureColour = texture(material, texcoord0.xy);
	fragColour = vec4(textureColour.rgb * ambient, textureColour.a);
}