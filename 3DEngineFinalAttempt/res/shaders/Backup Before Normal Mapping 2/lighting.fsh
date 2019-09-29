in vec2 texCoord0;
in vec3 worldPos0;
in vec3 normal0;

out vec4 fragColor;

uniform sampler2D diffuse;

#include "lighting.glh"
