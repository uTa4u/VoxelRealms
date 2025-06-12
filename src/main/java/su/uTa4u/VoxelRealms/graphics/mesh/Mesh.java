package su.uTa4u.VoxelRealms.graphics.mesh;

import org.joml.Vector3f;
import su.uTa4u.VoxelRealms.world.Voxel;
import su.uTa4u.VoxelRealms.world.VoxelMaterial;

import java.util.*;
import java.util.stream.IntStream;

public final class Mesh {
    private final int[] positions;
    private final float[] colors;
    private final int[] indices;

    private Mesh(int[] positions, float[] colors, int[] indices) {
        this.positions = positions;
        this.colors = colors;
        this.indices = indices;
    }

    public int getVertexCount() {
        return this.indices.length;
    }

    public int[] getPositions() {
        return this.positions;
    }

    public float[] getColors() {
        return this.colors;
    }

    public int[] getIndices() {
        return this.indices;
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private static final int VERTEX_COUNT = 8;
        private static final int FACE_COUNT = 6;
        private static final int POSITIONS_COUNT = VERTEX_COUNT * 3;
        private static final int COLORS_COUNT = VERTEX_COUNT * 4;
        private static final int INDICES_COUNT = FACE_COUNT * 6;

        private final List<Cube> cubes;

        private Cube cube;

        private Builder() {
            this.cubes = new ArrayList<>();
        }

        void addVoxelData(Voxel v, int x, int y, int z, Vector3f camPos) {
            this.cube = new Cube(v.getMaterial(), x, y, z, camPos);
        }

        void addXpFace() {
            final int offset = 0;
            this.cube.indices[offset + 0] = 5 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 1] = 7 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 2] = 4 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 3] = 4 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 4] = 7 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 5] = 6 + this.cubes.size() * VERTEX_COUNT;
        }

        void addXnFace() {
            final int offset = 6;
            this.cube.indices[offset + 0] = 1 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 1] = 3 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 2] = 0 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 3] = 0 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 4] = 3 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 5] = 2 + this.cubes.size() * VERTEX_COUNT;
        }

        void addYpFace() {
            final int offset = 12;
            this.cube.indices[offset + 0] = 3 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 1] = 2 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 2] = 7 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 3] = 7 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 4] = 2 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 5] = 6 + this.cubes.size() * VERTEX_COUNT;
        }

        void addYnFace() {
            final int offset = 18;
            this.cube.indices[offset + 0] = 1 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 1] = 0 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 2] = 5 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 3] = 5 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 4] = 0 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 5] = 4 + this.cubes.size() * VERTEX_COUNT;
        }

        void addZpFace() {
            final int offset = 24;
            this.cube.indices[offset + 0] = 1 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 1] = 3 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 2] = 5 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 3] = 5 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 4] = 3 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 5] = 7 + this.cubes.size() * VERTEX_COUNT;
        }

        void addZnFace() {
            final int offset = 30;
            this.cube.indices[offset + 0] = 0 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 1] = 2 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 2] = 4 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 3] = 4 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 4] = 2 + this.cubes.size() * VERTEX_COUNT;
            this.cube.indices[offset + 5] = 6 + this.cubes.size() * VERTEX_COUNT;
        }

        void addCube() {
            this.cubes.add(this.cube);
        }

        Mesh build(boolean isOpaque) {
            final int cubeCount = this.cubes.size();
            int[] positions = new int[cubeCount * POSITIONS_COUNT];
            float[] colors = new float[cubeCount * COLORS_COUNT];
            int[] indices = new int[cubeCount * INDICES_COUNT];
            float[] distances = new float[cubeCount * FACE_COUNT];
            int index = 0;
            for (Cube c : this.cubes) {
                System.arraycopy(c.positions, 0, positions, index * POSITIONS_COUNT, POSITIONS_COUNT);
                System.arraycopy(c.colors, 0, colors, index * COLORS_COUNT, COLORS_COUNT);
                System.arraycopy(c.indices, 0, indices, index * INDICES_COUNT, INDICES_COUNT);
                System.arraycopy(c.distances, 0, distances, index * FACE_COUNT, FACE_COUNT);
                index++;
            }

            int[] sortedIndices = new int[indices.length];
            if (!isOpaque) {
                int[] fromIndices = IntStream.range(0, cubeCount * FACE_COUNT).toArray();
                int[] toIndices = Arrays.stream(fromIndices)
                        .boxed()
                        .sorted((i, j) -> Float.compare(distances[j], distances[i]))
                        .mapToInt(v -> v)
                        .toArray();
                for (int i = 0; i < fromIndices.length; i++) {
                    int from = fromIndices[i] * 6;
                    int to = toIndices[i] * 6;
                    System.arraycopy(indices, from, sortedIndices, to, 6);
                }
            } else {
                sortedIndices = indices;
            }
            sortedIndices = Arrays.stream(sortedIndices).filter(v -> v != -1).toArray();
//            sortedIndices = Arrays.stream(indices).filter(v -> v != -1).toArray();

            return new Mesh(positions, colors, sortedIndices);
        }

        private static class Cube {
            private final int[] positions;
            private final float[] colors;
            private final int[] indices;
            private final float[] distances;

            private Cube(VoxelMaterial m, int x, int y, int z, Vector3f camPos) {
                this.positions = new int[]{
                        x, y, z,
                        x, y, z + 1,
                        x, y + 1, z,
                        x, y + 1, z + 1,
                        x + 1, y, z,
                        x + 1, y, z + 1,
                        x + 1, y + 1, z,
                        x + 1, y + 1, z + 1,
                };
                this.colors = new float[]{
                        m.r, m.g, m.b, m.a,
                        m.r, m.g, m.b, m.a,
                        m.r, m.g, m.b, m.a,
                        m.r, m.g, m.b, m.a,
                        m.r, m.g, m.b, m.a,
                        m.r, m.g, m.b, m.a,
                        m.r, m.g, m.b, m.a,
                        m.r, m.g, m.b, m.a,
                };
                this.indices = IntStream.generate(() -> -1).limit(INDICES_COUNT).toArray();
                this.distances = new float[]{
                        camPos.distanceSquared(x + 1.0f, y + 0.5f, z + 0.5f),
                        camPos.distanceSquared(x + 0.0f, y + 0.5f, z + 0.5f),
                        camPos.distanceSquared(x + 0.5f, y + 1.0f, z + 0.5f),
                        camPos.distanceSquared(x + 0.5f, y + 0.0f, z + 0.5f),
                        camPos.distanceSquared(x + 0.5f, y + 0.5f, z + 1.0f),
                        camPos.distanceSquared(x + 0.5f, y + 0.5f, z + 0.0f),
                };
            }
        }
    }
}
