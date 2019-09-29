#version 330
#include "lighting.fsh"

uniform SpotLight r_spotLight;

vec4 calcLightingEffect(vec3 normal, vec3 worldPos)
{
	return calcSpotLight(r_spotLight, normal, worldPos);
}

#include "lightingMain.fsh"