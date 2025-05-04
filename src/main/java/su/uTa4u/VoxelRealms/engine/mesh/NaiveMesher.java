package su.uTa4u.VoxelRealms.engine.mesh;

import su.uTa4u.VoxelRealms.world.Chunk;
import su.uTa4u.VoxelRealms.world.Voxel;
import su.uTa4u.VoxelRealms.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NaiveMesher {
    public NaiveMesher() {
    }

    public List<Mesh> createMeshForWorld(World world) {
        List<Mesh> meshes = new ArrayList<>();
//        for (int cy = 0; cy < SIZE; cy++) {
//            for (int cx = 0; cx < SIZE; cx++) {
//                for (int cz = 0; cz < SIZE; cz++) {
//                    Chunk chunk = this.chunks[cy][cx][cz];
//                    if (chunk == null) continue;
        for (int y = 0; y < World.SIZE; ++y) {
            for (int x = 0; x < World.SIZE; ++x) {
                for (int z = 0; z < World.SIZE; ++z) {
//                                Voxel voxel = chunk.voxels[y][x][z];
                    Voxel voxel = world.voxels[y][x][z];
                    if (voxel == null) continue;
                    meshes.add(this.createMeshForVoxel(voxel));
                }
            }
//                    }
//                }
//            }
        }
        return meshes;
    }

    private Mesh createMeshForVoxel(Voxel v) {
        return new Mesh(
                new int[]{
                        v.x, v.y, v.z,
                        v.x, v.y, v.z + 1,
                        v.x, v.y + 1, v.z,
                        v.x, v.y + 1, v.z + 1,
                        v.x + 1, v.y, v.z,
                        v.x + 1, v.y, v.z + 1,
                        v.x + 1, v.y + 1, v.z,
                        v.x + 1, v.y + 1, v.z + 1
                },
                new float[]{
                        0.5f, 0.0f, 0.0f,
                        0.0f, 0.5f, 0.0f,
                        0.0f, 0.0f, 0.5f,
                        0.0f, 0.5f, 0.5f,
                        0.5f, 0.0f, 0.0f,
                        0.0f, 0.5f, 0.0f,
                        0.0f, 0.0f, 0.5f,
                        0.0f, 0.5f, 0.5f,
                },
//                new float[]{
//                        v.r, v.g, v.b,
//                        v.r, v.g, v.b,
//                        v.r, v.g, v.b,
//                        v.r, v.g, v.b,
//                        v.r, v.g, v.b,
//                        v.r, v.g, v.b,
//                        v.r, v.g, v.b,
//                        v.r, v.g, v.b,
//                },
                new int[]{
                        0, 2, 4, 4, 2, 6, // +Z face
                        1, 3, 5, 5, 3, 7, // -Z face
                        5, 7, 4, 4, 7, 6, // +X face
                        1, 3, 0, 0, 3, 2, // -X face
                        1, 0, 5, 5, 0, 4, // +Y face
                        3, 2, 7, 7, 2, 6, // -Y face
                }
        );
    }
}
