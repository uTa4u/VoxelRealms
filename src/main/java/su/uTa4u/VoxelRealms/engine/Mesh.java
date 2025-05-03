package su.uTa4u.VoxelRealms.engine;

import static org.lwjgl.opengl.GL30.*;

public final class Mesh {
    private final int vertexCount;
    private final int vaoId;

    Mesh(float[] positions, int vertexCount) {
        this.vertexCount = vertexCount;

        this.vaoId = glGenVertexArrays();
        glBindVertexArray(this.vaoId);

        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public int getVaoId() {
        return this.vaoId;
    }

    public int getVertexCount() {
        return this.vertexCount;
    }
}
