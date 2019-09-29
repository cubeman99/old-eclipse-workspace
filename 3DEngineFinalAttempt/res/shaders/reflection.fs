#version 330

in vec2 texCoord0;
in vec3 worldPos0;
in vec3 normal0;
in mat3 tbnMatrix;

out vec4 fragColor;

uniform sampler2D normalMap;
uniform samplerCube r_cubeMap;
uniform vec3 c_eyePos;
uniform float reflectivity;

void main()
{
	vec3 normal = normalize(tbnMatrix * (((255.0 / 128.0)
		* texture(normalMap, texCoord0).xyz) - 1));
		
	vec3 directionToCamera = normalize(c_eyePos - worldPos0);
	vec3 refractDir = normalize(refract(-directionToCamera, normalize(normal), 1.33));
	//vec3 reflectDir = reflect(refractDir, normalize(normal));
	vec3 reflectDir = reflect(-directionToCamera, normalize(normal));
	fragColor = texture(r_cubeMap, reflectDir) * reflectivity;
	//if (reflectivity > 0.9)
	//	fragColor = vec4(vec3(0.5) + (normal.xyz * 0.5), 1);
}
