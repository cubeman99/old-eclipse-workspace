#version 330
#include "lighting.fsh"

uniform DirectionalLight r_directionalLight;

vec4 calcLightingEffect(vec3 normal, vec3 worldPos)
{
	return calcDirectionalLight(r_directionalLight, normal, worldPos);
}

#include "lightingMain.fsh"