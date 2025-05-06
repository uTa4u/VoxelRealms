package su.uTa4u.VoxelRealms.engine.graphics;

import org.lwjgl.opengl.GL;
import su.uTa4u.VoxelRealms.engine.Window;
import su.uTa4u.VoxelRealms.engine.mesh.Mesh;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public final class Renderer {
    private final ShaderProgram shaderProgram;
    private final List<Mesh> meshes;

    public final Projection projection;
    public final Camera camera;

    public Renderer(Window window) {
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);

        this.shaderProgram = new ShaderProgram(List.of(
                new ShaderProgram.ShaderData(GL_VERTEX_SHADER, "voxel.vert"),
                new ShaderProgram.ShaderData(GL_FRAGMENT_SHADER, "voxel.frag")
        ));

        this.meshes = new ArrayList<>();

        this.camera = new Camera();
        this.shaderProgram.createUniform("viewMatrix");

        this.projection = new Projection(window.getWidth(), window.getHeight());
        this.shaderProgram.createUniform("projectionMatrix");

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        this.shaderProgram.bind();

        this.shaderProgram.setUniform("viewMatrix", this.camera.getMatrix());
        this.shaderProgram.setUniform("projectionMatrix", this.projection.getMatrix());

        for (Mesh mesh : this.meshes) {
            glBindVertexArray(mesh.getVaoId());
            this.shaderProgram.validate();
            glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        }
        glBindVertexArray(0);

        this.shaderProgram.unbind();
    }

    public void addMeshes(List<Mesh> meshes) {
        this.meshes.addAll(meshes);
    }
}
