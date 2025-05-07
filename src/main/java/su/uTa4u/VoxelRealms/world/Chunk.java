package su.uTa4u.VoxelRealms.world;

import su.uTa4u.VoxelRealms.util.Utils;

public final class Chunk {
    public static final int SIZE = 32;

    public final Voxel[] voxels = new Voxel[SIZE * SIZE * SIZE];

    public Chunk() {
        for (int y = 0; y < SIZE; ++y) {
            for (int x = 0; x < SIZE; ++x) {
                for (int z = 0; z < SIZE; ++z) {
                    this.setVoxel(new Voxel(VoxelMaterial.getRandom()), x, y, z);
                }
            }
        }
//        this.setVoxel(Voxel.EMPTY, 0, 0, 0);
    }

    public Voxel getVoxel(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0) return Voxel.EMPTY;
        if (x >= SIZE || y >= SIZE || z >= SIZE) return Voxel.EMPTY;
        return this.voxels[Utils.posToIndex(x, y, z, SIZE)];
    }

    public void setVoxel(Voxel v, int x, int y, int z) {
        this.voxels[Utils.posToIndex(x, y, z, SIZE)] = v;
    }
}
