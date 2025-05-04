#version 330 core

layout (location = 0) in vec3 in_pos;
layout (location = 1) in vec3 in_color;

uniform mat4 projectionMatrix;

out vec4 vertexColor;

void main()
{
    gl_Position = projectionMatrix * vec4(in_pos, 1.0);
    vertexColor = vec4(in_color, 1.0);
}