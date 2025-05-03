package su.uTa4u.VoxelRealms;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Utils {
    private Utils() {
    }

    // https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
    public static String readFile(String name) {
        try {
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name)) {
                if (is == null) throw new IOException();
                return new String(is.readAllBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read resource: " + name, e);
        }
    }
}
