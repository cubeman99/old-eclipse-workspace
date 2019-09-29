#version 330


in vec2 texCoord0;
in vec3 normal0;
in vec3 worldPos0;
out vec4 fragColor;

uniform vec3 c_eyePos;
uniform sampler2D sampler;
uniform float m_specularIntensity;
uniform float m_specularPower;

#include "lighting.glh"

uniform PointLight r_pointLight;



void main()
{
    fragColor = texture(sampler, texCoord0.xy) *
		calcPointLight(r_pointLight, normalize(normal0), worldPos0);
}
