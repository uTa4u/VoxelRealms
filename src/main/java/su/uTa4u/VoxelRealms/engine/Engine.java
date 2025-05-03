package su.uTa4u.VoxelRealms.engine;

import static org.lwjgl.opengl.GL11.*;

public final class Engine {
    private final Window window;
    private final Renderer renderer;

    private final boolean isVsync;
    private final int targetFps;
    private final int targetTps;

    public Engine(String title, int initWidth, int initHeight, int targetFps, int targetTps) {
        this.isVsync = targetFps <= 0;
        this.window = new Window(title, initWidth, initHeight, this.isVsync);
        this.renderer = new Renderer();
        this.targetFps = targetFps;
        this.targetTps = targetTps;
    }

    public void run() {
        long start = System.currentTimeMillis();
        long previous = start;
        float msPerFrame = this.targetFps > 0 ? 1000.0f / this.targetFps : 0;
        float msPerTick = 1000.0f / this.targetTps;
        float deltaTick = 0;
        float deltaFrame = 0;
        long tickTime = start;
        int fps = 0;
        int tps = 0;
        long timer = start;

        while (!window.shouldClose()) {
            window.pollEvents();

            long current = System.currentTimeMillis();
            deltaFrame += (current - previous) / msPerFrame;
            deltaTick += (current - previous) / msPerTick;
            previous = current;

            if (this.isVsync || deltaFrame >= 1.0f) {
                // Handle input
            }

            if (deltaTick >= 1.0f) {
                long dt = current - tickTime;
                // Handle tick
                tps++;
                deltaTick--;
                tickTime = current;
            }

            if (this.isVsync || deltaFrame >= 1.0f) {
                renderer.render();
                fps++;
                deltaFrame--;
                window.tick();
            }

            if (current - timer > 1000) {
                timer += 1000;
                this.window.setTitle("FPS: " + fps + " TPS: " + tps);
                tps = 0;
                fps = 0;
            }
        }

        window.cleanup();
    }

}
