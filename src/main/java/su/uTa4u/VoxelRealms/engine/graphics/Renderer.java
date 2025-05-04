package su.uTa4u.VoxelRealms.engine.graphics;

import org.lwjgl.opengl.GL;
import su.uTa4u.VoxelRealms.engine.Window;
import su.uTa4u.VoxelRealms.engine.mesh.Mesh;
import su.uTa4u.VoxelRealms.engine.mesh.NaiveMesher;
import su.uTa4u.VoxelRealms.world.World;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public final class Renderer {
    private final ShaderProgram shaderProg;
    private final List<Mesh> meshes;

    public final Projection projection;
    public final Camera camera;

    public Renderer(Window window) {
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);

        this.shaderProg = new ShaderProgram(List.of(
                new ShaderProgram.ShaderData(GL_VERTEX_SHADER, "basic.vert"),
                new ShaderProgram.ShaderData(GL_FRAGMENT_SHADER, "basic.frag")
        ));

        this.meshes = new ArrayList<>();

        this.camera = new Camera();
        this.shaderProg.createUniform("viewMatrix");

        this.projection = new Projection(window.getWidth(), window.getHeight());
        this.shaderProg.createUniform("projectionMatrix");

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        this.shaderProg.bind();

        this.shaderProg.setUniform("viewMatrix", this.camera.getMatrix());
        this.shaderProg.setUniform("projectionMatrix", this.projection.getMatrix());

        for (Mesh mesh : this.meshes) {
            glBindVertexArray(mesh.getVaoId());
            this.shaderProg.validate();
            glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        }
        glBindVertexArray(0);

        this.shaderProg.unbind();
    }

    public void addMeshes(List<Mesh> meshes) {
        this.meshes.addAll(meshes);
    }
}
