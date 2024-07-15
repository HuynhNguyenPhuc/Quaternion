package com.example.aabb;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {

    public MyGLSurfaceView(Context context, String mode, Object... args) {
        super(context);

        setEGLContextClientVersion(3);

        if (!mode.equals("mode4")) {
            this.setRenderer(new Render(this, mode, args));
        }
        else {
            this.setRenderer(new LaurelRender(context, args));
        }
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}