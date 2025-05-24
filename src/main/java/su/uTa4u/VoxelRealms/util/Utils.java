package su.uTa4u.VoxelRealms.util;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import static java.awt.Font.TRUETYPE_FONT;

public final class Utils {
    private Utils() {
    }

    public static byte[] readBytes(String name) {
        try {
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name)) {
                if (is == null) throw new IOException();
                return is.readAllBytes();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read resource: " + name, e);
        }
    }

    // https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
    public static String readFile(String name) {
        return new String(readBytes(name));
    }

    public static Font readFont(String name) {
        try {
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("fonts/" + name)) {
                if (is == null) throw new IOException();
                return Font.createFont(TRUETYPE_FONT, is);
            }
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException("Could not read font: " + name, e);
        }
    }

    public static Font[] readFontCollection(String name) {
        try {
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("fonts/" + name)) {
                if (is == null) throw new IOException();
                return Font.createFonts(is);
            }
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException("Could not read font: " + name, e);
        }
    }

    public static int posToIndex(int x, int y, int z, int size) {
        return x + (z * size) + (y * size * size);
    }

    public static void swapInIntArray(int[] array, int from, int to, int size) {
        int[] temp = new int[size];
        from *= size;
        to *= size;
        System.arraycopy(array, from, temp, 0, size);
        System.arraycopy(array, to, array, from, size);
        System.arraycopy(temp, 0, array, to, size);
    }

    public static void swapInFloatArray(float[] array, int from, int to, int size) {
        float[] temp = new float[size];
        from *= size;
        to *= size;
        System.arraycopy(array, from, temp, 0, size);
        System.arraycopy(array, to, array, from, size);
        System.arraycopy(temp, 0, array, to, size);
    }

}
