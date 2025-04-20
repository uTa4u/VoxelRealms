package su.uTa4u.VoxelRealms;

import org.joml.Vector2f;
import org.joml.Vector3f;

public final class Player {
    public Vector3f pos;
    public Vector2f cam;

    public Player() {
        pos = new Vector3f(0, 1, 5);
        cam = new Vector2f(0, 0);
    }
}
