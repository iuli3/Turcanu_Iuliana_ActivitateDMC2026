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
    private RadioButton rbAcustic, rbElectric;
    private Spinner spinnerStare;
    private RatingBar ratingBar;
    private Button btnSalveaza;
    private DatePicker datePicker;
    private SwitchCompat switchOferta;
    private ToggleButton toggleVanzare;

    private int pozitie = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaugare_instrument);

        etNume = findViewById(R.id.etNume);
        etSerie = findViewById(R.id.etAnFabricatie);
        etPret = findViewById(R.id.etPret);
        cbEsteValid = findViewById(R.id.cbDisponibil);
        rbAcustic = findViewById(R.id.rbAcustic);
        rbElectric = findViewById(R.id.rbElectric);
        spinnerStare = findViewById(R.id.spinnerCategorie);
        ratingBar = findViewById(R.id.ratingBar);
        btnSalveaza = findViewById(R.id.btnSalveaza);
        datePicker = findViewById(R.id.datePicker);
        switchOferta = findViewById(R.id.switchOferta);
        toggleVanzare = findViewById(R.id.toggleVanzare);

        ArrayAdapter<InstrumentMuzical.StareInstrument> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                InstrumentMuzical.StareInstrument.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStare.setAdapter(adapter);

        if (getIntent().hasExtra("instrument_edit")) {
            InstrumentMuzical.InstrumentTehnic instrument = getIntent().getParcelableExtra("instrument_edit");
            pozitie = getIntent().getIntExtra("pozitie", -1);

            if (instrument != null) {
                etNume.setText(instrument.denumire);
                etSerie.setText(String.valueOf(instrument.serie));
                etPret.setText(String.valueOf(instrument.pret));
                cbEsteValid.setChecked(instrument.esteValid);
                spinnerStare.setSelection(instrument.stare.ordinal());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && instrument.dataAchizitie != null) {
                    datePicker.updateDate(
                            instrument.dataAchizitie.getYear(),
                            instrument.dataAchizitie.getMonthValue() - 1,
                            instrument.dataAchizitie.getDayOfMonth()
                    );
                }
            }
        }

        btnSalveaza.setOnClickListener(v -> {
            String denumire = etNume.getText().toString().trim();
            String serieStr = etSerie.getText().toString().trim();
            String pretStr = etPret.getText().toString().trim();

            if (denumire.isEmpty() || serieStr.isEmpty() || pretStr.isEmpty()) {
                Toast.makeText(this, "Completați toate câmpurile!", Toast.LENGTH_SHORT).show();
                return;
            }

            LocalDate dataAchizitie = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dataAchizitie = LocalDate.of(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
            }

            InstrumentMuzical.InstrumentTehnic instrument = new InstrumentMuzical.InstrumentTehnic(
                    denumire,
                    Integer.parseInt(serieStr),
                    cbEsteValid.isChecked(),
                    Double.parseDouble(pretStr),
                    (InstrumentMuzical.StareInstrument) spinnerStare.getSelectedItem(),
                    dataAchizitie
            );

            Intent rezultat = new Intent();
            rezultat.putExtra("instrument_key", instrument);
            rezultat.putExtra("pozitie", pozitie);

            setResult(RESULT_OK, rezultat);
            finish();
        });
    }
}