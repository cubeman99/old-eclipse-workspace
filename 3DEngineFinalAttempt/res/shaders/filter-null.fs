#version 330

out vec2 texCoord0;
out vec4 fragColor;

uniform sampler2D r_filterTexture;

void main()
{
	fragColor = texture(r_filterTexture, texCoord0);
}
