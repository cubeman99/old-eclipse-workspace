#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;
layout (location = 3) in vec3 tangent;

out vec2 texCoord0;
out vec3 normal0;
out vec3 tangent0;
out mat3 tbnMatrix;
out vec3 worldPos0;

uniform mat4 transform;
uniform mat4 transformProjected;

void main()
{
    gl_Position = transformProjected * vec4(position, 1.0);
	
	texCoord0   = texCoord;
    normal0     = normalize(transform * vec4(normal, 0.0)).xyz;
    tangent0    = normalize(transform * vec4(tangent, 0.0)).xyz;
	worldPos0   = (transform * vec4(position, 1.0)).xyz;
	
	vec3 n    = normalize(transform * vec4(normal, 0.0)).xyz;
	vec3 t    = normalize(transform * vec4(tangent, 0.0)).xyz;
	t = normalize(t - (dot(t, n) * n));
	
	vec3 b    = cross(t, n);
	tbnMatrix = mat3(t, b, n);
}