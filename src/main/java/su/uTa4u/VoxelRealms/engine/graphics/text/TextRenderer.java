package su.uTa4u.VoxelRealms.engine.graphics.text;

import org.lwjgl.system.MemoryStack;
import su.uTa4u.VoxelRealms.engine.graphics.ShaderProgram;
import su.uTa4u.VoxelRealms.util.Utils;
import su.uTa4u.VoxelRealms.engine.Window;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public final class TextRenderer {
    private final Map<Character, Glyph> glyphs;
    private int imageWidth;
    private int imageHeight;
    private final ShaderProgram shaderProgram;
    private final Window window;
    private final int fontHeight;
    private final int texture;
    private final int vaoId;
    private final int vboId;

    public TextRenderer(Window window) {
        this.glyphs = new HashMap<>();
        this.window = window;
        Font font = Utils.readFont("Minecraft.otf");
        font = font.deriveFont(48.0f);

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.dispose();

        Map<Character, BufferedImage> characters = new HashMap<>();
        this.imageWidth = 0;
        this.imageHeight = 0;
        for (int i = 32; i < 127; i++) {
            char c = (char) i;

            image = new BufferedImage(metrics.charWidth(c), metrics.getHeight(), BufferedImage.TYPE_INT_ARGB);
            g = image.createGraphics();
            g.setFont(font);
            g.setPaint(Color.WHITE);
            g.drawString(String.valueOf(c), 0, metrics.getAscent());
            g.dispose();
            characters.put(c, image);
            this.imageWidth += image.getWidth();
            this.imageHeight = Math.max(this.imageHeight, image.getHeight());
        }

        this.fontHeight = this.imageHeight;

        image = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        int ix = 0;
        for (var entry : characters.entrySet()) {
            BufferedImage i = entry.getValue();
            g.drawImage(i, ix, 0, null);
            this.glyphs.put(entry.getKey(), new Glyph(ix, 0, i.getWidth(), this.imageHeight));
            ix += i.getWidth();
        }
        g.dispose();

        this.texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        int[] pixels = new int[this.imageWidth * this.imageHeight];
        image.getRGB(0, 0, this.imageWidth, this.imageHeight, pixels, 0, this.imageWidth);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer buffer = stack.malloc(this.imageWidth * this.imageHeight * 4);
            for (int y = this.imageHeight - 1; y >= 0; y--) {
                for (int x = 0; x < this.imageWidth; x++) {
                    int pixel = pixels[y * this.imageWidth + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) (pixel & 0xFF));
                    buffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
            buffer.flip();
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.imageWidth, this.imageHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        }


        this.vaoId = glGenVertexArrays();
        this.vboId = glGenBuffers();

        this.shaderProgram = new ShaderProgram(List.of(
                new ShaderProgram.ShaderData(GL_VERTEX_SHADER, "glyph.vert"),
                new ShaderProgram.ShaderData(GL_FRAGMENT_SHADER, "glyph.frag")
        ));
        this.shaderProgram.createUniform("resolution");
    }

    public void renderText(String text, int x, int y) {
        int lines = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') lines++;
        }
        int textHeight = lines * this.fontHeight;

        glBindVertexArray(this.vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, this.vboId);
        this.shaderProgram.bind();
        this.shaderProgram.setUniform("resolution", this.window.getWidth(), this.window.getHeight());

        glBindTexture(GL_TEXTURE_2D, this.texture);

        float scale = 1.0f;
        float drawX = x;
        float drawY = y;
        for (int i = 0; i < text.length(); i++) {
            Glyph g = this.glyphs.get(text.charAt(i));
            float w = g.w * scale;
            float h = g.h * scale;
            float[] vertices = new float[]{
                    drawX, drawY, g.x / this.imageWidth, g.y / this.imageHeight,
                    drawX, drawY + h, g.x / this.imageWidth, (g.y + g.h) / this.imageHeight,
                    drawX + w, drawY, (g.x + g.w) / this.imageWidth, g.y / this.imageHeight,
                    drawX + w, drawY, (g.x + g.w) / this.imageWidth, g.y / this.imageHeight,
                    drawX, drawY + h, g.x / this.imageWidth, (g.y + g.h) / this.imageHeight,
                    drawX + w, drawY + h, (g.x + g.w) / this.imageWidth, (g.y + g.h) / this.imageHeight,
            };

            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 4, GL_FLOAT, false, Float.BYTES * 4, 0);

            glDrawArrays(GL_TRIANGLES, 0, 6);
            drawX += w;
        }

        this.shaderProgram.unbind();
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private record Glyph(float x, float y, float w, float h) {
    }
}
