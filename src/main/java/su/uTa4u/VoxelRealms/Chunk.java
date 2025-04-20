package su.uTa4u.VoxelRealms;

public final class Chunk {
    public static final int SIZE = 32;

    public final Block[][][] blocks = new Block[SIZE][SIZE][SIZE];

    private final int chunkX;
    private final int chunkY;
    private final int chunkZ;

    public Chunk(int i, int j, int k) {
        this.chunkX = i * SIZE;
        this.chunkY = j * SIZE;
        this.chunkZ = k * SIZE;
        for (int y = 0; y < SIZE; ++y) {
            for (int x = 0; x < SIZE; ++x) {
                for (int z = 0; z < SIZE; ++z) {
                    blocks[y][x][z] = new Block(x, y, z,255, 0, 0);
                }
            }
        }
    }
}
