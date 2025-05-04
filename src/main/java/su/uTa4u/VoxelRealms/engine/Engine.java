package su.uTa4u.VoxelRealms.engine;

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
        float msPerTick = 1000.0f / this.targetTps;
        float deltaTick = 0;
        int tps = 0;

        while (!this.window.shouldClose()) {
            this.window.pollEvents();

            long current = System.currentTimeMillis();
            long dt = current - previous;
            deltaFrame += dt / msPerFrame;
            deltaTick += dt / msPerTick;
            previous = current;

            if (this.isVsync || deltaFrame >= 1.0f) {
                // Handle input
            }

            if (deltaTick >= 1.0f) {
                // Handle tick
                tps++;
                deltaTick--;
            }

            if (this.isVsync || deltaFrame >= 1.0f) {
                this.renderer.render();
                fps++;
                deltaFrame--;
                this.window.tick();
            }

            if (current - secTimer >= 1000L) {
                secTimer += 1000L;
                this.window.setTitle("FPS: " + fps + " TPS: " + tps);
                tps = 0;
                fps = 0;
            }
        }

        this.window.cleanup();
    }

    void resize(int width, int height) {
        this.window.setSize(width, height);
        this.renderer.projection.update(width, height);
    }

}
