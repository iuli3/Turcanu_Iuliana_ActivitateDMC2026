package com.example.laborator11;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class

MainActivity extends AppCompatActivity {

    private final ArrayList<String> etichete = new ArrayList<>();
    private final ArrayList<Float> valori = new ArrayList<>();
    private final ArrayList<String> displayList = new ArrayList<>();
    private ArrayAdapter<String> displayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etEticheta = findViewById(R.id.etEticheta);
        EditText etValoare = findViewById(R.id.etValoare);
        Button btnAdauga = findViewById(R.id.btnAdauga);
        Spinner spinnerTip = findViewById(R.id.spinnerTip);
        ListView lvValori = findViewById(R.id.lvValori);
        Button btnAfiseaza = findViewById(R.id.btnAfiseaza);

        String[] tipuri = {"ColumnChart", "BarChart", "PieChart"};
        spinnerTip.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, tipuri));

        displayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList);
        lvValori.setAdapter(displayAdapter);

        lvValori.setOnItemLongClickListener((parent, view, pos, id) -> {
            etichete.remove(pos);
            valori.remove(pos);
            displayList.remove(pos);
            displayAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Valoare stearsa", Toast.LENGTH_SHORT).show();
            return true;
        });

        btnAdauga.setOnClickListener(v -> {
            String eticheta = etEticheta.getText().toString().trim();
            String valorStr = etValoare.getText().toString().trim();

            if (eticheta.isEmpty()) {
                Toast.makeText(this, "Introduceti o eticheta!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (valorStr.isEmpty()) {
                Toast.makeText(this, "Introduceti o valoare!", Toast.LENGTH_SHORT).show();
                return;
            }

            float valoare;
            try {
                valoare = Float.parseFloat(valorStr);
                if (valoare <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valoarea trebuie sa fie un numar pozitiv!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            etichete.add(eticheta);
            valori.add(valoare);
            displayList.add("  " + eticheta + ":  " + formatVal(valoare));
            displayAdapter.notifyDataSetChanged();

            etEticheta.setText("");
            etValoare.setText("");
            etEticheta.requestFocus();
        });

        btnAfiseaza.setOnClickListener(v -> {
            if (valori.size() < 2) {
                Toast.makeText(this, "Adaugati cel putin 2 valori!", Toast.LENGTH_SHORT).show();
                return;
            }

            float[] valoriArr = new float[valori.size()];
            for (int i = 0; i < valori.size(); i++) valoriArr[i] = valori.get(i);

            Intent intent = new Intent(this, GraficActivity.class);
            intent.putStringArrayListExtra("etichete", etichete);
            intent.putExtra("valori", valoriArr);
            intent.putExtra("tip", spinnerTip.getSelectedItem().toString());
            startActivity(intent);
        });
    }

    private String formatVal(float v) {
        return v == (int) v ? String.valueOf((int) v) : String.format("%.1f", v);
    }
}
