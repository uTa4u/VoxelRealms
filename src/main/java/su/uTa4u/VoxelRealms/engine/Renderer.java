package su.uTa4u.VoxelRealms.engine;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MathUtil;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import su.uTa4u.VoxelRealms.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;

public final class Renderer {
    public static final Renderer INSTANCE = new Renderer();

    private static final String VERTEX_SHADER = """
            #version 330 core
            
            uniform mat4 projection;
            
            layout (location = 0) in vec3 pos;

            void main()
            {
                gl_Position = projection * vec4(pos, 1.0);
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
    int uniformProjection;

    public void drawTriangle(Vector3i a, Vector3i b, Vector3i c) {
        int[] vertices = new int[9];
        vertices[0] = a.x;
        vertices[1] = a.y;
        vertices[2] = a.z;
        vertices[3] = b.x;
        vertices[4] = b.y;
        vertices[5] = b.z;
        vertices[6] = c.x;
        vertices[7] = c.y;
        vertices[8] = c.z;

        glUseProgram(shaderProg);
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    public void drawSquare(Vector3i a, Vector3i b, Vector3i c, Vector3i d) {
        drawTriangle(a, b, c);
        drawTriangle(a, d, c);
    }

    public void drawWorldForPlayer(World world, Player player) {
        List<Face> mesh = NaiveMesher.INSTANCE.generateMesh(world);
        Vector2i size = Engine.INSTANCE.getWindowSize();
        Matrix4f proj = new Matrix4f();
        proj.perspective((float) Math.toRadians(45), (float) size.x / size.y, 0.01f, 100.0f)
                .lookAt(player.pos.x, player.pos.y, player.pos.z,
                        0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f);
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        proj.get(fb);
        glUniformMatrix4fv(uniformProjection, false, fb);
        for (Face face : mesh) {
            this.drawSquare(face.a, face.b, face.c, face.d);
        }
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

        uniformProjection = glGetUniformLocation(shaderProg, "projection");

        glBindVertexArray(0);
    }
}
