package su.uTa4u.VoxelRealms.graphics;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public final class MouseInput {
    private final Vector2f currentPos;
    private final Vector2f previousPos;
    private final Vector2f delta;

    private boolean firstMouse;
    private boolean leftButtonPressed;
    private boolean rightButtonPressed;

    public MouseInput(long windowHandle) {
        this.currentPos = new Vector2f(0, 0);
        this.previousPos = new Vector2f(0, 0);
        this.delta = new Vector2f(0, 0);
        this.leftButtonPressed = false;
        this.rightButtonPressed = false;
        this.firstMouse = true;

        glfwSetCursorPosCallback(windowHandle, (handle, xpos, ypos) -> {
            this.currentPos.x = (float) xpos;
            this.currentPos.y = (float) ypos;
        });
//        glfwSetCursorEnterCallback(windowHandle, (handle, entered) -> this.inWindow = entered);
        glfwSetMouseButtonCallback(windowHandle, (handle, button, action, mode) -> {
            this.leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            this.rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    public Vector2f getDelta() {
        if (this.firstMouse) {
            this.previousPos.x = this.currentPos.x;
            this.previousPos.y = this.currentPos.y;
            this.firstMouse = false;
        }

        this.delta.y = this.currentPos.x - this.previousPos.x;
        this.delta.x = this.currentPos.y - this.previousPos.y;

        this.previousPos.x = this.currentPos.x;
        this.previousPos.y = this.currentPos.y;

        return this.delta;
    }

    public boolean isLeftButtonPressed() {
        return this.leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return this.rightButtonPressed;
    }
}
