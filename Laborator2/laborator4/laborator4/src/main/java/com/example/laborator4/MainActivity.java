package com.example.laborator4;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {


    //lab5
    private final List<InstrumentMuzical.InstrumentTehnic> listaInstrumente = new ArrayList<>();
    private ArrayAdapter<InstrumentMuzical.InstrumentTehnic> adapter;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    InstrumentMuzical.InstrumentTehnic instrument = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        instrument = new InstrumentMuzical.InstrumentTehnic(
                                data.getStringExtra("denumire"),
                                data.getIntExtra("serie", 0),
                                data.getBooleanExtra("esteValid", false),
                                data.getDoubleExtra("pret", 0.0),
                                InstrumentMuzical.StareInstrument.valueOf(data.getStringExtra("stare")),
                                LocalDate.parse(data.getStringExtra("dataAchizitie"))
                        );
                    }
                    listaInstrumente.add(instrument);
                    adapter.notifyDataSetChanged();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listViewInstrumente);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaInstrumente
        );

        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            InstrumentMuzical.InstrumentTehnic instrument = listaInstrumente.get(position);
            Toast.makeText(this, instrument.toString(), Toast.LENGTH_LONG).show();
        });
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            listaInstrumente.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Instrument sters!", Toast.LENGTH_SHORT).show();
            return true;
        });

        Button btnAdauga = findViewById(R.id.btnAdauga);
        btnAdauga.setOnClickListener(v -> launcher.launch(
                new Intent(MainActivity.this, AdaugareInstrument.class)
        ));
    }
}