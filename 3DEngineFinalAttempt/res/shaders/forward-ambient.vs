#version 330

layout (location = 2) in vec3 normal;
layout (location = 3) in vec3 tangent;

in vec3 position;
in vec2 texCoord;

out vec2 texCoord0;
out vec3 worldPos0;
out mat3 tbnMatrix;
out vec3 normal0;

uniform mat4 t_transform;
uniform mat4 t_transformProjected;

void main()
{
    gl_Position = t_transformProjected * vec4(position, 1.0);
	texCoord0 = texCoord;
	
    worldPos0   = (t_transform * vec4(position, 1.0)).xyz;
	
	vec3 n = normalize(t_transform * vec4(normal, 0.0)).xyz;
	vec3 t = normalize(t_transform * vec4(tangent, 0.0)).xyz;
	t = normalize(t - (dot(t, n) * n));
	
	vec3 b    = cross(t, n);
	tbnMatrix = mat3(t, b, n);
	
	normal0 = normal;
}
