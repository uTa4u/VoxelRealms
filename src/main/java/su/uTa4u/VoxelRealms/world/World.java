package su.uTa4u.VoxelRealms.world;

import su.uTa4u.VoxelRealms.engine.mesh.Mesh;

import java.util.ArrayList;
import java.util.List;

public final class World {
    public static final int SIZE = 32;

    //    public final Chunk[][][] chunks = new Chunk[SIZE][SIZE][SIZE];
    public final Voxel[][][] voxels = new Voxel[SIZE][SIZE][SIZE];

    public World() {
//        this.chunks[SIZE / 2][SIZE / 2][SIZE - 1] = new Chunk(0, 0, 0);
//        for (int y = 0; y < SIZE; ++y) {
//            for (int x = 0; x < SIZE; ++x) {
//                for (int z = 0; z < SIZE; ++z) {
//                    voxels[y][x][z] = new Voxel(x, y, z, 255, 0, 0);
//                }
//            }
//        }
        voxels[0][0][0] = new Voxel(0, 0, -10, 0.0f, 1.0f, 0.0f);
//        voxels[0][0][1] = new Voxel(0, 1, -2, 1.0f, 0.0f, 0.0f);
//        voxels[0][0][2] = new Voxel(1, 0, -2, 0.0f, 0.0f, 1.0f);
    }
}
