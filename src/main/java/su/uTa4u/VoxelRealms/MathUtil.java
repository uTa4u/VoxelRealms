package su.uTa4u.VoxelRealms;

import org.joml.Vector3f;

public final class MathUtil {

    public static float[] foo(Vector3f[] vertices) {
        float[] ret = new float[vertices.length * 3];
        for (int i = 0; i < vertices.length; ++i) {
            ret[(i * 3)] = vertices[i].x;
            ret[(i * 3) + 1] = vertices[i].y;
            ret[(i * 3) + 2] = vertices[i].z;
        }
        return ret;
    }

    private MathUtil() {
    }
}
