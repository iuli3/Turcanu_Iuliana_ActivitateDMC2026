package com.example.proiect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MisiuniActivity extends AppCompatActivity {

    private static final int REQ_ADAUGA = 10;
    private DatabaseHelper db;
    private MisiuneAdapter adapter;
    private Spinner spinnerFiltru;
    private List<Misiune> toateMisiunile = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misiuni);

        db = new DatabaseHelper(this);
        ListView lvMisiuni = findViewById(R.id.lvMisiuni);
        Button btnAdauga = findViewById(R.id.btnAdaugaMisiune);
        Button btnCalendar = findViewById(R.id.btnCalendarMisiuni);
        spinnerFiltru = findViewById(R.id.spinnerFiltru);

        toateMisiunile = new ArrayList<>(db.getToateMisiunile());
        adapter = new MisiuneAdapter(this, new ArrayList<>());
        lvMisiuni.setAdapter(adapter);

        String[] optiuni = {"Toate misiunile", "Active", "Inactive", "Prioritate ↓"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, optiuni);
        spinnerFiltru.setAdapter(spinnerAdapter);
        spinnerFiltru.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aplicaFiltru(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        lvMisiuni.setOnItemLongClickListener((parent, view, position, id) -> {
            Misiune m = adapter.getItem(position);
            db.stergeMisiune(m.id);
            db.adaugaLog("Misiune stearsa", m.nume);
            final int mId = m.id;
            toateMisiunile.removeIf(x -> x.id == mId);
            aplicaFiltru(spinnerFiltru.getSelectedItemPosition());
            Toast.makeText(this, "Misiune stearsa", Toast.LENGTH_SHORT).show();
            return true;
        });

        btnAdauga.setOnClickListener(v ->
                startActivityForResult(new Intent(this, AdaugareMisiuneActivity.class), REQ_ADAUGA));

        btnCalendar.setOnClickListener(v ->
                startActivity(new Intent(this, CalendarMisiuniActivity.class)));
    }

    private void aplicaFiltru(int pozitie) {
        List<Misiune> filtrate = new ArrayList<>();
        switch (pozitie) {
            case 0:
                filtrate.addAll(toateMisiunile);
                break;
            case 1:
                for (Misiune m : toateMisiunile) if (m.activ) filtrate.add(m);
                break;
            case 2:
                for (Misiune m : toateMisiunile) if (!m.activ) filtrate.add(m);
                break;
            case 3:
                filtrate.addAll(toateMisiunile);
                Collections.sort(filtrate, (a, b) -> Float.compare(b.prioritate, a.prioritate));
                break;
        }
        adapter.clear();
        adapter.addAll(filtrate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADAUGA && resultCode == RESULT_OK) {
            toateMisiunile = new ArrayList<>(db.getToateMisiunile());
            aplicaFiltru(spinnerFiltru.getSelectedItemPosition());
        }
    }
}
