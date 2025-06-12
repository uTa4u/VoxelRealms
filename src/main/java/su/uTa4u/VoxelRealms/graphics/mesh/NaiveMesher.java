package su.uTa4u.VoxelRealms.graphics.mesh;

import org.joml.Vector3f;
import su.uTa4u.VoxelRealms.world.Chunk;
import su.uTa4u.VoxelRealms.world.Voxel;
import su.uTa4u.VoxelRealms.world.World;

import java.util.ArrayList;
import java.util.List;

public final class NaiveMesher {
    public NaiveMesher() {
    }

    public List<ChunkMesh> createMeshForWorld(World world, Vector3f camPos) {
        List<ChunkMesh> meshes = new ArrayList<>();
        for (int cy = 0; cy < World.SIZE; cy++) {
            for (int cx = 0; cx < World.SIZE; cx++) {
                for (int cz = 0; cz < World.SIZE; cz++) {
                    Chunk chunk = world.getChunk(cx, cy, cz);
                    if (chunk == null) continue;
                    Mesh.Builder opaque = Mesh.builder();
                    Mesh.Builder transparent = Mesh.builder();
                    for (int y = 0; y < Chunk.SIZE; ++y) {
                        for (int x = 0; x < Chunk.SIZE; ++x) {
                            for (int z = 0; z < Chunk.SIZE; ++z) {
                                Voxel voxel = chunk.getVoxel(x, y, z);
                                if (voxel == null) continue;
                                boolean isOpaque = voxel.isOpaque();
                                Voxel other;
                                Mesh.Builder builder = isOpaque ? opaque : transparent;
                                builder.addVoxelData(voxel, cx * Chunk.SIZE + x, cy * Chunk.SIZE + y, cz * Chunk.SIZE + z, camPos);
                                other = world.getVoxel(x + 1, y, z, cx, cy, cz);
                                if (!other.isOpaque()) {
                                    if (!isOpaque) {
                                        if (!voxel.isSameMaterial(other)) {
                                            builder.addXpFace();
                                        }
                                    } else {
                                        builder.addXpFace();
                                    }
                                }
                                other = world.getVoxel(x - 1, y, z, cx, cy, cz);
                                if (!other.isOpaque()) {
                                    if (!isOpaque) {
                                        if (!voxel.isSameMaterial(other)) {
                                            builder.addXnFace();
                                        }
                                    } else {
                                        builder.addXnFace();
                                    }
                                }
                                other = world.getVoxel(x, y + 1, z, cx, cy, cz);
                                if (!other.isOpaque()) {
                                    if (!isOpaque) {
                                        if (!voxel.isSameMaterial(other)) {
                                            builder.addYpFace();
                                        }
                                    } else {
                                        builder.addYpFace();
                                    }
                                }
                                other = world.getVoxel(x, y - 1, z, cx, cy, cz);
                                if (!other.isOpaque()) {
                                    if (!isOpaque) {
                                        if (!voxel.isSameMaterial(other)) {
                                            builder.addYnFace();
                                        }
                                    } else {
                                        builder.addYnFace();
                                    }
                                }
                                other = world.getVoxel(x, y, z + 1, cx, cy, cz);
                                if (!other.isOpaque()) {
                                    if (!isOpaque) {
                                        if (!voxel.isSameMaterial(other)) {
                                            builder.addZpFace();
                                        }
                                    } else {
                                        builder.addZpFace();
                                    }
                                }
                                other = world.getVoxel(x, y, z - 1, cx, cy, cz);
                                if (!other.isOpaque()) {
                                    if (!isOpaque) {
                                        if (!voxel.isSameMaterial(other)) {
                                            builder.addZnFace();
                                        }
                                    } else {
                                        builder.addZnFace();
                                    }
                                }
                                builder.addCube();
                            }
                        }
                    }
                    float d = camPos.distanceSquared(cx + Chunk.SIZE_HALF, cy + Chunk.SIZE_HALF, cz + Chunk.SIZE_HALF);
                    meshes.add(new ChunkMesh(opaque.build(true), transparent.build(false), d));
                }
            }
        }
        meshes.sort((a, b) -> Float.compare(a.getDistToCam(), b.getDistToCam()));
        return meshes;
    }
}
