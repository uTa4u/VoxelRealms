package su.uTa4u.VoxelRealms.engine.mesh;

import su.uTa4u.VoxelRealms.world.Chunk;
import su.uTa4u.VoxelRealms.world.Voxel;
import su.uTa4u.VoxelRealms.world.World;

import java.util.ArrayList;
import java.util.List;

public final class NaiveMesher {
    public NaiveMesher() {
    }

    public MeshPair createMeshForWorld(World world) {
        MeshPair meshes = new MeshPair();
        for (int cy = 0; cy < World.SIZE; cy++) {
            for (int cx = 0; cx < World.SIZE; cx++) {
                for (int cz = 0; cz < World.SIZE; cz++) {
                    Chunk chunk = world.getChunk(cx, cy, cz);
                    if (chunk == null) continue;

                    MeshBuilder opaque = new MeshBuilder();
                    MeshBuilder transparent = new MeshBuilder();
                    for (int y = 0; y < Chunk.SIZE; ++y) {
                        for (int x = 0; x < Chunk.SIZE; ++x) {
                            for (int z = 0; z < Chunk.SIZE; ++z) {
                                Voxel voxel = chunk.getVoxel(x, y, z);
                                if (voxel == null) continue;
                                MeshBuilder builder = voxel.isOpaque() ? opaque : transparent;
                                builder.addVoxel(voxel, cx * Chunk.SIZE + x, cy * Chunk.SIZE + y, cz * Chunk.SIZE + z);
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
                                builder.incVoxelCount();
                            }
                        }
                    }
                    meshes.addOpaque(opaque.build());
                    meshes.addTransparent(transparent.build());
                }
            }
        }
        return meshes;
    }

    private static class MeshBuilder {
        private static final int POSITIONS_OFFSET = 24;
        private static final int COLORS_OFFSET = 32;
        private static final int INDEX_OFFSET = 8;
        private static final int FACE_OFFSET = 6;
        private static final int FACE_COUNT_MAX = 6;

        private final int[] positions;
        private final float[] colors;
        private final int[] indices;
        private int voxelCount;
        private int faceCount;

        private MeshBuilder() {
            this.positions = new int[Chunk.SIZE * Chunk.SIZE * Chunk.SIZE * POSITIONS_OFFSET];
            this.colors = new float[Chunk.SIZE * Chunk.SIZE * Chunk.SIZE * COLORS_OFFSET];
            this.indices = new int[Chunk.SIZE * Chunk.SIZE * Chunk.SIZE * FACE_OFFSET * FACE_COUNT_MAX];
            this.voxelCount = 0;
            this.faceCount = 0;
        }

        private void addVoxel(Voxel v, int x, int y, int z) {
            int[] positions = new int[]{
                    x, y, z,
                    x, y, z + 1,
                    x, y + 1, z,
                    x, y + 1, z + 1,
                    x + 1, y, z,
                    x + 1, y, z + 1,
                    x + 1, y + 1, z,
                    x + 1, y + 1, z + 1
            };
            System.arraycopy(positions, 0, this.positions, this.voxelCount * POSITIONS_OFFSET, positions.length);
            float[] colors = new float[]{
                    v.getMaterial().r, v.getMaterial().g, v.getMaterial().b, v.getMaterial().a,
                    v.getMaterial().r, v.getMaterial().g, v.getMaterial().b, v.getMaterial().a,
                    v.getMaterial().r, v.getMaterial().g, v.getMaterial().b, v.getMaterial().a,
                    v.getMaterial().r, v.getMaterial().g, v.getMaterial().b, v.getMaterial().a,
                    v.getMaterial().r, v.getMaterial().g, v.getMaterial().b, v.getMaterial().a,
                    v.getMaterial().r, v.getMaterial().g, v.getMaterial().b, v.getMaterial().a,
                    v.getMaterial().r, v.getMaterial().g, v.getMaterial().b, v.getMaterial().a,
                    v.getMaterial().r, v.getMaterial().g, v.getMaterial().b, v.getMaterial().a,
            };
            System.arraycopy(colors, 0, this.colors, this.voxelCount * COLORS_OFFSET, colors.length);
        }

