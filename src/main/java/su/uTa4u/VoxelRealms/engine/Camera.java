package su.uTa4u.VoxelRealms.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public final class Camera {
    private static final float MOVEMENT_SPEED = 5.0f;
    private static final float MOUSE_SENSITIVITY = 0.1f;

    private final Vector3f up;
    private final Vector3f right;

    private final Vector3f direction;
    private final Vector3f position;
    private final Vector2f rotation;
    private final Matrix4f viewMatrix;

    public Camera() {
        this.up = new Vector3f();
        this.right = new Vector3f();

        this.direction = new Vector3f();
        this.position = new Vector3f();
        this.rotation = new Vector2f();
        this.viewMatrix = new Matrix4f();
    }

    public void moveBackwards(float inc) {
        this.viewMatrix.positiveZ(this.direction).negate().mul(inc);
        this.position.sub(this.direction);
        this.recalculate();
    }

    public void moveForward(float inc) {
        this.viewMatrix.positiveZ(this.direction).negate().mul(inc);
        this.position.add(this.direction);
        this.recalculate();
    }

    public void moveLeft(float inc) {
        this.viewMatrix.positiveX(this.right).mul(inc);
        this.position.sub(this.right);
        this.recalculate();
    }

    public void moveRight(float inc) {
        this.viewMatrix.positiveX(this.right).mul(inc);
        this.position.add(this.right);
        this.recalculate();
    }

    public void moveDown(float inc) {
        this.viewMatrix.positiveY(this.up).mul(inc);
        this.position.sub(this.up);
        this.recalculate();
    }

    public void moveUp(float inc) {
        this.viewMatrix.positiveY(this.up).mul(inc);
        this.position.add(this.up);
        this.recalculate();
    }

    public void addRotation(float x, float y) {
        this.rotation.add(x, y);
        this.recalculate();
    }

    public Matrix4f getMatrix() {
        return this.viewMatrix;
    }

    private void recalculate() {
        this.viewMatrix
                .identity()
                .rotateX(this.rotation.x)
                .rotateY(this.rotation.y)
                .translate(-this.position.x, -this.position.y, -this.position.z);
    }

    public void handleWindowInput(Window window, float dt) {
        float dist = dt * MOVEMENT_SPEED;
        if (window.isKeyPressed(GLFW_KEY_W)) {
            this.moveForward(dist);
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            this.moveBackwards(dist);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            this.moveLeft(dist);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            this.moveRight(dist);
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            this.moveUp(dist);
        } else if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            this.moveDown(dist);
        }

        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isRightButtonPressed()) {
            Vector2f delta = mouseInput.getDelta();
            this.addRotation((float) Math.toRadians(-delta.x * MOUSE_SENSITIVITY),
                    (float) Math.toRadians(-delta.y * MOUSE_SENSITIVITY));
        }
    }
}
