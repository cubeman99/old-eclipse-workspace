
// Return whether the given value is in the range 0 to 1 (exclusive).
bool inRange(float value)
{
	return (value > 0.002 && value < 0.998);
}

float calcShadowAmount(sampler2D shadowMap, vec4 initialShadowMapCoords)
{
	vec3 shadowMapCoords = (initialShadowMapCoords.xyz / initialShadowMapCoords.w) * vec3(0.5) + vec3(0.5);
	
	if (inRange(shadowMapCoords.x) && inRange(shadowMapCoords.y) && inRange(shadowMapCoords.z))
		return sampleVarianceShadowMap(shadowMap, shadowMapCoords.xy, shadowMapCoords.z, r_minVariance, r_lightBleedReduction);
	else
		return 1.0;
}

void main()
{
	vec3 directionToCamera = normalize(c_eyePos - worldPos0);
	vec2 texCoords = calcParallaxTexCoords(dispMap, tbnMatrix,
		directionToCamera, texCoord0, dispMapScale, dispMapBias);
	
	vec3 normal = normalize(tbnMatrix * (((255.0 / 128.0)
		* sampleTexture(normalMap, texCoords).xyz) - 1));
	
    vec4 lightingAmt =
		calcLightingEffect(normal, worldPos0) *
		calcShadowAmount(r_shadowMap, shadowMapCoords0);
	
	//fragColor = sampleBlendedNormal(diffuse, blendDiffuse, texCoords, normal) * lightingAmt * (1.0 - reflectivity);
	fragColor = sampleTexture(diffuse, texCoords) * lightingAmt * (1.0 - reflectivity);
}
