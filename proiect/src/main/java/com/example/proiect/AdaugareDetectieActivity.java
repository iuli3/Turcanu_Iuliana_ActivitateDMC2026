package com.example.proiect;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AdaugareDetectieActivity extends AppCompatActivity {

    private String dataSelectata = "";

    private static final String[] LOCATII_CAMERE = {
            "Intrare principală, Sector Nord",
            "Flancul stâng, Sector Vest",
            "Ieșire spate, Sector Sud",
            "Flancul drept, Sector Est"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaugare_detectie);

        Spinner spinnerCamera = findViewById(R.id.spinnerCamera);
        EditText etLocatie = findViewById(R.id.etLocatie);
        EditText etPersoane = findViewById(R.id.etPersoane);
        CheckBox cbAlerta = findViewById(R.id.cbAlerta);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Switch swActiv = findViewById(R.id.swActiv);
        Button btnData = findViewById(R.id.btnSelecteazaData);
        TextView tvData = findViewById(R.id.tvDataSelectata);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        Button btnSalveaza = findViewById(R.id.btnSalveazaDetectie);

        String[] camere = {"FATA", "LATERAL1", "SPATE", "LATERAL2"};
        spinnerCamera.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, camere));

        spinnerCamera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etLocatie.setText(LOCATII_CAMERE[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnData.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                dataSelectata = day + "/" + (month + 1) + "/" + year;
                tvData.setText(dataSelectata);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnSalveaza.setOnClickListener(v -> {
            String locatie = etLocatie.getText().toString().trim();
            String persoaneStr = etPersoane.getText().toString().trim();

            if (locatie.isEmpty() || dataSelectata.isEmpty() || persoaneStr.isEmpty()) {
                Toast.makeText(this, "Completati toate campurile!", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            btnSalveaza.setEnabled(false);

            Detectie d = new Detectie();
            d.camera = spinnerCamera.getSelectedItem().toString();
            d.locatie = locatie;
            d.data = dataSelectata;
            d.persoane = Integer.parseInt(persoaneStr);
            d.confidenta = ratingBar.getRating() / 5f;
            d.alerta = cbAlerta.isChecked();
            d.activ = swActiv.isChecked();

            new DatabaseHelper(this).adaugaDetectie(d);

            progressBar.postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                setResult(RESULT_OK);
                finish();
            }, 600);
        });
    }
}
