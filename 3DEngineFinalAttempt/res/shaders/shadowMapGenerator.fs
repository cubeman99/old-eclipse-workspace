#version 330

out vec4 fragColor;

void main()
{
	float depth =  gl_FragCoord.z;
	
	float dx = dFdx(depth);
	float dy = dFdy(depth);
	float moment2 = (depth * depth) + (0.25 * ((dx * dy) + (dy * dy)));
	
	fragColor = vec4(gl_FragCoord.z, gl_FragCoord.z * gl_FragCoord.z, 0.0, 0.0);
}
