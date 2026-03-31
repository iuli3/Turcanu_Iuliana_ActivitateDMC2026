package com.example.laborator4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<InstrumentMuzical.InstrumentTehnic> listaInstrumente = new ArrayList<>();
    private InstrumentAdapter adapter;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    InstrumentMuzical.InstrumentTehnic instrument = data.getParcelableExtra("instrument_key");
                    int pozitie = data.getIntExtra("pozitie", -1);

                    if (instrument != null) {
                        if (pozitie != -1) {
                            listaInstrumente.set(pozitie, instrument);
                            Toast.makeText(this, "Instrument modificat!", Toast.LENGTH_SHORT).show();
                        } else {
                            listaInstrumente.add(instrument);
                            Toast.makeText(this, "Instrument adăugat!", Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listViewInstrumente);
        adapter = new InstrumentAdapter(this, listaInstrumente);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            InstrumentMuzical.InstrumentTehnic instrumentDeEditat = listaInstrumente.get(position);
            Intent intent = new Intent(MainActivity.this, AdaugareInstrument.class);
            intent.putExtra("instrument_edit", instrumentDeEditat);
            intent.putExtra("pozitie", position);
            launcher.launch(intent);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            // listaInstrumente.remove(position);
            // adapter.notifyDataSetChanged();
            // Toast.makeText(this, "Instrument șters!",
            InstrumentMuzical.InstrumentTehnic instrument = listaInstrumente.get(position);
            salveazaFavorite(instrument);
            return true;
        });

        Button btnAdauga = findViewById(R.id.btnAdauga);
        btnAdauga.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdaugareInstrument.class);
            launcher.launch(intent);
        });

        Button btnSetari = findViewById(R.id.btnSetari);
        btnSetari.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        incarcaSiAplicaSetari();
    }

    @Override
    protected void onResume() {
        super.onResume();
        incarcaSiAplicaSetari();
    }

    private void incarcaSiAplicaSetari() {
        SharedPreferences sp = getSharedPreferences("SetariApp", MODE_PRIVATE);

        float marimeFont = sp.getFloat("font_size", 18f);
        String culoareFont = sp.getString("font_color", "Negru");

        int colorInt;
        switch (culoareFont) {
            case "Rosu": colorInt = Color.RED; break;
            case "Albastru": colorInt = Color.BLUE; break;
            case "Verde": colorInt = Color.GREEN; break;
            default: colorInt = Color.BLACK;
        }

        Button btnAdauga = findViewById(R.id.btnAdauga);
        Button btnSetari = findViewById(R.id.btnSetari);

        if (btnAdauga != null) {
            btnAdauga.setTextSize(marimeFont);
            btnAdauga.setTextColor(colorInt);
        }
        if (btnSetari != null) {
            btnSetari.setTextSize(marimeFont);
            btnSetari.setTextColor(colorInt);
        }
    }

    private void salveazaFavorite(InstrumentMuzical.InstrumentTehnic instrument) {
        String numeFisier = "favorite.txt";
        String linie = instrument.denumire + "," + instrument.serie + "," + instrument.pret + "," + instrument.esteValid
                + "," + instrument.stare + "," + instrument.dataAchizitie + "\n";

        try (java.io.FileOutputStream fos = openFileOutput(numeFisier, MODE_APPEND)) {
            fos.write(linie.getBytes());
            Toast.makeText(this, "Instrument adăugat în FAVORITE!", Toast.LENGTH_SHORT).show();
        } catch (java.io.IOException e) {
            android.util.Log.e("MainActivity", "eroare la salvarea in FAVORITE", e);
            Toast.makeText(this, "Eroare la salvarea în FAVORITE", Toast.LENGTH_SHORT).show();
        }
    }
}