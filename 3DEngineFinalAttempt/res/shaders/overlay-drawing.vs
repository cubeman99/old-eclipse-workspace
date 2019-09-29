#version 330

in vec3 position;

//uniform mat4 t_transform;
uniform mat4 t_transformProjected;

void main()
{
    gl_Position = t_transformProjected * vec4(position, 1.0);
}
