package com.example.laborator4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvDenumire, tvSerie, tvPret, tvValid, tvStare, tvRating, tvTipSunet;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    tvDenumire.setText("Denumire: " + data.getStringExtra("denumire"));
                    tvSerie.setText("Serie: "+ data.getIntExtra("serie", 0));
                    tvPret.setText("Pret: "+ data.getDoubleExtra("pret", 0) + " RON");
                    tvValid.setText("Valid: "+ data.getBooleanExtra("esteValid", false));
                    tvStare.setText("Stare: "+ data.getStringExtra("stare"));
                    tvRating.setText("Rating: "+data.getFloatExtra("rating", 0));
                    tvTipSunet.setText("Sunet: "+data.getStringExtra("tipSunet"));
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDenumire = findViewById(R.id.tvDenumire);
        tvSerie= findViewById(R.id.tvSerie);
        tvPret= findViewById(R.id.tvPret);
        tvValid= findViewById(R.id.tvValid);
        tvStare= findViewById(R.id.tvStare);
        tvRating= findViewById(R.id.tvRating);
        tvTipSunet= findViewById(R.id.tvTipSunet);

        Button btnAdauga = findViewById(R.id.btnAdauga);
        btnAdauga.setOnClickListener(v -> launcher.launch(
                new Intent(MainActivity.this, AdaugareInstrument.class)
        ));
    }
}