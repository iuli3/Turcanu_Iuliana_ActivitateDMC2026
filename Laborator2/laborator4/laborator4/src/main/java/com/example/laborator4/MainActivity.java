package com.example.laborator4;

import android.content.Intent;
import android.os.Build;
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
            listaInstrumente.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Instrument șters!", Toast.LENGTH_SHORT).show();
            return true;
        });

        Button btnAdauga = findViewById(R.id.btnAdauga);
        btnAdauga.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdaugareInstrument.class);
            launcher.launch(intent);
        });
    }
}