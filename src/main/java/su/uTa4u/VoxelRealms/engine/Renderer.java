package su.uTa4u.VoxelRealms.engine;

import org.lwjgl.opengl.GL;

import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public final class Renderer {
    private final ShaderProgram shaderProg;
    private final Mesh mesh;

    Renderer() {
        GL.createCapabilities();

        this.shaderProg = new ShaderProgram(List.of(
                new ShaderProgram.ShaderData(GL_VERTEX_SHADER, "basic.vert"),
                new ShaderProgram.ShaderData(GL_FRAGMENT_SHADER, "basic.frag")
        ));

        float[] positions = new float[]{
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
        };
        float[] colors = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };

        this.mesh = new Mesh(positions, colors, indices);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        this.shaderProg.bind();

        glBindVertexArray(this.mesh.getVaoId());
        this.shaderProg.validate();
        glDrawElements(GL_TRIANGLES, this.mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
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
