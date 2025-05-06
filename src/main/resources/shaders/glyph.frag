#version 330

in vec2 texCoord;

uniform sampler2D tex;

out vec4 FragColor;

void main() {
    FragColor = texture(tex, texCoord);
//    FragColor = vec4(1.0, 0.0, 0.0, 1.0);
//    FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}