package su.uTa4u.VoxelRealms.world;

public enum Direction {
    EAST(1, 0, 0),
    WEST(-1, 0, 0),
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1);

    public static final Direction[] DIRS = values();

    public final int dx;
    public final int dy;
    public final int dz;

    Direction(int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }
}
