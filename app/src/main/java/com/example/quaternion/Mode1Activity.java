package com.example.quaternion;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;

public class Mode1Activity extends AppCompatActivity {
    private MyGLSurfaceView mGLView;
    private SeekBar seekBarX;
    private SeekBar seekBarY;
    private SeekBar seekBarZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode1);

        seekBarX = findViewById(R.id.seekbar_x);
        seekBarY = findViewById(R.id.seekbar_y);
        seekBarZ = findViewById(R.id.seekbar_z);

        mGLView = new MyGLSurfaceView(this, "mode1", 0.0f, 90.0f, 0.0f);
        FrameLayout glSurfaceContainer = findViewById(R.id.gl_surface_container);
        glSurfaceContainer.addView(mGLView);

        seekBarX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateScreen(progress, seekBarY.getProgress(), seekBarZ.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateScreen(seekBarX.getProgress(), progress, seekBarZ.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarZ.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateScreen(seekBarX.getProgress(), seekBarY.getProgress(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateScreen(int angleX, int angleY, int angleZ) {
        float normalizedAngleX = (float) angleX / seekBarX.getMax() * 360.0f;
        float normalizedAngleY = (float) angleY / seekBarY.getMax() * 360.0f;
        float normalizedAngleZ = (float) angleZ / seekBarZ.getMax() * 360.0f;

        mGLView.updateAngles(normalizedAngleX, normalizedAngleY, normalizedAngleZ);
    }
}