#version 330
#include "lighting.fsh"

uniform PointLight r_pointLight;

vec4 calcLightingEffect(vec3 normal, vec3 worldPos)
{
	return calcPointLight(r_pointLight, normal, worldPos);
}

#include "lightingMain.fsh"
