#version 330 core

layout (location = 0) in vec3 in_pos;
layout (location = 1) in vec4 in_color;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out vec4 vertexColor;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * vec4(in_pos, 1.0);
    vertexColor = in_color;
}