        private void addZpFace() {
            int[] face = new int[]{
                    1 + this.voxelCount * INDEX_OFFSET,
                    3 + this.voxelCount * INDEX_OFFSET,
                    5 + this.voxelCount * INDEX_OFFSET,
                    5 + this.voxelCount * INDEX_OFFSET,
                    3 + this.voxelCount * INDEX_OFFSET,
                    7 + this.voxelCount * INDEX_OFFSET,
            };
            System.arraycopy(face, 0, this.indices, this.faceCount * FACE_OFFSET, face.length);
            this.faceCount++;
        }

        private void addZnFace() {
            int[] face = new int[]{
                    0 + this.voxelCount * INDEX_OFFSET,
                    2 + this.voxelCount * INDEX_OFFSET,
                    4 + this.voxelCount * INDEX_OFFSET,
                    4 + this.voxelCount * INDEX_OFFSET,
                    2 + this.voxelCount * INDEX_OFFSET,
                    6 + this.voxelCount * INDEX_OFFSET,
            };
            System.arraycopy(face, 0, this.indices, this.faceCount * FACE_OFFSET, face.length);
            this.faceCount++;
        }

        private void addXpFace() {
            int[] face = new int[]{
                    5 + this.voxelCount * INDEX_OFFSET,
                    7 + this.voxelCount * INDEX_OFFSET,
                    4 + this.voxelCount * INDEX_OFFSET,
                    4 + this.voxelCount * INDEX_OFFSET,
                    7 + this.voxelCount * INDEX_OFFSET,
                    6 + this.voxelCount * INDEX_OFFSET,
            };
            System.arraycopy(face, 0, this.indices, this.faceCount * FACE_OFFSET, face.length);
            this.faceCount++;
        }

        private void addXnFace() {
            int[] face = new int[]{
                    1 + this.voxelCount * INDEX_OFFSET,
                    3 + this.voxelCount * INDEX_OFFSET,
                    0 + this.voxelCount * INDEX_OFFSET,
                    0 + this.voxelCount * INDEX_OFFSET,
                    3 + this.voxelCount * INDEX_OFFSET,
                    2 + this.voxelCount * INDEX_OFFSET,
            };
            System.arraycopy(face, 0, this.indices, this.faceCount * FACE_OFFSET, face.length);
            this.faceCount++;
        }

        private void addYpFace() {
            int[] face = new int[]{
                    3 + this.voxelCount * INDEX_OFFSET,
                    2 + this.voxelCount * INDEX_OFFSET,
                    7 + this.voxelCount * INDEX_OFFSET,
                    7 + this.voxelCount * INDEX_OFFSET,
                    2 + this.voxelCount * INDEX_OFFSET,
                    6 + this.voxelCount * INDEX_OFFSET,
            };
            System.arraycopy(face, 0, this.indices, this.faceCount * FACE_OFFSET, face.length);
            this.faceCount++;
        }

        private void addYnFace() {
            int[] face = new int[]{
                    1 + this.voxelCount * INDEX_OFFSET,
                    0 + this.voxelCount * INDEX_OFFSET,
                    5 + this.voxelCount * INDEX_OFFSET,
                    5 + this.voxelCount * INDEX_OFFSET,
                    0 + this.voxelCount * INDEX_OFFSET,
                    4 + this.voxelCount * INDEX_OFFSET,
            };
            System.arraycopy(face, 0, this.indices, this.faceCount * FACE_OFFSET, face.length);
            this.faceCount++;
        }

        private void incVoxelCount() {
            this.voxelCount++;
        }

        private Mesh build() {
            return new Mesh(this.positions, this.colors, this.indices);
        }
    }

    // TODO:
    //  Make this into a ChunkMesh that will contain one opaque mesh and one transparent mesh
    //  as well as face count and other useful information
    //  Try using Lists instead of arrays to reduce memory allocation. Launch with World.SIZE = 8 to compare
    //  Move MeshBuilder inside of ChunkMesh
    public static class MeshPair {
        private final List<Mesh> opaque;
        private final List<Mesh> transparent;

        private MeshPair() {
            this.opaque = new ArrayList<>();
            this.transparent = new ArrayList<>();
        }

        private void addOpaque(Mesh m) {
            this.opaque.add(m);
        }

        private void addTransparent(Mesh m) {
            this.transparent.add(m);
        }

        public List<Mesh> getOpaque() {
            return this.opaque;
        }

        public List<Mesh> getTransparent() {
            return this.transparent;
        }
    }

}
