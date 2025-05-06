package su.uTa4u.VoxelRealms.world;

public final class Voxel {
    public final int x;
    public final int y;
    public final int z;

    public final VoxelMaterial material;

    public Voxel(int x, int y, int z, VoxelMaterial material) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
    }
}
