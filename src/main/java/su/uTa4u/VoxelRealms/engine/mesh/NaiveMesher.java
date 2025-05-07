package su.uTa4u.VoxelRealms.engine.mesh;

import su.uTa4u.VoxelRealms.world.Chunk;
import su.uTa4u.VoxelRealms.world.Voxel;
import su.uTa4u.VoxelRealms.world.VoxelMaterial;
import su.uTa4u.VoxelRealms.world.World;

import java.util.ArrayList;
import java.util.List;

public final class NaiveMesher {
    public NaiveMesher() {
    }

    public List<Mesh> createMeshForWorld(World world) {
        List<Mesh> meshes = new ArrayList<>();
        for (int cy = 0; cy < World.SIZE; cy++) {
            for (int cx = 0; cx < World.SIZE; cx++) {
                for (int cz = 0; cz < World.SIZE; cz++) {
                    Chunk chunk = world.getChunk(cx, cy, cz);
                    if (chunk == null) continue;

                    for (int y = 0; y < Chunk.SIZE; ++y) {
                        for (int x = 0; x < Chunk.SIZE; ++x) {
                            for (int z = 0; z < Chunk.SIZE; ++z) {
                                Voxel voxel = chunk.getVoxel(x, y, z);
                                if (voxel == null) continue;
                                MeshBuilder builder = new MeshBuilder(voxel.getMaterial(), x, y, z);
                                if (!world.getVoxel(x + 1, y, z, cx, cy, cz).isOpaque()) {
                                    builder.addXpFace();
                                }
                                if (!world.getVoxel(x - 1, y, z, cx, cy, cz).isOpaque()) {
                                    builder.addXnFace();
                                }
                                if (!world.getVoxel(x, y + 1, z, cx, cy, cz).isOpaque()) {
                                    builder.addYpFace();
                                }
                                if (!world.getVoxel(x, y - 1, z, cx, cy, cz).isOpaque()) {
                                    builder.addYnFace();
                                }
                                if (!world.getVoxel(x, y, z + 1, cx, cy, cz).isOpaque()) {
                                    builder.addZpFace();
                                }
                                if (!world.getVoxel(x, y, z - 1, cx, cy, cz).isOpaque()) {
                                    builder.addZnFace();
                                }
                                meshes.add(builder.build());
                            }
                        }
                    }

                }
            }
        }
        return meshes;
    }

    private static class MeshBuilder {
        private static final int[] XP_FACE = new int[]{5, 7, 4, 4, 7, 6};
        private static final int[] XN_FACE = new int[]{1, 3, 0, 0, 3, 2};
        private static final int[] YP_FACE = new int[]{3, 2, 7, 7, 2, 6};
        private static final int[] YN_FACE = new int[]{1, 0, 5, 5, 0, 4};
        private static final int[] ZP_FACE = new int[]{1, 3, 5, 5, 3, 7};
        private static final int[] ZN_FACE = new int[]{0, 2, 4, 4, 2, 6};

        private final VoxelMaterial m;
        private final int x;
        private final int y;
        private final int z;
        private final int[] indices;

        private MeshBuilder(VoxelMaterial m, int x, int y, int z) {
            this.m = m;
            this.x = x;
            this.y = y;
            this.z = z;
            this.indices = new int[36];
        }

        private void addZpFace() {
            System.arraycopy(ZP_FACE, 0, this.indices, 0, 6);
        }

        private void addZnFace() {
            System.arraycopy(ZN_FACE, 0, this.indices, 6, 6);
        }

        private void addXpFace() {
            System.arraycopy(XP_FACE, 0, this.indices, 12, 6);
        }

        private void addXnFace() {
            System.arraycopy(XN_FACE, 0, this.indices, 18, 6);
        }

        private void addYpFace() {
            System.arraycopy(YP_FACE, 0, this.indices, 24, 6);
        }

        private void addYnFace() {
            System.arraycopy(YN_FACE, 0, this.indices, 30, 6);
        }

        private Mesh build() {
            return new Mesh(
                    new int[]{
                            this.x, this.y, this.z,
                            this.x, this.y, this.z + 1,
                            this.x, this.y + 1, this.z,
                            this.x, this.y + 1, this.z + 1,
                            this.x + 1, this.y, this.z,
                            this.x + 1, this.y, this.z + 1,
                            this.x + 1, this.y + 1, this.z,
                            this.x + 1, this.y + 1, this.z + 1
                    },
                    new float[]{
                            this.m.r, this.m.g, this.m.b, this.m.a,
                            this.m.r, this.m.g, this.m.b, this.m.a,
                            this.m.r, this.m.g, this.m.b, this.m.a,
                            this.m.r, this.m.g, this.m.b, this.m.a,
                            this.m.r, this.m.g, this.m.b, this.m.a,
                            this.m.r, this.m.g, this.m.b, this.m.a,
                            this.m.r, this.m.g, this.m.b, this.m.a,
                            this.m.r, this.m.g, this.m.b, this.m.a,
                    },
                    this.indices
            );
        }
    }

}
