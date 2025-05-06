#version 330

layout (location = 0) in vec4 in_data; // vec2 pos + vec2 texCoord

uniform ivec2 resolution;

out vec2 texCoord;

void main() {
    float x = in_data.x / resolution.x * 2 - 1;
    float y = in_data.y / resolution.y * 2 - 1;
    gl_Position = vec4(x, y, 0.0, 1.0);
    texCoord = in_data.zw;
}