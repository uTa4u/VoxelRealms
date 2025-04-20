package su.uTa4u.VoxelRealms;

public final class World {
    public static final int SIZE = 256;
    public static final int SIZE_SQUARED = SIZE * SIZE;
    public static final int SIZE_QUBED = SIZE * SIZE * SIZE;

    public final Chunk[] chunks = new Chunk[SIZE_QUBED];

    public World() {
        this.chunks[0] = new Chunk(0, 0, 0);
    }
}
