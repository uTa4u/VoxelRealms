package su.uTa4u.VoxelRealms.profiler;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import su.uTa4u.VoxelRealms.logger.Logger;

// TODO: make this autocloseable?
public final class Profiler {
    private static final double MILLION = 1000000;
    private final Object2LongMap<String> profiles;
    private String currentProfileName;
    private long currentProfileStart;

    private Profiler() {
        this.profiles = new Object2LongOpenHashMap<>();
    }

    public static Profiler create() {
        return new Profiler();
    }

    public void push(String name) {
        this.currentProfileName = name;
        this.currentProfileStart = System.nanoTime();
    }

    public void pop() {
        this.pop(false);
    }

    public void pop(boolean accumulate) {
        final long duration = System.nanoTime() - this.currentProfileStart;
        if (accumulate && this.profiles.containsKey(this.currentProfileName)) {
            this.profiles.put(this.currentProfileName, this.profiles.getLong(this.currentProfileName) + duration);
        } else {
            this.profiles.put(this.currentProfileName, duration);
        }
    }

    public void clear() {
        this.profiles.clear();
    }

    public void logAll(Logger logger) {
        this.profiles.forEach((k, v) -> logger.info(k + " took " + v / MILLION + "ms"));
    }

    public void logLast(Logger logger) {
        logger.info(this.currentProfileName + " took " + this.profiles.getLong(this.currentProfileName) / MILLION + "ms");
    }

    public void log(Logger logger, String name) {
        logger.info(name + " took " + this.profiles.getLong(name) / MILLION + "ms");
    }

}
