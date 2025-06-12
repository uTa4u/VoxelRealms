package su.uTa4u.VoxelRealms;

import su.uTa4u.VoxelRealms.graphics.Renderer;
import su.uTa4u.VoxelRealms.graphics.Window;

public final class Engine {
    private final Window window;
    private final Renderer renderer;

    private final boolean isVsync;
    private final int targetFps;
    private final int targetTps;

    public Engine(String title, int initWidth, int initHeight, int targetFps, int targetTps) {
        this.isVsync = targetFps <= 0;
        this.window = new Window(this, title, initWidth, initHeight, this.isVsync);
        this.renderer = new Renderer(this.window);
        this.targetFps = targetFps;
        this.targetTps = targetTps;
    }

    public void run() {
        long previous = System.currentTimeMillis();
        long secTimer = System.currentTimeMillis();

        float msPerFrame = !this.isVsync ? 1000.0f / this.targetFps : 0;
        float deltaFrame = 0;
        int fps = 0;
        int renderFps = 0;
        
        float msPerTick = 1000.0f / this.targetTps;
        float deltaTick = 0;
        int tps = 0;
        int renderTps = 0;

        while (!this.window.shouldClose()) {
            this.window.pollEvents();

            long current = System.currentTimeMillis();
            long dt = current - previous;
            deltaFrame += dt / msPerFrame;
            deltaTick += dt / msPerTick;
            previous = current;

            if (this.isVsync || deltaFrame >= 1.0f) {
                // Handle input
                this.renderer.camera.handleWindowInput(this.window, dt / 1000.0f);
            }

            if (deltaTick >= 1.0f) {
                // Handle tick
                tps++;
                deltaTick--;
            }

            if (this.isVsync || deltaFrame >= 1.0f) {
                this.renderer.render();
                this.renderer.renderText("FPS: " + renderFps + "\nTPS: " + renderTps, 10, this.window.getHeight() - 10, 1.0f, 1.0f, 1.0f, 1.0f);

                fps++;
                deltaFrame--;
                this.window.tick();
            }

            if (current - secTimer >= 1000L) {
                secTimer += 1000L;
                renderTps = tps;
                renderFps = fps;
                tps = 0;
                fps = 0;
            }
        }

        this.window.cleanup();
    }

    public void resize(int width, int height) {
        this.window.setSize(width, height);
        this.renderer.projection.update(width, height);
    }

    public Renderer getRenderer() {
        return this.renderer;
    }

}
