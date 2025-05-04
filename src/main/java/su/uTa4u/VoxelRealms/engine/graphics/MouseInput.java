package su.uTa4u.VoxelRealms.engine.graphics;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public final class MouseInput {
    private final Vector2f previousPos;
    private final Vector2f delta;

    private boolean firstMouse;
    private boolean leftButtonPressed;
    private boolean rightButtonPressed;

    public MouseInput(long windowHandle) {
        this.previousPos = new Vector2f(0, 0);
        this.delta = new Vector2f();
        this.leftButtonPressed = false;
        this.rightButtonPressed = false;
        this.firstMouse = true;

        glfwSetCursorPosCallback(windowHandle, (handle, xpos, ypos) -> {
            if (this.firstMouse) {
                this.previousPos.x = (float) xpos;
                this.previousPos.y = (float) ypos;
                this.firstMouse = false;
            }

            this.delta.y = (float) xpos - this.previousPos.x;
            this.delta.x = (float) ypos - this.previousPos.y;

            this.previousPos.x = (float) xpos;
            this.previousPos.y = (float) ypos;
        });
//        glfwSetCursorEnterCallback(windowHandle, (handle, entered) -> this.inWindow = entered);
        glfwSetMouseButtonCallback(windowHandle, (handle, button, action, mode) -> {
            this.leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            this.rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    public Vector2f getDelta() {
        return this.delta;
    }

    public boolean isLeftButtonPressed() {
        return this.leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return this.rightButtonPressed;
    }
}
