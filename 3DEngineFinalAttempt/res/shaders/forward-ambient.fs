#version 330
#include "sampling.glh"

in vec2 texCoord0;
in vec3 worldPos0;
in mat3 tbnMatrix;
in vec3 normal0;

out vec4 fragColor;

uniform vec3 r_ambientIntensity;
uniform sampler2D diffuse;

uniform vec3 c_eyePos;
uniform sampler2D dispMap;

//uniform vec3 color;

uniform float dispMapScale;
uniform float dispMapBias;
uniform float reflectivity;

void main()
{
	vec3 directionToCamera = normalize(c_eyePos - worldPos0);
	vec2 texCoords = calcParallaxTexCoords(dispMap, tbnMatrix,
		directionToCamera, texCoord0, dispMapScale, dispMapBias);
	
	//fragColor = sampleBlendedNormal(diffuse, blendDiffuse, texCoords, normal0) * vec4(r_ambientIntensity, 1) * (1.0 - reflectivity);
	fragColor = sampleTexture(diffuse, texCoords) * vec4(r_ambientIntensity, 1) * (1.0 - reflectivity);
}

