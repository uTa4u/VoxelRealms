package su.uTa4u.VoxelRealms.engine;

import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLDebugMessageCallbackI;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import su.uTa4u.VoxelRealms.*;

import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.glDebugMessageCallback;
import static org.lwjgl.opengl.GLUtil.setupDebugMessageCallback;
import static org.lwjgl.system.MemoryStack.stackPush;

public final class Engine {
    public static final Engine INSTANCE = new Engine();

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private long window;

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) throw new RuntimeException("Failed to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = GLFW.glfwCreateWindow(DEFAULT_WIDTH, DEFAULT_HEIGHT, VoxelRealms.TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) throw new RuntimeException("Failed to create the GLFW window");

        int glVerMajor = glfwGetWindowAttrib(window, GLFW_CONTEXT_VERSION_MAJOR);
        int glVerMinor = glfwGetWindowAttrib(window, GLFW_CONTEXT_VERSION_MINOR);
        System.out.println("[INFO] OpenGL Version " + glVerMajor + "." + glVerMinor);

        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        glDebugMessageCallback((GLDebugMessageCallbackI) setupDebugMessageCallback(), MemoryUtil.NULL);

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true);
        });

        Vector2i size = getWindowSize();

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidmode != null;
        glfwSetWindowPos(
                window,
                (vidmode.width() - size.x) / 2,
                (vidmode.height() - size.y) / 2
        );

        glfwSwapInterval(1);

        glfwShowWindow(window);
    }

    private void loop() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        World world = new World();
        Player player = new Player();

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.INSTANCE.drawWorldForPlayer(world, player);

            glfwSwapBuffers(window);

            glfwPollEvents();
        }
    }

    public void run() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public Vector2i getWindowSize() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetWindowSize(window, width, height);
            return new Vector2i(width.get(0), height.get(0));
        }
    }

    private Engine() {
    }
}
