package su.uTa4u.VoxelRealms;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NaiveMesher {
    public static final NaiveMesher INSTANCE = new NaiveMesher();

    public List<Face> generateMesh(World world) {
        List<Face> mesh = new ArrayList<>();
        for (Chunk chunk : world.chunks) {
            if (chunk == null) continue;
            for (int y = 0; y < Chunk.SIZE; ++y) {
                for (int x = 0; x < Chunk.SIZE; ++x) {
                    for (int z = 0; z < Chunk.SIZE; ++z) {
                        mesh.addAll(Arrays.asList(chunk.blocks[y][x][z].getBlockFaces()));
                    }
                }
            }
        }
        return mesh;
    }

    private NaiveMesher() {
    }
}
