package su.uTa4u.VoxelRealms.engine;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import su.uTa4u.VoxelRealms.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public final class ShaderProgram {
    private final int id;
    private final Map<String, Integer> uniforms;

    public ShaderProgram(List<ShaderData> shaders) {
        this.id = glCreateProgram();
        this.uniforms = new HashMap<>();
        if (this.id == 0) {
            throw new RuntimeException("Could not create GL Program");
        }

        int[] shaderIds = new int[shaders.size()];
        for (int i = 0; i < shaders.size(); i++) {
            ShaderData shader = shaders.get(i);
            int shaderId = glCreateShader(shader.type);
            shaderIds[i] = shaderId;
            GL20.glShaderSource(shaderId, Utils.readFile("shaders/" + shader.name));
            glCompileShader(shaderId);
            if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
                System.err.println("Could not compile shader: " + glGetShaderInfoLog(shaderId));
            }
            glAttachShader(this.id, shaderId);
        }

        glLinkProgram(this.id);
        if (glGetProgrami(this.id, GL_LINK_STATUS) == 0) {
            System.err.println("Could not link program: " + glGetProgramInfoLog(this.id));
        }

        for (int shaderId : shaderIds) {
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

    public void createUniform(String name) {
        int loc = glGetUniformLocation(this.id, name);
        if (loc < 0) throw new RuntimeException("Could not create uniform: " + name + " in shader program " + this.id);
        this.uniforms.put(name, loc);
    }

    public void setUniform(String name, Matrix4f value) {
        Integer loc = this.uniforms.get(name);
        if (loc == null) throw new RuntimeException("Could not find uniform: " + name + " in shader program " + this.id);
        glUniformMatrix4fv(loc, false, value.get(new float[16]));
    }

    public record ShaderData(int type, String name) {
    }
}
