package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui.components;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class EditReminderGLViewRenderer implements GLSurfaceView.Renderer {
    Context context;
    private TextureHourglass textureHourglass;
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private  float angleTriangle=0.5f;
    private  float speedTriangle=0.05f;
    private float[] rotationMatrix = new float[16];

    public EditReminderGLViewRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.878f, 0.878f, 0.878f, 1.0f);
        textureHourglass = new TextureHourglass();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = -0.090f * ((int) time);
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 1f, 0f);

        Matrix.setLookAtM(viewMatrix, 0, 3f, 0f, 3f, 0f, 0f, 0f, 0f, 1f, 0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);
        textureHourglass.draw(scratch);
    }

    public static int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}