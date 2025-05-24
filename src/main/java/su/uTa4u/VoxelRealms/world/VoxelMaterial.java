package su.uTa4u.VoxelRealms.world;

import java.util.Random;

public final class VoxelMaterial {
    public static final VoxelMaterial AIR = new VoxelMaterial(0, 0, 0, 0);
    public static final VoxelMaterial STONE = new VoxelMaterial(128, 128, 128, 255);
    public static final VoxelMaterial GRASS = new VoxelMaterial(144, 244, 144, 255);
    public static final VoxelMaterial GLASS = new VoxelMaterial(144, 0, 0, 63);

    private static final Random RANDOM = new Random(1);
    private static final VoxelMaterial[] VOXEL_MATERIALS = new VoxelMaterial[]{
            VoxelMaterial.AIR,
            VoxelMaterial.STONE,
            VoxelMaterial.GRASS,
            VoxelMaterial.GLASS,
    };
    private static final int VOXEL_MATERIAL_COUNT = VOXEL_MATERIALS.length;

    public final float r;
    public final float g;
    public final float b;
    public final float a;

    private VoxelMaterial(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    private VoxelMaterial(int r, int g, int b, int a) {
        this(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    public static VoxelMaterial getRandom() {
        return VOXEL_MATERIALS[RANDOM.nextInt(VOXEL_MATERIAL_COUNT)];
    }
}
