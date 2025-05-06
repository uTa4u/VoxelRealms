package su.uTa4u.VoxelRealms.world;

public final class Chunk {
    public static final int SIZE = 32;

    public final Voxel[][][] voxels = new Voxel[SIZE][SIZE][SIZE];

    private final int cx;
    private final int cy;
    private final int cz;

    public Chunk(int i, int j, int k) {
        this.cx = i * SIZE;
        this.cy = j * SIZE;
        this.cz = k * SIZE;
        for (int y = 0; y < SIZE / 2; ++y) {
            for (int x = 0; x < SIZE / 2; ++x) {
                for (int z = 0; z < SIZE / 2; ++z) {
                    this.voxels[y][x][z] = new Voxel(x + cx, y + cy, z + cz, VoxelMaterial.getRandom());
                }
            }
        }
//        this.voxels[0][0][0] = new Voxel(0 + cx, 0 + cy, 0 + cz, VoxelMaterial.getRandom());
//        this.voxels[0][0][1] = new Voxel(0 + cx, 0 + cy, 1 + cz, VoxelMaterial.getRandom());
//        this.voxels[0][0][2] = new Voxel(0 + cx, 0 + cy, 2 + cz, VoxelMaterial.getRandom());
//        this.voxels[0][0][3] = new Voxel(0 + cx, 0 + cy, 3 + cz, VoxelMaterial.getRandom());
//        this.voxels[0][0][4] = new Voxel(0 + cx, 0 + cy, 4 + cz, VoxelMaterial.getRandom());
    }
}
