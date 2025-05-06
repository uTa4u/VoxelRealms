package su.uTa4u.VoxelRealms.world;

import java.util.Random;

public final class VoxelMaterial {
    private static final VoxelMaterial STONE = new VoxelMaterial(128, 128, 128);
    private static final VoxelMaterial GRASS = new VoxelMaterial(144, 244, 144);

    private static final Random RANDOM = new Random();
    private static final VoxelMaterial[] VOXEL_MATERIALS = new VoxelMaterial[]{
            su.uTa4u.VoxelRealms.world.VoxelMaterial.STONE,
            su.uTa4u.VoxelRealms.world.VoxelMaterial.GRASS,
    };
    private static final int VOXEL_MATERIAL_COUNT = VOXEL_MATERIALS.length;

    public final float r;
    public final float g;
    public final float b;

    private VoxelMaterial(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    private VoxelMaterial(int r, int g, int b) {
        this(r / 255.0f, g / 255.0f, b / 255.0f);
    }

    public static VoxelMaterial getRandom() {
        return VOXEL_MATERIALS[RANDOM.nextInt(VOXEL_MATERIAL_COUNT)];
    }
}
