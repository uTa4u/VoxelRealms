package su.uTa4u.VoxelRealms.world;

public final class Voxel {
    public static final Voxel EMPTY = new Voxel(VoxelMaterial.AIR);

    private final VoxelMaterial material;

    public Voxel(VoxelMaterial material) {
        this.material = material;
    }

    public VoxelMaterial getMaterial() {
        return this.material;
    }

    public boolean isOpaque() {
        return this.material != VoxelMaterial.AIR;
    }
}
