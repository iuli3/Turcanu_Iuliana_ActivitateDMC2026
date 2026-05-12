package com.example.laborator11;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GraficActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafic);

        ArrayList<String> etichete = getIntent().getStringArrayListExtra("etichete");
        float[] valori = getIntent().getFloatArrayExtra("valori");
        String tip = getIntent().getStringExtra("tip");

        if (etichete == null || valori == null || tip == null) {
            finish();
            return;
        }

        TextView tvTitlu = findViewById(R.id.tvTitluGrafic);
        tvTitlu.setText(tip);

        GraficView graficView = findViewById(R.id.graficView);
        graficView.setDate(valori, etichete.toArray(new String[0]), tip);
    }
}
