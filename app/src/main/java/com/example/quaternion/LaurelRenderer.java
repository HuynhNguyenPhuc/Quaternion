package com.example.quaternion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class LaurelRenderer implements GLSurfaceView.Renderer {
    private final Context mActivityContext;
    private final String mode;
    private float alpha;
    private float beta;
    private float gamma;
    private Vector axis;
    private float angle;

    private final float[] mModelMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private float[] lightPosition;
    private float[] center;

    private final FloatBuffer mVertexBuffer;
    private final Material mMaterial;

    private final int numVBOHandles = 1;
    private final int[] mVBOHandles = new int [numVBOHandles];

    private int mModelMatrixHandle;
    private int mViewMatrixHandle;
    private int mProjectionMatrixHandle;
    private int mPositionHandle;
    private int mTextureHandle;
    private int mNormalHandle;
    private int mTextureCoordinateHandle;
    private int mLightPositionHandle;
    private int mTextureUniformHandle;
    private int mKAHandle;
    private int mKDHandle;
    private int mKSHandle;
    private int mNSHandle;

    private final int numPoints;
    private final int mBytesPerFloat = 4;
    private final int mPositionDataSize = 3;
    private final int mNormalDataSize = 3;
    private final int mTextureDataSize = 2;

    private int laurelProgramHandle;

    public LaurelRenderer(Context context, String mode, Object... args) {
        this.mActivityContext = context;
        this.mode = mode;
        this.getArguments(mode, args);

        VertexLoader loader = new VertexLoader(context, "laurel.obj");
        List<Float> vertexArray = loader.getVertexArray();
        this.center = loader.getCenter();

        numPoints = vertexArray.size() / (mPositionDataSize + mNormalDataSize + mTextureDataSize);
        mVertexBuffer = ByteBuffer.allocateDirect(vertexArray.size() * mBytesPerFloat)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        for (float value: vertexArray) {
            mVertexBuffer.put(value);
        }
        mVertexBuffer.position(0);

        mMaterial = new MaterialLoader().load(mActivityContext, "laurel.mtl");

        vertexArray.clear();
        System.gc();
        System.runFinalization();
    }

    public void getArguments(String mode, Object... args){
        switch (mode){
            case "mode1":
                this.alpha = (Float) args[0];
                this.beta = (Float) args[1];
                this.gamma = (Float) args[2];
                break;
            case "mode2":
                this.axis = new Vector ((Float) args[0], (Float) args[1], (Float) args[2]);
                this.angle = (Float) args[3];

                UnitQuaternion q = new UnitQuaternion(axis, (float) Math.toRadians(angle));
                float[] angles = q.getEulerAngles();
                this.alpha = angles[0];
                this.beta = angles[1];
                this.gamma = angles[2];
                break;
        }
    }

    public void setAngles(Object... args){
        switch (mode) {
            case "mode1":
                this.alpha = (Float) args[0];
                this.beta = (Float) args[1];
                this.gamma = (Float) args[2];
                break;
            case "mode2":
                this.angle = (Float) args[0];

                UnitQuaternion q = new UnitQuaternion(axis, (float) Math.toRadians(angle));
                float[] angles = q.getEulerAngles();

                this.alpha = angles[0];
                this.beta = angles[1];
                this.gamma = angles[2];
                break;
        }
    }

    public static int loadTexture(final Context context, final int resourceId)
    {
        final int[] textureHandle = new int[1];

        GLES30.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandle[0]);

            // Set wrapping
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);

            // Set filtering
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    void setupLaurelProgram(){
        final String vertexShader = ShaderProgram.getVertexShader();
        int vertexShaderHandle = GLES30.glCreateShader(GLES30.GL_VERTEX_SHADER);
        GLES30.glShaderSource(vertexShaderHandle, vertexShader);
        GLES30.glCompileShader(vertexShaderHandle);

        final String fragmentShader = ShaderProgram.getFragmentShader();
        int fragmentShaderHandle = GLES30.glCreateShader(GLES30.GL_FRAGMENT_SHADER);
        GLES30.glShaderSource(fragmentShaderHandle, fragmentShader);
        GLES30.glCompileShader(fragmentShaderHandle);

        laurelProgramHandle = GLES30.glCreateProgram();
        GLES30.glAttachShader(laurelProgramHandle, vertexShaderHandle);
        GLES30.glAttachShader(laurelProgramHandle, fragmentShaderHandle);
        GLES30.glBindAttribLocation(laurelProgramHandle, 0, "a_Position");
        GLES30.glBindAttribLocation(laurelProgramHandle, 1, "a_TexCoord");
        GLES30.glBindAttribLocation(laurelProgramHandle, 2, "a_Normal");
        GLES30.glLinkProgram(laurelProgramHandle);

        mPositionHandle = GLES30.glGetAttribLocation(laurelProgramHandle, "a_Position");
        mTextureCoordinateHandle = GLES30.glGetAttribLocation(laurelProgramHandle, "a_TexCoord");
        mNormalHandle = GLES30.glGetAttribLocation(laurelProgramHandle, "a_Normal");

        mModelMatrixHandle = GLES30.glGetUniformLocation(laurelProgramHandle, "u_ModelMatrix");
        mViewMatrixHandle = GLES30.glGetUniformLocation(laurelProgramHandle, "u_ViewMatrix");
        mProjectionMatrixHandle = GLES30.glGetUniformLocation(laurelProgramHandle, "u_ProjectionMatrix");
        mLightPositionHandle = GLES30.glGetUniformLocation(laurelProgramHandle, "u_LightPosition");
        mTextureUniformHandle = GLES30.glGetUniformLocation(laurelProgramHandle, "u_Texture");
        mKAHandle = GLES30.glGetUniformLocation(laurelProgramHandle, "kA");
        mKDHandle = GLES30.glGetUniformLocation(laurelProgramHandle, "kD");
        mKSHandle = GLES30.glGetUniformLocation(laurelProgramHandle, "kS");
        mNSHandle = GLES30.glGetUniformLocation(laurelProgramHandle, "nS");
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        // Enable depth testing
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);

        /* Set up the view matrix */
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 7.5f;

        /* Set up the center point */
        center[0] += eyeX;
        center[1] += eyeY;
        center[2] += eyeZ;

        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = 0.0f;

        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        /* Set up the light position */
        final float lightX =  0.0f;
        final float lightY =  0.0f;
        final float lightZ =  -2.0f;

        lightPosition = new float [] {lightX, lightY, lightZ};

        GLES30.glGenBuffers(numVBOHandles, mVBOHandles, 0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOHandles[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, mVertexBuffer.capacity() * mBytesPerFloat, mVertexBuffer, GLES30.GL_DYNAMIC_DRAW);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

        setupLaurelProgram();
        GLES30.glUseProgram(laurelProgramHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        GLES30.glViewport(0, 0, width, height);

        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 15.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

        switch (mode){
            case "mode2":
            case "mode1":
                Matrix.setIdentityM(mModelMatrix, 0);

                Matrix.translateM(mModelMatrix, 0, -center[0], -center[1], -center[2]);

                Matrix.rotateM(mModelMatrix, 0, alpha, 1.0f, 0.0f, 0.0f);
                Matrix.rotateM(mModelMatrix, 0, beta, 0.0f, 1.0f, 0.0f);
                Matrix.rotateM(mModelMatrix, 0, gamma, 0.0f, 0.0f, 1.0f);

                Matrix.translateM(mModelMatrix, 0, center[0], center[1], center[2]);

                drawLaurel();
                break;
        }
    }

    private void drawLaurel() {
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureHandle);
        GLES30.glUniform1i(mTextureUniformHandle, 0);
        mTextureHandle = loadTexture(mActivityContext, R.drawable.laurel);

        GLES30.glUniformMatrix4fv(mModelMatrixHandle, 1, false, mModelMatrix, 0);
        GLES30.glUniformMatrix4fv(mViewMatrixHandle, 1, false, mViewMatrix, 0);
        GLES30.glUniformMatrix4fv(mProjectionMatrixHandle, 1, false, mProjectionMatrix, 0);
        GLES30.glUniform3fv(mLightPositionHandle, 1, lightPosition, 0);

        GLES30.glUniform3fv(mKAHandle, 1, mMaterial.getAmbient(), 0);
        GLES30.glUniform3fv(mKDHandle, 1, mMaterial.getDiffuse(), 0);
        GLES30.glUniform3fv(mKSHandle, 1, mMaterial.getSpecular(), 0);
        GLES30.glUniform1f(mNSHandle, mMaterial.getShininess());
        
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOHandles[0]);
        GLES30.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES30.GL_FLOAT, false, (mPositionDataSize + mNormalDataSize + mTextureDataSize) * mBytesPerFloat, 0);
        GLES30.glEnableVertexAttribArray(mPositionHandle);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOHandles[0]);
        GLES30.glVertexAttribPointer(mTextureCoordinateHandle, mTextureDataSize, GLES30.GL_FLOAT, false, (mPositionDataSize + mNormalDataSize + mTextureDataSize) * mBytesPerFloat, mPositionDataSize * mBytesPerFloat);
        GLES30.glEnableVertexAttribArray(mTextureCoordinateHandle);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOHandles[0]);
        GLES30.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES30.GL_FLOAT, false, (mPositionDataSize + mNormalDataSize + mTextureDataSize) * mBytesPerFloat, (mPositionDataSize + mTextureDataSize) * mBytesPerFloat);
        GLES30.glEnableVertexAttribArray(mNormalHandle);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, numPoints);
    }
}