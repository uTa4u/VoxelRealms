package su.uTa4u.VoxelRealms.world;

public final class World {
    public static final int SIZE = 32;

    public final Chunk[][][] chunks = new Chunk[SIZE][SIZE][SIZE];

    public World() {
        this.chunks[0][0][0] = new Chunk(-1, -1, -1);
    }
}
