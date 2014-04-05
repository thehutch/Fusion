#version 330

uniform mat4 model;
uniform vec3 cameraPos;

// Material settings
uniform sampler2D material;
uniform float materialShininess;
uniform vec3 materialSpecularColour;

uniform struct Light
{
	vec3 position;
	vec3 colour;
	float attenuation;
	float ambientCoeff;
} light;

in vec3 position0;
in vec2 texcoord0;
in vec3 normal0;

out vec4 fragColour;

void main()
{
	// Calculate normal in word space
	vec3 normal = normalize(transpose(inverse(mat3(model))) * normal0);

	// Calculate location of fragment in world space
	vec3 surfacePos = vec3(model * vec4(position0, 1.0));

	// Calculate the colour of the surface
	vec4 surfaceColour = texture(material, texcoord0);

	// Calculate the vector from the surface to the light source
	vec3 surfaceToLight = normalize(light.position - surfacePos);

	// Calculate the vector from the surface to the camera
	vec3 surfaceToCamera = normalize(cameraPos - surfacePos);

	// Ambient
	vec3 ambient = light.ambientCoeff * surfaceColour.rgb * light.colour;

	// Diffuse
	float diffuseCoeff = max(0.0, dot(normal, surfaceToLight));
	vec3 diffuse = diffuseCoeff * surfaceColour.rgb * light.colour;

	// Specular
	float specularCoeff = 0.0;
	if (diffuseCoeff > 0.0)
	{
		specularCoeff = pow(max(0.0, dot(surfaceToCamera, reflect(-surfaceToLight, normal))), materialShininess);
	}
	vec3 specular = specularCoeff * materialSpecularColour * light.colour;

	// Attenutation
	float distanceToLight = length(surfaceToLight);
	float attenuation = 1.0 / (1.0 + light.attenuation * pow(distanceToLight, 2));

	// Linear colour (Colour before gamma correction)
	vec3 linearColour = ambient + attenuation * (diffuse + specular);

	// Final colour (After gamma correction)
	vec3 gamma = vec3(1.0 / 2.2);
	fragColour = vec4(pow(linearColour, gamma), surfaceColour.a);
}