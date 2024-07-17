package com.example.quaternion;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class Mode3ViewActivity extends AppCompatActivity {

    private MyGLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode3_view);

        String mode = getIntent().getStringExtra("mode");
        float q1X = getIntent().getFloatExtra("q1_x", 0.0f);
        float q1Y = getIntent().getFloatExtra("q1_y", 0.0f);
        float q1Z = getIntent().getFloatExtra("q1_z", 0.0f);
        float q1W = getIntent().getFloatExtra("q1_w", 0.0f);

        float q2X = getIntent().getFloatExtra("q2_x", 0.0f);
        float q2Y = getIntent().getFloatExtra("q2_y", 0.0f);
        float q2Z = getIntent().getFloatExtra("q2_z", 0.0f);
        float q2W = getIntent().getFloatExtra("q2_w", 0.0f);

        UnitQuaternion q1 = new UnitQuaternion(q1W, q1X, q1Y, q1Z);
        UnitQuaternion q2 = new UnitQuaternion(q2W, q2X, q2Y, q2Z);
        mGLView = new MyGLSurfaceView(this, mode, q1, q2);
        FrameLayout glSurfaceContainer = findViewById(R.id.gl_surface_container);
        glSurfaceContainer.addView(mGLView);

        SeekBar seekBar = findViewById(R.id.seekbar_t);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float t = progress * 1.0f / seekBar.getMax();
                mGLView.updateValues(t);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
