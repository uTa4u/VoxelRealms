package su.uTa4u.VoxelRealms;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;

public final class Face {
    public final Vector3i a;
    public final Vector3i b;
    public final Vector3i c;
    public final Vector3i d;

    public Face(Vector3i a, Vector3i b, Vector3i c, Vector3i d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
}
