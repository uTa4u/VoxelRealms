#version 330

in vec2 texCoord;

uniform sampler2D tex;
uniform vec3 color;

out vec4 FragColor;

void main() {
    FragColor = vec4(color, 1.0) * texture(tex, texCoord).w;
}