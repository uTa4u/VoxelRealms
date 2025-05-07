package su.uTa4u.VoxelRealms.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.uTa4u.VoxelRealms.util.Utils;

public final class World {
    public static final int SIZE = 4;

    private final Chunk[] chunks = new Chunk[SIZE * SIZE * SIZE];

    public World() {
        for (int cy = 0; cy < SIZE; cy++) {
            for (int cx = 0; cx < SIZE; cx++) {
                for (int cz = 0; cz < SIZE; cz++) {
                    this.setChunk(new Chunk(), cx, cy, cz);
                }
            }
        }
    }

    // m - middle chunk coords
    // l - coords local to middle chunk
    public Voxel getVoxel(int lx, int ly, int lz, int mcx, int mcy, int mcz) {
        int gx = lx + mcx * Chunk.SIZE;
        int gy = ly + mcy * Chunk.SIZE;
        int gz = lz + mcz * Chunk.SIZE;
        Chunk c = this.getChunk(gx / Chunk.SIZE, gy / Chunk.SIZE, gz / Chunk.SIZE);
        return c == null ? Voxel.EMPTY : c.getVoxel(gx % Chunk.SIZE, gy % Chunk.SIZE, gz % Chunk.SIZE);
    }

    // TODO: center chunks around (0, 0) and allow negative indexes
    @Nullable
    public Chunk getChunk(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0) return null;
        if (x >= SIZE || y >= SIZE || z >= SIZE) return null;
        return this.chunks[Utils.posToIndex(x, y, z, SIZE)];
    }

    public void setChunk(Chunk chunk, int x, int y, int z) {
        this.chunks[Utils.posToIndex(x, y, z, SIZE)] = chunk;
    }
}
