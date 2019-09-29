#version 330

in vec3 position;

out vec3 position0;

uniform mat4 t_transformProjected;

void main()
{
	position0 = position;
    gl_Position = t_transformProjected * vec4(position, 1.0);
}
