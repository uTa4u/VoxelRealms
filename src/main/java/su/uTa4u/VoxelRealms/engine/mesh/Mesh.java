package su.uTa4u.VoxelRealms.engine.mesh;

public final class Mesh {
    private final int[] positions;
    private final float[] colors;
    private final int[] indices;

    public Mesh(int[] positions, float[] colors, int[] indices) {
        this.positions = positions;
        this.colors = colors;
        this.indices = indices;
    }

    public int getVertexCount() {
        return this.indices.length;
    }

    public int[] getPositions() {
        return this.positions;
    }

    public float[] getColors() {
        return this.colors;
    }

    public int[] getIndices() {
        return this.indices;
    }
}
