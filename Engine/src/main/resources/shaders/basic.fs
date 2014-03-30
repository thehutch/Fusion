#version 140

varying vec2 textureUV;

uniform sampler2D diffuse;

void main()
{
	vec4 colour = texture2D(diffuse, textureUV);
	if (colour.a <= 0)
	{
		discard;
	}
	gl_FragColor = colour;
}