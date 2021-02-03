package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui.components;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_BLEND;

public class TextureHourglass {
    private static final int COORDS_PER_VERTEX = 3;

    private final String vertexShaderCode =
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private final int program;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private int positionHandle;
    private int colorHandle;
    private int vPMatrixHandle;

    static float[][] colors = {
            {1.0f, 1.0f, 1.0f, 0.1f},
            {0.286f, 0.286f, 0.286f, 1f},
            {0.216f, 0.278f, 0.31f, 1f}
    };

    static float vertices[] = {
            0.0f, 0.0f, 0.0f, // point (0, 0, 0)
            -0.25f, -0.75f, 0.25f, // 1. left bottom front corner
            0.25f, -0.75f, 0.25f, // 2. right bottom front corner
            0.25f, -0.75f, -0.25f, // 3. right bottom back corner
            -0.25f, -0.75f, -0.25f, // 4. left bottom back corner
            -0.25f, 0.75f, 0.25f, // 5. left top front corner
            0.25f, 0.75f, 0.25f, // 6. right top front corner
            0.25f, 0.75f, -0.25f, // 7. right top back corner
            -0.25f, 0.75f, -0.25f, // 8. left top back corner
            -0.01f, 0f, 0.01f, // 9. left center front corner
            0.01f, 0f, 0.01f, // 10. right center front corner
            0.01f, 0f, -0.01f, // 11. right center back corner
            -0.01f, 0f, -0.01f, // 12. left center back corner
            -0.50f/3, -0.50f, 0.50f/3, // 13. left mid-bottom front corner
            0.50f/3, -0.50f, 0.50f/3, // 14. right mid-bottom front corner
            0.50f/3, -0.50f, -0.50f/3, // 15. right mid-bottom back corner
            -0.50f/3, -0.50f, -0.50f/3, // 16. left mid-bottom back corner
            -0.50f/3, 0.50f, 0.50f/3, // 17. left mid-top front corner
            0.50f/3, 0.50f, 0.50f/3, // 18. right mid-top front corner
            0.50f/3, 0.50f, -0.50f/3, // 19. right mid-top back corner
            -0.50f/3, 0.50f, -0.50f/3, // 20. left mid-top back corner
            0.0f, -0.75f, 0.0f, // 21. bottom center
    };

    short indices[] = {
            5, 6, 8, 6, 7, 8, // top base (transparent)
            17, 18, 5, 18, 6, 5, 18, 19, 6, 19, 7, 6, 20, 19, 8, 19, 7, 8, 17, 20, 5, 20, 8, 5, // top transparent
            13, 14, 9, 14, 10, 9, 14, 15, 10, 15, 11, 10, 16, 15, 11, 16, 11, 12, 13, 16, 9, 16, 12, 9, // bottom transparent
            1, 2, 4, 2, 3, 4, // bottom base (transparent)
            9, 10, 17, 10, 18, 17, 10, 11, 18, 11, 19, 18, 12, 11, 19, 12, 19, 20, 9, 12, 17, 9, 20, 17, // top colored
            1, 2, 13, 2, 14, 13, 2, 3, 14, 3, 15, 14, 4, 3, 16, 3, 15, 16, 1, 4, 13, 4, 16, 13, // bottom colored
            0, 21, 5, 6, 6, 7, 7, 8, 8, 5, 5, 9, 6, 10, 7, 11, 8, 20, // lines
            9, 1, 10, 2, 11, 3, 12, 4, 1, 2, 2, 3, 3, 4, 4, 1 // lines
    };

    public TextureHourglass() {
        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(indices.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuffer.put(indices).position(0);

        int vertexShader = EditReminderGLViewRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = EditReminderGLViewRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        vPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        colorHandle = GLES20.glGetUniformLocation(program, "vColor");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glEnable(GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < 36; i++) {
            // Set the color for each of the faces
            float[] color = i < 18 ? colors[0] : colors[1];
            GLES20.glUniform4fv(colorHandle, 1, color, 0);

            indexBuffer.position(i * 3);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, 3, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        }
        GLES20.glDisable(GL_BLEND);

        for (int i = 0; i < 17; i++) {
            // Set the color for each of the line
            float[] color = i == 0 ? colors[1] : colors[2];
            GLES20.glUniform4fv(colorHandle, 1, color, 0);

            indexBuffer.position(36 * 3 + i * 2);
            GLES20.glDrawElements(GLES20.GL_LINE_STRIP, 2, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        }

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
