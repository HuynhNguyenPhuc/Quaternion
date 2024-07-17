package com.example.quaternion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Mode3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode3);

        String mode = getIntent().getStringExtra("mode");

        EditText q1X = findViewById(R.id.q1_x);
        EditText q1Y = findViewById(R.id.q1_y);
        EditText q1Z = findViewById(R.id.q1_z);
        EditText q1W = findViewById(R.id.q1_w);

        EditText q2X = findViewById(R.id.q2_x);
        EditText q2Y = findViewById(R.id.q2_y);
        EditText q2Z = findViewById(R.id.q2_z);
        EditText q2W = findViewById(R.id.q2_w);

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> {
            float q1XValue = Float.parseFloat(q1X.getText().toString());
            float q1YValue = Float.parseFloat(q1Y.getText().toString());
            float q1ZValue = Float.parseFloat(q1Z.getText().toString());
            float q1WValue = Float.parseFloat(q1W.getText().toString());

            float q2XValue = Float.parseFloat(q2X.getText().toString());
            float q2YValue = Float.parseFloat(q2Y.getText().toString());
            float q2ZValue = Float.parseFloat(q2Z.getText().toString());
            float q2WValue = Float.parseFloat(q2W.getText().toString());

            Intent intent = new Intent(Mode3Activity.this, Mode3ViewActivity.class);

            intent.putExtra("mode", mode);
            intent.putExtra("q1_x", q1XValue);
            intent.putExtra("q1_y", q1YValue);
            intent.putExtra("q1_z", q1ZValue);
            intent.putExtra("q1_w", q1WValue);

            intent.putExtra("q2_x", q2XValue);
            intent.putExtra("q2_y", q2YValue);
            intent.putExtra("q2_z", q2ZValue);
            intent.putExtra("q2_w", q2WValue);

            startActivity(intent);
        });

        Button returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> finish());
    }
}