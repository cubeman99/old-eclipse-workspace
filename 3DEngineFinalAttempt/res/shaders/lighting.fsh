in vec2 texCoord0;
in vec3 worldPos0;
in vec3 normal0;
in vec4 shadowMapCoords0;
in mat3 tbnMatrix;

out vec4 fragColor;

uniform sampler2D diffuse;
uniform sampler2D normalMap;
uniform sampler2D dispMap;
uniform sampler2D specularMap;

uniform float dispMapScale;
uniform float dispMapBias;
uniform float reflectivity;

uniform sampler2D r_shadowMap;

uniform float r_minVariance;
uniform float r_lightBleedReduction;

#include "lighting.glh"
