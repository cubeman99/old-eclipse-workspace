#version 330
#include "water.glh"

//layout (location = 2) in vec3 normal;
//layout (location = 3) in vec3 tangent;

in vec3 position;
//in vec2 texCoord;
out vec3 normal0;
out vec3 worldPos0;

uniform float g_time;
uniform Wave g_wave;

//uniform mat4 t_transform;
uniform mat4 t_transformProjected;

void main()
{
	vec3 pos = position.xyz;
	//pos.y += g_wave.amplitude * sin((dot(g_wave.direction, pos.xz) * g_wave.frequency) + (g_time * g_wave.phaseTime));
	normal0 = vec3(0, 1, 0);
	
	float slope = 0;
	slope += g_wave.amplitude * sin((dot(g_wave.direction, pos.xz) * g_wave.frequency) + (g_time * g_wave.phaseTime));
	pos.y += slope;
	
	float rough = 0.4;
	pos.x += rough * g_wave.amplitude * g_wave.direction.x * cos((dot(g_wave.direction, pos.xz) * g_wave.frequency) + (g_time * g_wave.phaseTime));
	pos.y += rough * g_wave.amplitude * g_wave.direction.y * cos((dot(g_wave.direction, pos.xz) * g_wave.frequency) + (g_time * g_wave.phaseTime));
	
	slope = 0;
	slope += g_wave.frequency * g_wave.direction.x * g_wave.amplitude * cos((dot(g_wave.direction, pos.xz) * g_wave.frequency) + (g_time * g_wave.phaseTime));
	normal0.x = -slope;
	
	slope = 0;
	slope += g_wave.frequency * g_wave.direction.y * g_wave.amplitude * cos((dot(g_wave.direction, pos.xz) * g_wave.frequency) + (g_time * g_wave.phaseTime));
	normal0.z = -slope;
	normal0 = normalize(normal0);
	
	worldPos0 = pos;
    gl_Position = t_transformProjected * vec4(pos, 1.0);
}
