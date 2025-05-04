package su.uTa4u.VoxelRealms.engine;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public final class MouseInput {
    private final Vector2f currentPos;
    private final Vector2f previousPos;
    private final Vector2f delta;

    private boolean inWindow;

    private boolean leftButtonPressed;
    private boolean rightButtonPressed;

    public MouseInput(long windowHandle) {
        this.previousPos = new Vector2f(-1, -1);
        this.currentPos = new Vector2f();
        this.delta = new Vector2f();
        this.leftButtonPressed = false;
        this.rightButtonPressed = false;
        this.inWindow = false;

        glfwSetCursorPosCallback(windowHandle, (handle, xpos, ypos) -> {
            this.currentPos.x = (float) xpos;
            this.currentPos.y = (float) ypos;
        });
        glfwSetCursorEnterCallback(windowHandle, (handle, entered) -> this.inWindow = entered);
        glfwSetMouseButtonCallback(windowHandle, (handle, button, action, mode) -> {
            this.leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            this.rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    public void input() {
        this.delta.x = 0;
        this.delta.y = 0;
        if (this.previousPos.x > 0 && this.previousPos.y > 0 && this.inWindow) {
            double dx = this.currentPos.x - this.previousPos.x;
            double dy = this.currentPos.y - this.previousPos.y;
            if (dx != 0) {
                this.delta.y = (float) dx;
            }
            if (dy != 0) {
                this.delta.x = (float) dy;
            }
        }
        this.previousPos.x = this.currentPos.x;
        this.previousPos.y = this.currentPos.y;
    }

    public Vector2f getCurrentPos() {
        return this.currentPos;
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
