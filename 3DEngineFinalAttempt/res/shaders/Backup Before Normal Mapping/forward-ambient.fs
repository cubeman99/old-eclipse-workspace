#version 330

varying vec2 texCoord0;

uniform vec3 r_ambientIntensity;
uniform sampler2D sampler;
//uniform vec3 m_color;

void main()
{
	gl_FragColor = texture2D(sampler, texCoord0.xy) * vec4(r_ambientIntensity, 1);
}
