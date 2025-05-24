package su.uTa4u.VoxelRealms.engine.mesh;

import org.joml.Vector3f;

import java.util.Comparator;

public final class ChunkMesh {
    private final Mesh opaque;
    private final Mesh transparent;
    private final float distToCam;

    public ChunkMesh(Mesh opaque, Mesh transparent, float distToCam) {
        this.opaque = opaque;
        this.transparent = transparent;
        this.distToCam = distToCam;
    }

    public int getTriangleCount() {
        return (this.opaque.getVertexCount() + this.transparent.getVertexCount()) / 3;
    }

    public Mesh getOpaque() {
        return this.opaque;
    }

    public Mesh getTransparent() {
        return this.transparent;
    }

    public float getDistToCam() {
        return this.distToCam;
    }
}
