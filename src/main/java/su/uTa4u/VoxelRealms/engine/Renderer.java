package su.uTa4u.VoxelRealms.engine;

import org.joml.Vector3i;
import org.lwjgl.opengl.GL;

import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public final class Renderer {
    private final ShaderProgram shaderProg;
    private final Mesh mesh;

    Renderer() {
        GL.createCapabilities();

        this.shaderProg = new ShaderProgram(List.of(
                new ShaderProgram.ShaderData(GL_VERTEX_SHADER, "./shaders/basic.vert"),
                new ShaderProgram.ShaderData(GL_FRAGMENT_SHADER, "./shaders/basic.frag")
        ));

        float[] positions = new float[]{
                0.0f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f
        };

        this.mesh = new Mesh(positions, 3);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        this.shaderProg.bind();

        glBindVertexArray(this.mesh.getVaoId());
        this.shaderProg.validate();
        glDrawArrays(GL_TRIANGLES, 0, this.mesh.getVertexCount());
        glBindVertexArray(0);

        this.shaderProg.unbind();
    }

//    public void drawTriangle(Vector3i a, Vector3i b, Vector3i c) {
//        int[] vertices = new int[9];
//        vertices[0] = a.x;
//        vertices[1] = a.y;
//        vertices[2] = a.z;
//        vertices[3] = b.x;
//        vertices[4] = b.y;
//        vertices[5] = b.z;
//        vertices[6] = c.x;
//        vertices[7] = c.y;
//        vertices[8] = c.z;
//
//        glUseProgram(shaderProg);
//        glBindVertexArray(vao);
//        glBindBuffer(GL_ARRAY_BUFFER, vbo);
//        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
//        glDrawArrays(GL_TRIANGLES, 0, 3);
//    }
//
//    public void drawSquare(Vector3i a, Vector3i b, Vector3i c, Vector3i d) {
//        drawTriangle(a, b, c);
//        drawTriangle(a, d, c);
//    }
}
