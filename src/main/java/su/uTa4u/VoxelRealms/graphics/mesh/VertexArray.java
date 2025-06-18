package su.uTa4u.VoxelRealms.graphics.mesh;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public final class VertexArray {
    private final int vaoId;
    private final int positionVboId;
    private final int colorVboId;
    private final int eboId;

    public VertexArray() {
        this.vaoId = glGenVertexArrays();
        this.bind();

        this.positionVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.positionVboId);
        glVertexAttribPointer(0, 3, GL_INT, false, 0, 0);
        glEnableVertexAttribArray(0);

        this.colorVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.colorVboId);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        this.eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.eboId);

        this.unbind();
    }

    public void setData(Mesh mesh) {
        // Position
        glBindBuffer(GL_ARRAY_BUFFER, this.positionVboId);
        glBufferData(GL_ARRAY_BUFFER, mesh.getPositions(), GL_STATIC_DRAW);

        // Color
        glBindBuffer(GL_ARRAY_BUFFER, this.colorVboId);
        glBufferData(GL_ARRAY_BUFFER, mesh.getColors(), GL_STATIC_DRAW);

        // Index
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, mesh.getIndices(), GL_STATIC_DRAW);
    }

    public void bind() {
        glBindVertexArray(this.vaoId);
    }

    public void unbind() {
        glBindVertexArray(0);
    }
}
