package su.uTa4u.VoxelRealms;

import org.lwjgl.system.Configuration;
import su.uTa4u.VoxelRealms.logger.Logger;

public final class Main {
    private static final Logger LOGGER = Logger.create();
    private static final String TITLE = "Voxel Realms";
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
    private static final int TARGET_FPS = Integer.MAX_VALUE;
    private static final int TARGET_TPS = 20;

    public static final boolean WIREFRAME_MODE = false;
    public static final boolean DEBUG = false;
    public static final boolean IS_IN_IDE = Boolean.getBoolean("isInIDE");

    public static void main(String[] args) {
        Configuration.STACK_SIZE.set(1024*8);

        Engine engine = new Engine(TITLE, DEFAULT_WIDTH, DEFAULT_HEIGHT, TARGET_FPS, TARGET_TPS);

        engine.run();
    }
}
