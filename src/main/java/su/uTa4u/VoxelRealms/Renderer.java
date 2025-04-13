package su.uTa4u.VoxelRealms;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public final class Renderer {
    public static final Renderer INSTANCE = new Renderer();

    private static final String VERTEX_SHADER = """
            #version 330 core
            layout (location = 0) in vec3 pos;

            void main()
            {
                gl_Position = vec4(pos.x, pos.y, pos.z, 1.0);
            }
            """;

    private static final String FRAGMENT_SHADER = """
            #version 330 core
            out vec4 FragColor;
              
            void main()
            {
                FragColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);
            }
            """;

    int vbo;
    int vao;
    int vert;
    int frag;
    int shaderProg;

    public void drawTriangle(Vector3f[] vertices) {
        glUseProgram(shaderProg);
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, MathUtil.foo(vertices), GL_STATIC_DRAW);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    private Renderer() {
        IntBuffer success = MemoryUtil.memAllocInt(1);

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        vert = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vert, VERTEX_SHADER);
        glCompileShader(vert);
        glGetShaderiv(vert, GL_COMPILE_STATUS, success);
        if (success.get(0) != 0) {
            System.out.println(glGetShaderInfoLog(vert));
        }

        frag = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(frag, FRAGMENT_SHADER);
        glCompileShader(frag);
        glGetShaderiv(frag, GL_COMPILE_STATUS, success);
        if (success.get(0) != 0) {
            System.out.println(glGetShaderInfoLog(frag));
        }

        shaderProg = glCreateProgram();
        glAttachShader(shaderProg, vert);
        glAttachShader(shaderProg, frag);
        glLinkProgram(shaderProg);
        glGetProgramiv(shaderProg, GL_LINK_STATUS, success);
        if (success.get(0) != 0) {
            System.out.println(glGetProgramInfoLog(shaderProg));
        }

        glDeleteShader(vert);
        glDeleteShader(frag);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);
    }
}
