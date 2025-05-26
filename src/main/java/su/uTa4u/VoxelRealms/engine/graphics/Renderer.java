package su.uTa4u.VoxelRealms.engine.graphics;

import org.lwjgl.opengl.GL;
import su.uTa4u.VoxelRealms.Main;
import su.uTa4u.VoxelRealms.engine.Window;
import su.uTa4u.VoxelRealms.engine.graphics.text.TextRenderer;
import su.uTa4u.VoxelRealms.engine.mesh.ChunkMesh;
import su.uTa4u.VoxelRealms.engine.mesh.Mesh;
import su.uTa4u.VoxelRealms.engine.mesh.NaiveMesher;
import su.uTa4u.VoxelRealms.engine.mesh.VertexArray;
import su.uTa4u.VoxelRealms.world.World;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public final class Renderer {
    private final ShaderProgram shaderProgram;
    private final VertexArray vertexArray;

    private final World world;
    private final NaiveMesher mesher;

    private final List<Mesh> opaqueMeshes;
    private final List<Mesh> transparentMeshes;

    public final Projection projection;
    public final Camera camera;

    private final Window window;
    private final TextRenderer textRenderer;

    public Renderer(Window window) {
        this.window = window;
        this.textRenderer = new TextRenderer(this.window);
        this.vertexArray = new VertexArray();

        GL.createCapabilities();

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

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.world = new World();
        this.mesher = new NaiveMesher();
    }

    public void render() {
        List<ChunkMesh> meshes = this.mesher.createMeshForWorld(this.world, this.camera.getPosition());
        for (ChunkMesh chunkMesh : meshes) {
            this.opaqueMeshes.add(chunkMesh.getOpaque());
            this.transparentMeshes.add(chunkMesh.getTransparent());
        }

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        this.shaderProgram.bind();

        this.shaderProgram.setUniform("viewMatrix", this.camera.getMatrix());
        this.shaderProgram.setUniform("projectionMatrix", this.projection.getMatrix());

        if (Main.WIREFRAME_MODE) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }

        this.vertexArray.bind();

        glEnable(GL_DEPTH_TEST);

        for (Mesh mesh : this.opaqueMeshes) {
            this.vertexArray.setData(mesh);
//            this.shaderProgram.validate();
            glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        }

        glEnable(GL_BLEND);
        glDepthMask(false);
        for (Mesh mesh : this.transparentMeshes) {
            this.vertexArray.setData(mesh);
//            this.shaderProgram.validate();
            glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        }
        glDepthMask(true);
        glDisable(GL_BLEND);

        glDisable(GL_DEPTH_TEST);

        if (Main.WIREFRAME_MODE) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }

        this.vertexArray.unbind();

        this.shaderProgram.unbind();

//        this.renderText("Meshes: " + (this.transparentMeshes.size() + this.opaqueMeshes.size()) + "\nTriangles: " +
//                meshes.stream().mapToInt(ChunkMesh::getTriangleCount).sum() + "\n",
//                10, this.window.getHeight() - 10 - this.textRenderer.getFontHeight() * 2,
//                1.0f, 1.0f, 1.0f, 1.0f);

        this.opaqueMeshes.clear();
        this.transparentMeshes.clear();
    }

    public void renderText(String text, int x, int y, float r, float g, float b, float scale) {
        this.textRenderer.renderText(text, x, y, r, g, b, scale);
    }
}
