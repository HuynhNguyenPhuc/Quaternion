package com.example.quaternion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Mode2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode2);

        String mode = getIntent().getStringExtra("mode");

        // Vector inputs
        EditText vectorXInput = findViewById(R.id.x);
        EditText vectorYInput = findViewById(R.id.y);
        EditText vectorZInput = findViewById(R.id.z);

        findViewById(R.id.submit_button).setOnClickListener(v -> {
            float vectorX = Float.parseFloat(vectorXInput.getText().toString());
            float vectorY = Float.parseFloat(vectorYInput.getText().toString());
            float vectorZ = Float.parseFloat(vectorZInput.getText().toString());

            Intent intent = new Intent(Mode2Activity.this, Mode2ViewActivity.class);
            intent.putExtra("mode", mode);
            intent.putExtra("vectorX", vectorX);
            intent.putExtra("vectorY", vectorY);
            intent.putExtra("vectorZ", vectorZ);
            startActivity(intent);
        });

        findViewById(R.id.return_button).setOnClickListener(v -> finish());
    }
}