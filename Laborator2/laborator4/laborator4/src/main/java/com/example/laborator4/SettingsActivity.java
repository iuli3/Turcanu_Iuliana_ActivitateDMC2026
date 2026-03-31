package com.example.laborator4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText etDimensiune;
    private Spinner spinnerCuloare;
    private Button btnSalveazaSetari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etDimensiune = findViewById(R.id.etDimensiuneFont);
        spinnerCuloare = findViewById(R.id.spinnerCuloareFont);
        btnSalveazaSetari = findViewById(R.id.btnSalveazaSetari);

        String[] culori = {"Negru", "Rosu", "Albastru", "Verde"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, culori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCuloare.setAdapter(adapter);

        SharedPreferences sp = getSharedPreferences("SetariApp", MODE_PRIVATE);
        float dimensiuneSalvata = sp.getFloat("font_size", 18f);
        String culoareSalvata = sp.getString("font_color", "Negru");

        etDimensiune.setText(String.valueOf(dimensiuneSalvata));

        for (int i = 0; i < culori.length; i++) {
            if (culori[i].equals(culoareSalvata)) {
                spinnerCuloare.setSelection(i);
                break;
            }
        }

        btnSalveazaSetari.setOnClickListener(v -> {
            String dimText = etDimensiune.getText().toString().trim();

            if (!dimText.isEmpty()) {
                try {
                    float nouaDimensiune = Float.parseFloat(dimText);
                    String nouaCuloare = spinnerCuloare.getSelectedItem().toString();

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putFloat("font_size", nouaDimensiune);
                    editor.putString("font_color", nouaCuloare);

                    editor.apply();

                    Toast.makeText(this, "Setări salvate: " + nouaCuloare + ", " + nouaDimensiune, Toast.LENGTH_SHORT).show();

                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Dimensiunea trebuie să fie un număr valid!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Introduceți o dimensiune!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}