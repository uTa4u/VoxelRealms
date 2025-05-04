package su.uTa4u.VoxelRealms;

import org.joml.Matrix4f;

public final class Projection {
    private static final float FOV = (float) Math.toRadians(60.0);
    private static final float Z_FAR = 1000.0f;
    private static final float Z_NEAR = 0.01f;

    private final Matrix4f projMatrix;

    public Projection(int width, int height) {
        this.projMatrix = new Matrix4f();
        this.update(width, height);
    }

    public void update(int width, int height) {
        this.projMatrix.setPerspective(FOV, (float) width / (float) height, Z_NEAR, Z_FAR);
    }

    public Matrix4f getMatrix() {
        return this.projMatrix;
    }

}
