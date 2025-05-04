package su.uTa4u.VoxelRealms;

import su.uTa4u.VoxelRealms.engine.Engine;

public final class Main {
    private static final String TITLE = "Voxel Realms";
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
    private static final int TARGET_FPS = Integer.MAX_VALUE;
    private static final int TARGET_TPS = 20;

    public static void main(String[] args) {
        Engine engine = new Engine(TITLE, DEFAULT_WIDTH, DEFAULT_HEIGHT, TARGET_FPS, TARGET_TPS);
        engine.run();
    }
}
