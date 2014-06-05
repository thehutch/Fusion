#version 330

uniform mat4 modelMatrix;
uniform mat3 normalMatrix;
uniform vec3 cameraPos;

// Material settings
uniform sampler2D material;
uniform float materialShininess;

uniform struct Light
{
	vec3 position;
	vec3 colour;
	float attenuation;
} light;

in vec3 position0;
in vec2 texcoord0;
in vec3 normal0;

out vec4 fragColour;

void main()
{
	// Calculate normal in world space
	vec3 normal = normalize(normalMatrix * normal0);

	// Calculate location of fragment in world space
	vec3 surfacePos = (modelMatrix * vec4(position0, 1.0)).xyz;

	// Calculate the colour of the surface
	vec4 surfaceColour = texture(material, texcoord0.xy);

	// Calculate the vector from the surface to the light source
	vec3 surfaceToLight = light.position - surfacePos;
	// Calculate the distance to the light
	float distanceToLight = length(surfaceToLight);
	// Normalise the camera to light vector
	surfaceToLight /= distanceToLight;

	// Calculate the vector from the surface to the camera
	vec3 surfaceToCamera = normalize(cameraPos - surfacePos);

	// Diffuse
	float diffuseCoeff = max(0.0, dot(normal, surfaceToLight));
	vec3 diffuse = diffuseCoeff * surfaceColour.rgb * light.colour;

	// Specular
	float specularCoeff = 0.0;
	if (diffuseCoeff > 0.0)
	{
		specularCoeff = pow(max(0.0, dot(surfaceToCamera, reflect(-surfaceToLight, normal))), materialShininess);
	}
	vec3 specular = specularCoeff * light.colour;

	// Attenutation
	float attenuation = 1.0 / (1.0 + light.attenuation * pow(distanceToLight, 2));

	// Final fragment colour
	fragColour = vec4(attenuation * (diffuse + specular), surfaceColour.a);
}