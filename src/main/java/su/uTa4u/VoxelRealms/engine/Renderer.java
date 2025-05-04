package su.uTa4u.VoxelRealms.engine;

import org.lwjgl.opengl.GL;
import su.uTa4u.VoxelRealms.Projection;
import su.uTa4u.VoxelRealms.engine.mesh.Mesh;

import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public final class Renderer {
    private final ShaderProgram shaderProg;
    private final Mesh mesh;

    public final Projection projection;

    Renderer(Window window) {
        GL.createCapabilities();

        this.shaderProg = new ShaderProgram(List.of(
                new ShaderProgram.ShaderData(GL_VERTEX_SHADER, "basic.vert"),
                new ShaderProgram.ShaderData(GL_FRAGMENT_SHADER, "basic.frag")
        ));

        float[] positions = new float[]{
                -0.5f, 0.5f, -1.0f,
                -0.5f, -0.5f, -1.0f,
                0.5f, -0.5f, -1.0f,
                0.5f, 0.5f, -1.0f,
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

        this.projection = new Projection(window.getWidth(), window.getHeight());
        this.shaderProg.createUniform("projectionMatrix");

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        this.shaderProg.bind();

        this.shaderProg.setUniform("projectionMatrix", this.projection.getMatrix());

        glBindVertexArray(this.mesh.getVaoId());
        this.shaderProg.validate();
        glDrawElements(GL_TRIANGLES, this.mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        this.shaderProg.unbind();
    }
}
