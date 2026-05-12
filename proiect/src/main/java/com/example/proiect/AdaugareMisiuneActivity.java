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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdaugareMisiuneActivity extends AppCompatActivity {

    private static final String JSON_URL =
            "https://raw.githubusercontent.com/turcanuiuliana/dronewatch-data/main/zone.json";

    private String dataSelectata = "";
    private Spinner spinnerZona;
    private ProgressBar pbZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaugare_misiune);

        spinnerZona = findViewById(R.id.spinnerZona);
        pbZone = findViewById(R.id.pbZone);
        EditText etNume = findViewById(R.id.etNumeMisiune);
        EditText etOraStart = findViewById(R.id.etOraStart);
        EditText etOraStop = findViewById(R.id.etOraStop);
        Switch swActiv = findViewById(R.id.swActivMisiune);
        RatingBar ratingPrioritate = findViewById(R.id.ratingPrioritate);
        Button btnData = findViewById(R.id.btnSelecteazaDataMisiune);
        TextView tvData = findViewById(R.id.tvDataSelectataMisiune);
        Button btnSalveaza = findViewById(R.id.btnSalveazaMisiune);

        incarcaTipuriZone();

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
            m.prioritate = ratingPrioritate.getRating();

            new DatabaseHelper(this).adaugaMisiune(m);
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
            List<String> tipuri = fetchTipuriZone();
            handler.post(() -> {
                pbZone.setVisibility(View.GONE);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, tipuri);
                spinnerZona.setAdapter(adapter);
            });
        });
    }

    private List<String> fetchTipuriZone() {
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

    private List<String> parseZoneJson(String json) {
        List<String> tipuri = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray zone = obj.getJSONArray("zone");
            for (int i = 0; i < zone.length(); i++) {
                tipuri.add(zone.getJSONObject(i).getString("tip"));
            }
        } catch (Exception e) {
            return fallbackTipuri();
        }
        return tipuri.isEmpty() ? fallbackTipuri() : tipuri;
    }

    private List<String> fallbackTipuri() {
        return new ArrayList<>(Arrays.asList(
                "Piata publica",
                "Perimetru industrial",
                "Zona rezidentiala",
                "Intersectie rutiera",
                "Parc public",
                "Cladire institutionala"
        ));
    }
}
