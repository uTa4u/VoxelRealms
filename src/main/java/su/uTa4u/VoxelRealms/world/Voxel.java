package su.uTa4u.VoxelRealms.world;

import org.joml.Vector3i;
import su.uTa4u.VoxelRealms.engine.mesh.Mesh;

public final class Voxel {
    public final int x;
    public final int y;
    public final int z;

    public final float r;
    public final float g;
    public final float b;

    public Voxel(int x, int y, int z, float r, float g, float b) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.r = r;
        this.g = g;
        this.b = b;
    }
}
