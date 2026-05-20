package com.example.proiect;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetariAvansateActivity extends AppCompatActivity {

    private static final String PREFS = "dronewatch_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setari_avansate);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        Spinner spinnerRefresh = findViewById(R.id.spinnerIntervalRefresh);
        Switch swSunetAlerte = findViewById(R.id.swSunetAlerte);
        Switch swAfisareCoordonate = findViewById(R.id.swAfisareCoordonate);
        Switch swConfirmareSterge = findViewById(R.id.swConfirmareSterge);
        Button btnSalveaza = findViewById(R.id.btnSalveazaSetariAvansate);
        Button btnResetare = findViewById(R.id.btnResetareSetari);

        String[] intervaluri = {"30 secunde", "1 minut", "5 minute", "10 minute", "Manual"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, intervaluri);
        spinnerRefresh.setAdapter(spinnerAdapter);

        int intervalSalvat = prefs.getInt("intervalRefresh", 2);
        spinnerRefresh.setSelection(intervalSalvat);
        swSunetAlerte.setChecked(prefs.getBoolean("sunetAlerte", true));
        swAfisareCoordonate.setChecked(prefs.getBoolean("afisareCoordonate", false));
        swConfirmareSterge.setChecked(prefs.getBoolean("confirmareSterge", true));

        btnSalveaza.setOnClickListener(v -> {
            prefs.edit()
                    .putInt("intervalRefresh", spinnerRefresh.getSelectedItemPosition())
                    .putBoolean("sunetAlerte", swSunetAlerte.isChecked())
                    .putBoolean("afisareCoordonate", swAfisareCoordonate.isChecked())
                    .putBoolean("confirmareSterge", swConfirmareSterge.isChecked())
                    .apply();
            new DatabaseHelper(this).adaugaLog("Setari actualizate",
                    "Interval: " + intervaluri[spinnerRefresh.getSelectedItemPosition()]);
            Toast.makeText(this, "Setari salvate!", Toast.LENGTH_SHORT).show();
        });

        btnResetare.setOnClickListener(v -> {
            spinnerRefresh.setSelection(2);
            swSunetAlerte.setChecked(true);
            swAfisareCoordonate.setChecked(false);
            swConfirmareSterge.setChecked(true);
            Toast.makeText(this, "Setari resetate la valori implicite", Toast.LENGTH_SHORT).show();
        });
    }
}
