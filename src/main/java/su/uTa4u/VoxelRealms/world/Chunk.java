package su.uTa4u.VoxelRealms.world;

import su.uTa4u.VoxelRealms.util.Utils;

public final class Chunk {
    public static final int SIZE = 32;
    public static final int SIZE_HALF = SIZE / 2;
    public static final int SIZE_QUBED = SIZE * SIZE * SIZE;

    public final Voxel[] voxels = new Voxel[SIZE_QUBED];

    private boolean shouldRemesh;

    public Chunk() {
        for (int y = 0; y < SIZE; ++y) {
            for (int x = 0; x < SIZE; ++x) {
                for (int z = 0; z < SIZE; ++z) {
                    this.setVoxel(new Voxel(VoxelMaterial.getRandom()), x, y, z);
                }
            }
        }
    }

    public Voxel getVoxel(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0) return Voxel.EMPTY;
        if (x >= SIZE || y >= SIZE || z >= SIZE) return Voxel.EMPTY;
        Voxel v = this.voxels[Utils.posToIndex(x, y, z, SIZE)];
        return v != null ? v : Voxel.EMPTY;
    }

    public void setVoxel(Voxel v, int x, int y, int z) {
        this.voxels[Utils.posToIndex(x, y, z, SIZE)] = v;
    }

    public void setShouldRemesh() {
        this.shouldRemesh = true;
    }

    public boolean getShouldRemesh() {
        return this.shouldRemesh;
    }
}
