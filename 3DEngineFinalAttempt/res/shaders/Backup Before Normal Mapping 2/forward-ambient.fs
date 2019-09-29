#version 330

in vec2 texCoord0;

out vec4 fragColor;

uniform vec3 r_ambientIntensity;
uniform sampler2D diffuse;
//uniform vec3 m_color;

void main()
{
	fragColor = texture(diffuse, texCoord0.xy) * vec4(r_ambientIntensity, 1);
}
