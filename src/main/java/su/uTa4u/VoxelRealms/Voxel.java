package su.uTa4u.VoxelRealms;

import org.joml.Vector3i;

public final class Voxel {
    public final int x;
    public final int y;
    public final int z;
    public final Vector3i v;

    public final byte r;
    public final byte g;
    public final byte b;

    public Voxel(int x, int y, int z, byte r, byte g, byte b) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.v = new Vector3i(this.x, this.y, this.z);
        
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
