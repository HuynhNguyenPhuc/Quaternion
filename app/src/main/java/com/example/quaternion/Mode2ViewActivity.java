package com.example.quaternion;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class Mode2ViewActivity extends AppCompatActivity {
    private MyGLSurfaceView mGLView;
    private SeekBar seekBarAngle;
    private SeekBar seekBarY;
    private SeekBar seekBarZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode2_view);

        String mode = getIntent().getStringExtra("mode");
        float vectorX = getIntent().getFloatExtra("vectorX", 0.0f);
        float vectorY = getIntent().getFloatExtra("vectorY", 0.0f);
        float vectorZ = getIntent().getFloatExtra("vectorZ", 0.0f);

        seekBarAngle = findViewById(R.id.seekbar_angle);

        mGLView = new MyGLSurfaceView(this, mode, vectorX, vectorY, vectorZ, 0.0f);
        FrameLayout glSurfaceContainer = findViewById(R.id.gl_surface_container);
        glSurfaceContainer.addView(mGLView);

        seekBarAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateScreen(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateScreen(int angle) {
        float normalizedAngle = (float) angle / seekBarAngle.getMax() * 360.0f;
        mGLView.updateValues(normalizedAngle);
    }
}