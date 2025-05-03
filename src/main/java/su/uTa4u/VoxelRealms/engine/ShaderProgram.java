package su.uTa4u.VoxelRealms.engine;

import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;
import su.uTa4u.VoxelRealms.Utils;

import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

public final class ShaderProgram {
    private final int id;

    public ShaderProgram(List<ShaderData> shaders) {
        this.id = glCreateProgram();
        if (this.id == 0) {
            throw new RuntimeException("Could not create GL Program");
        }

        IntBuffer success = MemoryUtil.memAllocInt(1);

        for (ShaderData shader : shaders) {
            int shaderId = glCreateShader(shader.type);
            GL20.glShaderSource(shaderId, Utils.readFile(shader.path));
            glCompileShader(shaderId);
            glGetShaderiv(shaderId, GL_COMPILE_STATUS, success);
            if (success.get(0) == 0) {
                System.err.println("Could not compile shader: " + glGetShaderInfoLog(shaderId));
            }
            glAttachShader(this.id, shaderId);
            glLinkProgram(this.id);
            glGetProgramiv(this.id, GL_LINK_STATUS, success);
            if (success.get(0) == 0) {
                System.err.println("Could not link program: " + glGetProgramInfoLog(this.id));
            }
            glDetachShader(this.id, shaderId);
            glDeleteShader(shaderId);
        }
    }

    public void bind() {
        glUseProgram(this.id);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void validate() {
        glValidateProgram(this.id);
        if (glGetProgrami(this.id, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Could not validate shader program: " + glGetProgramInfoLog(this.id));
        }
    }

    record ShaderData(int type, String path) {
    }
}
