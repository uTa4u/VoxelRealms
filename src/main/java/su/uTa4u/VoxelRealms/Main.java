package su.uTa4u.VoxelRealms;

import org.lwjgl.system.Configuration;
import su.uTa4u.VoxelRealms.engine.Engine;
import su.uTa4u.VoxelRealms.engine.mesh.NaiveMesher;
import su.uTa4u.VoxelRealms.world.World;

public final class Main {
    private static final String TITLE = "Voxel Realms";
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
    private static final int TARGET_FPS = Integer.MAX_VALUE;
    private static final int TARGET_TPS = 20;

    public static void main(String[] args) {
        Configuration.STACK_SIZE.set(1024*8);

        Engine engine = new Engine(TITLE, DEFAULT_WIDTH, DEFAULT_HEIGHT, TARGET_FPS, TARGET_TPS);

        World world = new World();
        NaiveMesher mesher = new NaiveMesher();
        engine.getRenderer().addMeshes(mesher.createMeshForWorld(world));

        engine.run();
    }
}
