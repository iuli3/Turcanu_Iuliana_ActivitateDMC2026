package com.example.laborator4;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.time.LocalDate;

public class AdaugareInstrument extends AppCompatActivity {

    private EditText etNume, etSerie, etPret;
    private CheckBox cbEsteValid;
    private RadioButton rbAcustic;
    private Spinner spinnerStare;
    private RatingBar ratingBar;
    private Button btnSalveaza;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaugare_instrument);

        etNume = findViewById(R.id.etNume);
        etSerie= findViewById(R.id.etAnFabricatie);
        etPret= findViewById(R.id.etPret);
        cbEsteValid= findViewById(R.id.cbDisponibil);
        rbAcustic= findViewById(R.id.rbAcustic);
        spinnerStare= findViewById(R.id.spinnerCategorie);
        ratingBar= findViewById(R.id.ratingBar);
        btnSalveaza= findViewById(R.id.btnSalveaza);
        datePicker = findViewById(R.id.datePicker);

        SwitchCompat switchOferta= findViewById(R.id.switchOferta);
        ToggleButton toggleVanzare= findViewById(R.id.toggleVanzare);

        ArrayAdapter<InstrumentMuzical.StareInstrument> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                InstrumentMuzical.StareInstrument.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStare.setAdapter(adapter);

        btnSalveaza.setOnClickListener(v -> {
            String denumire = etNume.getText().toString().trim();
            String serieStr = etSerie.getText().toString().trim();
            String pretStr  = etPret.getText().toString().trim();

            LocalDate dataAchizitie = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dataAchizitie = LocalDate.of(
                        datePicker.getYear(),
                        datePicker.getMonth() + 1,  // luna e 0-indexed
                        datePicker.getDayOfMonth()
                );
            }

            if (denumire.isEmpty() || serieStr.isEmpty() || pretStr.isEmpty()) {
                Toast.makeText(this, "Completati toate campurile!", Toast.LENGTH_SHORT).show();
                return;
            }

            //lab5
            InstrumentMuzical.InstrumentTehnic instrument = new InstrumentMuzical.InstrumentTehnic(
                    denumire,
                    Integer.parseInt(serieStr),
                    cbEsteValid.isChecked(),
                    Double.parseDouble(pretStr),
                    (InstrumentMuzical.StareInstrument) spinnerStare.getSelectedItem(),
                    dataAchizitie
            );

            Intent rezultat = new Intent();
            rezultat.putExtra("denumire", instrument.denumire);
            rezultat.putExtra("serie", instrument.serie);
            rezultat.putExtra("pret", instrument.pret);
            rezultat.putExtra("esteValid", instrument.esteValid);
            rezultat.putExtra("stare", instrument.stare.toString());
            rezultat.putExtra("dataAchizitie", instrument.dataAchizitie.toString());
            rezultat.putExtra("rating", ratingBar.getRating());
            rezultat.putExtra("tipSunet", rbAcustic.isChecked() ? "Acustic" : "Electric");
            rezultat.putExtra("oferta", switchOferta.isChecked());
            rezultat.putExtra("vanzare", toggleVanzare.isChecked());


            setResult(RESULT_OK, rezultat);
            finish();
        });
    }
}