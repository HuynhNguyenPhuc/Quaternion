package com.example.quaternion;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {
    LaurelRenderer mRenderer;
    public MyGLSurfaceView(Context context, String mode, Object... args) {
        super(context);

        setEGLContextClientVersion(3);

        mRenderer = new LaurelRenderer(context, mode, args);
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void updateAngles(Object... args) {
        mRenderer.setAngles(args);
        requestRender();
    }
}