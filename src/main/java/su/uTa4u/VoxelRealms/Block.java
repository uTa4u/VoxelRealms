package su.uTa4u.VoxelRealms;

import org.joml.Vector3i;

public final class Block {
    public final byte x;
    public final byte y;
    public final byte z;
    public final Vector3i v;

    public final byte r;
    public final byte g;
    public final byte b;

    public Block(int x, int y, int z, int r, int g, int b) {
        this.x = (byte) x;
        this.y = (byte) y;
        this.z = (byte) z;
        this.v = new Vector3i(this.x, this.y, this.z);
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
    }

    public Face[] getBlockFaces() {
        Face[] faces = new Face[6];
        faces[0] = new Face(
                (new Vector3i(this.v)).add(0, 0, 0),
                (new Vector3i(this.v)).add(1, 0, 0),
                (new Vector3i(this.v)).add(0, 1, 0),
                (new Vector3i(this.v)).add(1, 1, 0)
        );
        faces[1] = new Face(
                (new Vector3i(this.v)).add(0, 0, 1),
                (new Vector3i(this.v)).add(1, 0, 1),
                (new Vector3i(this.v)).add(0, 1, 1),
                (new Vector3i(this.v)).add(1, 1, 1)
        );
        faces[2] = new Face(
                (new Vector3i(this.v)).add(0, 0, 0),
                (new Vector3i(this.v)).add(0, 0, 1),
                (new Vector3i(this.v)).add(0, 1, 0),
                (new Vector3i(this.v)).add(0, 1, 1)
        );
        faces[3] = new Face(
                (new Vector3i(this.v)).add(1, 0, 0),
                (new Vector3i(this.v)).add(1, 0, 1),
                (new Vector3i(this.v)).add(1, 1, 0),
                (new Vector3i(this.v)).add(1, 1, 1)
        );
        faces[4] = new Face(
                (new Vector3i(this.v)).add(0, 0, 0),
                (new Vector3i(this.v)).add(1, 0, 0),
                (new Vector3i(this.v)).add(0, 0, 1),
                (new Vector3i(this.v)).add(1, 0, 1)
        );
        faces[5] = new Face(
                (new Vector3i(this.v)).add(0, 1, 0),
                (new Vector3i(this.v)).add(1, 1, 0),
                (new Vector3i(this.v)).add(0, 1, 1),
                (new Vector3i(this.v)).add(1, 1, 1)
        );
        return faces;
    }
}
