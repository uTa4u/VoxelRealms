package su.uTa4u.VoxelRealms.graphics.mesh;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.joml.*;
import su.uTa4u.VoxelRealms.logger.Logger;
import su.uTa4u.VoxelRealms.profiler.Profiler;
import su.uTa4u.VoxelRealms.world.Direction;
import su.uTa4u.VoxelRealms.world.Voxel;

import java.util.*;
import java.util.stream.IntStream;

public final class Mesh {
    private static final int INDICES_PER_FACE = 6;
    private static final EnumMap<Direction, IntList> FACE_POS_OFFSETS = new EnumMap<>(Direction.class);

    private final List<Vector3ic> positions;
    private final Object2IntMap<Vector3ic> positionIndices;
    private List<int[]> indices;
    private final List<Vector4fc> colors;
    private final FloatList camDistances;

    public Mesh() {
        this.positions = new ArrayList<>();
        this.positionIndices = new Object2IntOpenHashMap<>();
        this.indices = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.camDistances = new FloatArrayList();
    }

    void addFace(Direction dir, Voxel v, int x, int y, int z, Vector3fc camPos) {
        final int missingIndex = this.positionIndices.defaultReturnValue();
        IntList posOffsets = FACE_POS_OFFSETS.get(dir);
        int[] indices = new int[4];
        for (int i = 0; i < 4; i++) {
            Vector4fc color = v.getMaterial().vec;
            Vector3i vec = new Vector3i(
                    x + posOffsets.getInt(0 + i * 3),
                    y + posOffsets.getInt(1 + i * 3),
                    z + posOffsets.getInt(2 + i * 3)
            );
            int index = this.positionIndices.getInt(vec);
            if (index == missingIndex || this.colors.get(index) != color) {
                int newIndex = this.positions.size();
                this.positions.add(vec);
                this.positionIndices.put(vec, newIndex);
                this.colors.add(color);
                indices[i] = newIndex;
            } else {
                indices[i] = index;
            }
        }
        this.indices.add(new int[]{indices[0], indices[1], indices[3], indices[3], indices[1], indices[2]});

        if (camPos != null) {
            Vector3ic v0 = this.positions.get(indices[0]);
            Vector3ic v2 = this.positions.get(indices[2]);
            this.camDistances.add(camPos.distanceSquared(
                    (v0.x() + v2.x()) / 2f,
                    (v0.y() + v2.y()) / 2f,
                    (v0.z() + v2.z()) / 2f
            ));
        }
    }

    void sort() {
        if (this.indices.isEmpty()) return;
        this.indices = IntStream.range(0, this.camDistances.size()).boxed()
                .sorted(Comparator.comparingDouble(this.camDistances::getFloat).reversed())
                .map(this.indices::get)
                .toList();
    }

    public int[] getPositions() {
        int[] ret = new int[this.positions.size() * 3];
        for (int i = 0; i < this.positions.size(); i++) {
            Vector3ic vec = this.positions.get(i);
            ret[0 + i * 3] = vec.x();
            ret[1 + i * 3] = vec.y();
            ret[2 + i * 3] = vec.z();
        }
        return ret;
    }

    public int[] getIndices() {
        int[] ret = new int[this.indices.size() * INDICES_PER_FACE];
        for (int i = 0; i < this.indices.size(); i++) {
            System.arraycopy(this.indices.get(i), 0, ret, i * INDICES_PER_FACE, INDICES_PER_FACE);
        }
        return ret;
    }

    public float[] getColors() {
        float[] ret = new float[this.colors.size() * 4];
        for (int i = 0; i < this.colors.size(); i++) {
            Vector4fc vec = this.colors.get(i);
            ret[0 + i * 4] = vec.x();
            ret[1 + i * 4] = vec.y();
            ret[2 + i * 4] = vec.z();
            ret[3 + i * 4] = vec.w();
        }
        return ret;
    }

    public int getVertexCount() {
        return this.indices.size() * INDICES_PER_FACE;
    }

    static {
        FACE_POS_OFFSETS.put(Direction.EAST, IntList.of(1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1));
        FACE_POS_OFFSETS.put(Direction.WEST, IntList.of(0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0));
        FACE_POS_OFFSETS.put(Direction.UP, IntList.of(0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0));
        FACE_POS_OFFSETS.put(Direction.DOWN, IntList.of(0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1));
        FACE_POS_OFFSETS.put(Direction.NORTH, IntList.of(0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0));
        FACE_POS_OFFSETS.put(Direction.SOUTH, IntList.of(0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1));
    }
}
