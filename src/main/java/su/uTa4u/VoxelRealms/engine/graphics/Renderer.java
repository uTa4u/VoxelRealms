package su.uTa4u.VoxelRealms.engine.graphics;

import org.lwjgl.opengl.GL;
import su.uTa4u.VoxelRealms.Main;
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
    private final List<Mesh> opaqueMeshes;
    private final List<Mesh> transparentMeshes;

    public final Projection projection;
    public final Camera camera;

    public Renderer(Window window) {
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.shaderProgram = new ShaderProgram(List.of(
                new ShaderProgram.ShaderData(GL_VERTEX_SHADER, "voxel.vert"),
                new ShaderProgram.ShaderData(GL_FRAGMENT_SHADER, "voxel.frag")
        ));

        this.opaqueMeshes = new ArrayList<>();
        this.transparentMeshes = new ArrayList<>();

        this.camera = new Camera();
        this.shaderProgram.createUniform("viewMatrix");

        this.projection = new Projection(window.getWidth(), window.getHeight());
        this.shaderProgram.createUniform("projectionMatrix");

        glClearColor(0.69f, 0.85f, 0.9f, 1.0f);
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        this.shaderProgram.bind();

        this.shaderProgram.setUniform("viewMatrix", this.camera.getMatrix());
        this.shaderProgram.setUniform("projectionMatrix", this.projection.getMatrix());

        if (Main.WIREFRAME_MODE) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        for (Mesh mesh : this.opaqueMeshes) {
            glBindVertexArray(mesh.getVaoId());
            this.shaderProgram.validate();
            glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        }

        glDepthMask(false);
        for (Mesh mesh : this.transparentMeshes) {
            glBindVertexArray(mesh.getVaoId());
            this.shaderProgram.validate();
            glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        }
        glDepthMask(true);

        glBindVertexArray(0);

        if (Main.WIREFRAME_MODE) glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        this.shaderProgram.unbind();
    }

    public void addOpaqueMeshes(List<Mesh> meshes) {
        this.opaqueMeshes.addAll(meshes);
    }

    public void addTransparentMeshes(List<Mesh> meshes) {
        this.transparentMeshes.addAll(meshes);
    }
}
