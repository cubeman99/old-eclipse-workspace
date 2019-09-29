#version 330

in vec3 position0;

out vec4 fragColor;

uniform samplerCube r_cubeMap;

void main()
{
	fragColor = texture(r_cubeMap, position0);
}
