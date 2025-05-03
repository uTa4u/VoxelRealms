package su.uTa4u.VoxelRealms.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLDebugMessageCallbackI;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.glDebugMessageCallback;
import static org.lwjgl.opengl.GLUtil.setupDebugMessageCallback;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Window {
    private final long handle;
    private final int glVerMajor;
    private final int glVerMinor;
    private int width;
    private int height;

    Window(String title, int initWidth, int initHeight, boolean isVsync) {
        if (!glfwInit()) throw new RuntimeException("Failed to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        this.handle = GLFW.glfwCreateWindow(
                initWidth,
                initHeight,
                title,
                NULL,
                NULL
        );
        if (this.handle == NULL) throw new RuntimeException("Failed to create the GLFW window");
        this.width = initWidth;
        this.height = initHeight;

        this.glVerMajor = glfwGetWindowAttrib(handle, GLFW_CONTEXT_VERSION_MAJOR);
        this.glVerMinor = glfwGetWindowAttrib(handle, GLFW_CONTEXT_VERSION_MINOR);
        System.out.println("[INFO] OpenGL Version " + this.glVerMajor + "." + this.glVerMinor);

        glfwMakeContextCurrent(handle);

        GL.createCapabilities();

        glDebugMessageCallback((GLDebugMessageCallbackI) setupDebugMessageCallback(), NULL);

        GLFWErrorCallback.createPrint(System.err).set();

        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true);
        });

        if (isVsync) {
            glfwSwapInterval(1);
        } else {
            glfwSwapInterval(0);
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidmode == null) throw new RuntimeException("Failed to get GLFW VideoMode");
        glfwSetWindowPos(
                handle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        glfwShowWindow(handle);
    }

    public long getHandle() {
        return this.handle;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(this.handle);
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public void tick() {
        glfwSwapBuffers(this.handle);
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(this.handle, keyCode) == GLFW_PRESS;
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(this.handle, title);
    }

    public void cleanup() {
        glfwFreeCallbacks(this.handle);
        glfwDestroyWindow(this.handle);

        glfwTerminate();
        GLFWErrorCallback ercb = glfwSetErrorCallback(null);
        if (ercb != null) ercb.free();
    }
}
