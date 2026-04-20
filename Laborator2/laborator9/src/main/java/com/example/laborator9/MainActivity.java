package com.example.laborator9;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenImages = findViewById(R.id.btnOpenImages);
        btnOpenImages.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ImagesActivity.class);
            startActivity(intent);
        });
    }
}
