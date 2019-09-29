#version 330
in vec3 position;
in vec2 texCoord;

out vec2 texCoord0;
uniform mat4 t_transform;

void main()
{
    gl_Position = t_transform * vec4(position, 1.0);
    texCoord0 = texCoord; 
}
