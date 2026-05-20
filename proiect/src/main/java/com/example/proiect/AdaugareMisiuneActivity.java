package com.example.proiect;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdaugareMisiuneActivity extends AppCompatActivity {

    private static final String JSON_URL =
            "https://raw.githubusercontent.com/iuli3/dw-data/main/text.json";

    private String dataSelectata = "";
    private Spinner spinnerZona;
    private ProgressBar pbZone;
    private TextView tvRisc;
    private TextView tvDescriereZona;
    private List<TipZona> listaZone = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaugare_misiune);

        spinnerZona = findViewById(R.id.spinnerZona);
        pbZone = findViewById(R.id.pbZone);
        tvRisc = findViewById(R.id.tvRisc);
        tvDescriereZona = findViewById(R.id.tvDescriereZona);
        EditText etNume = findViewById(R.id.etNumeMisiune);
        EditText etOraStart = findViewById(R.id.etOraStart);
        EditText etOraStop = findViewById(R.id.etOraStop);
        Switch swActiv = findViewById(R.id.swActivMisiune);
        CheckBox cbAlertaAuto = findViewById(R.id.cbAlertaAuto);
        CheckBox cbRecurenta = findViewById(R.id.cbRecurenta);
        RatingBar ratingPrioritate = findViewById(R.id.ratingPrioritate);
        Button btnData = findViewById(R.id.btnSelecteazaDataMisiune);
        TextView tvData = findViewById(R.id.tvDataSelectataMisiune);
        Button btnSalveaza = findViewById(R.id.btnSalveazaMisiune);

        incarcaTipuriZone();

        spinnerZona.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (!listaZone.isEmpty() && position < listaZone.size()) {
                    TipZona zona = listaZone.get(position);
                    tvRisc.setText("Risc: " + zona.risc);
                    tvDescriereZona.setText(zona.descriere);

                    int culoare;
                    switch (zona.risc) {
                        case "ridicat": culoare = 0xFFE53935; break;
                        case "mediu":   culoare = 0xFFFB8C00; break;
                        default:        culoare = 0xFF43A047; break;
                    }
                    tvRisc.setTextColor(culoare);
                }
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        btnData.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                dataSelectata = day + "/" + (month + 1) + "/" + year;
                tvData.setText(dataSelectata);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnSalveaza.setOnClickListener(v -> {
            String nume = etNume.getText().toString().trim();
            String oraStart = etOraStart.getText().toString().trim();
            String oraStop = etOraStop.getText().toString().trim();

            if (nume.isEmpty()) {
                Toast.makeText(this, "Introduceti numele misiunii!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dataSelectata.isEmpty()) {
                Toast.makeText(this, "Selectati o data!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (oraStart.isEmpty() || oraStop.isEmpty()) {
                Toast.makeText(this, "Introduceti intervalul orar!", Toast.LENGTH_SHORT).show();
                return;
            }

            Misiune m = new Misiune();
            m.nume = nume;
            m.tipZona = spinnerZona.getSelectedItem() != null
                    ? spinnerZona.getSelectedItem().toString() : "Necunoscut";
            m.data = dataSelectata;
            m.oraStart = oraStart;
            m.oraStop = oraStop;
            m.activ = swActiv.isChecked();
            m.alertaAuto = cbAlertaAuto.isChecked();
            m.recurenta = cbRecurenta.isChecked();
            m.prioritate = ratingPrioritate.getRating();

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.adaugaMisiune(m);
            dbHelper.adaugaLog("Misiune adaugata", m.nume + " — " + m.tipZona);
            Toast.makeText(this, "Misiune salvata!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }

    private void incarcaTipuriZone() {
        pbZone.setVisibility(View.VISIBLE);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            List<TipZona> zone = fetchTipuriZone();
            handler.post(() -> {
                pbZone.setVisibility(View.GONE);
                listaZone = zone;
                ArrayAdapter<TipZona> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, listaZone);
                spinnerZona.setAdapter(adapter);
            });
        });
    }

    private List<TipZona> fetchTipuriZone() {
        try {
            URL url = new URL(JSON_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();
                return parseZoneJson(sb.toString());
            }
        } catch (Exception ignored) {}
        return fallbackTipuri();
    }

    private List<TipZona> parseZoneJson(String json) {
        List<TipZona> tipuri = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray zone = obj.getJSONArray("zone");
            for (int i = 0; i < zone.length(); i++) {
                JSONObject z = zone.getJSONObject(i);
                tipuri.add(new TipZona(
                        z.getString("tip"),
                        z.getString("risc"),
                        z.getString("descriere")
                ));
            }
        } catch (Exception e) {
            return fallbackTipuri();
        }
        return tipuri.isEmpty() ? fallbackTipuri() : tipuri;
    }

    private List<TipZona> fallbackTipuri() {
        List<TipZona> list = new ArrayList<>();
        list.add(new TipZona("Perimetru militar", "ridicat", "Zone restrictionate, acces controlat strict"));
        list.add(new TipZona("Aeroport", "ridicat", "Infrastructura critica, monitorizare intensiva"));
        list.add(new TipZona("Port fluvial", "mediu", "Zona de tranzit marfuri si persoane"));
        list.add(new TipZona("Stadion", "mediu", "Evenimente cu aglomerare mare de persoane"));
        list.add(new TipZona("Gara", "mediu", "Nod de transport, flux continuu de calatori"));
        list.add(new TipZona("Depozit logistic", "scazut", "Zone industriale de stocare si distributie"));
        return list;
    }
}