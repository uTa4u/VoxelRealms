package su.uTa4u.VoxelRealms.graphics.mesh;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import su.uTa4u.VoxelRealms.util.Utils;
import su.uTa4u.VoxelRealms.world.Chunk;
import su.uTa4u.VoxelRealms.world.Direction;
import su.uTa4u.VoxelRealms.world.Voxel;
import su.uTa4u.VoxelRealms.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NaiveMesher {
    private static final Map<Chunk, ChunkMesh> CACHE = new HashMap<>();

    public NaiveMesher() {
    }

    public List<ChunkMesh> createMeshForWorld(World world, Vector3fc camPos) {
        List<ChunkMesh> meshes = new ArrayList<>();
        for (int cy = 0; cy < World.SIZE; cy++) {
            for (int cx = 0; cx < World.SIZE; cx++) {
                for (int cz = 0; cz < World.SIZE; cz++) {
                    Chunk chunk = world.getChunk(cx, cy, cz);
                    if (chunk == null) continue;
                    if (CACHE.containsKey(chunk) && !chunk.getShouldRemesh()) {
                        meshes.add(CACHE.get(chunk));
                        continue;
                    }
                    Mesh opaque = new Mesh();
                    Mesh transparent = new Mesh();
                    for (int y = 0; y < Chunk.SIZE; ++y) {
                        for (int x = 0; x < Chunk.SIZE; ++x) {
                            for (int z = 0; z < Chunk.SIZE; ++z) {
                                Voxel voxel = chunk.getVoxel(x, y, z);
                                if (voxel == null) continue;
                                boolean isOpaque = voxel.isOpaque();
                                for (Direction d : Direction.DIRS) {
                                    Voxel other = world.getVoxel(x + d.dx, y + d.dy, z + d.dz, cx, cy, cz);
                                    if (!other.isOpaque() && (isOpaque || !other.isSameMaterial(voxel))) {
                                        int gx = cx * Chunk.SIZE + x;
                                        int gy = cy * Chunk.SIZE + y;
                                        int gz = cz * Chunk.SIZE + z;
                                        if (isOpaque) {
                                            opaque.addFace(d, voxel, gx, gy, gz, null);
                                        } else {
                                            transparent.addFace(d, voxel, gx, gy, gz, camPos);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    transparent.sort();
                    float d = camPos.distanceSquared(cx + Chunk.SIZE_HALF, cy + Chunk.SIZE_HALF, cz + Chunk.SIZE_HALF);
                    ChunkMesh chunkMesh = new ChunkMesh(opaque, transparent, d);
                    meshes.add(chunkMesh);
                    CACHE.put(chunk, chunkMesh);
                    // Forcefully disable CACHE for testing purposes
                    chunk.setShouldRemesh();
                }
            }
        }
        meshes.sort((a, b) -> Float.compare(a.getDistToCam(), b.getDistToCam()));
        return meshes;
    }
}
