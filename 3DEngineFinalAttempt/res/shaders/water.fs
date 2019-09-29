#version 330

out vec4 fragColor;

in vec3 worldPos0;
in vec3 normal0;

uniform samplerCube r_cubeMap;
uniform vec3 c_eyePos;

void main()
{
	
	float r = normal0.x * 0.5 + 0.5;
	float g = normal0.z * 0.5 + 0.5;
	float b = normal0.y * 0.5 + 0.5;
	
	
	vec3 directionToCamera = normalize(c_eyePos - worldPos0);
	vec3 reflectDir = reflect(-directionToCamera, normalize(normal0));
	fragColor = texture(r_cubeMap, reflectDir);
	//fragColor *= 0.00001;
	//fragColor += vec4(r, g, b, 1) * 0.00001;
	//fragColor.g += 1;
	//fragColor.a += 1;
	//fragColor += vec4(r, g, b, 1);
}

