#version 330 core

in vec3 position0;
in vec2 texcoord0;
in vec3 normal0;

out vec4 fragColour;

uniform mat4 modelMatrix;
uniform mat3 normalMatrix;

// Material settings
uniform sampler2D material;

uniform struct Light
{
	vec3 direction;
	vec3 colour;
} light;

void main()
{
	// Calculate the normal in world space
	vec3 normal = normalize(normalMatrix * normal0);

	// Calculate location of fragment in world space
	vec3 surfacePos = (modelMatrix * vec4(position0, 1.0)).xyz;

	// Calculate the colour of the surface
	vec4 surfaceColour = texture2D(material, texcoord0.xy);

	// Diffuse
	float diffuseCoeff = max(0.0, dot(normal, light.direction));
	vec3 diffuse = diffuseCoeff * surfaceColour.rgb * light.colour.rgb;

	fragColour = vec4(diffuse, surfaceColour.a);
}
