package com.example.proiect;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatisticiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistici);

        DatabaseHelper db = new DatabaseHelper(this);

        // ── Misiuni ──────────────────────────────────────────────────────────
        List<Misiune> misiuni = db.getToateMisiunile();
        int misiuniTotal = misiuni.size();
        int misiuniActive = 0, misiuniInactive = 0;
        Map<String, Integer> perZona = new LinkedHashMap<>();
        for (Misiune m : misiuni) {
            if (m.activ) misiuniActive++; else misiuniInactive++;
            String zona = (m.tipZona != null && !m.tipZona.isEmpty()) ? m.tipZona : "Altele";
            String label = zona.length() > 9 ? zona.substring(0, 7) + ".." : zona;
            perZona.put(label, perZona.containsKey(label) ? perZona.get(label) + 1 : 1);
        }

        // ── Camere ───────────────────────────────────────────────────────────
        List<CameraInfo> camereStd = CameraInfo.getCamere();
        List<CameraInfo> camereCustom = db.getCamereCustom();
        int camereTotal = camereStd.size() + camereCustom.size();
        int camereActive = 0;
        for (CameraInfo c : camereStd) if (c.active) camereActive++;
        for (CameraInfo c : camereCustom) if (c.active) camereActive++;

        // ── Alerte ───────────────────────────────────────────────────────────
        List<Detectie> detectii = db.getToateDetectiile();
        int alerteTotal = 0, alerteActive = 0;
        Map<String, Integer> perCamera = new LinkedHashMap<>();
        for (Detectie d : detectii) {
            if (d.alerta) {
                alerteTotal++;
                if (d.activ) alerteActive++;
                String cam = (d.camera != null && !d.camera.isEmpty()) ? d.camera : "?";
                perCamera.put(cam, perCamera.containsKey(cam) ? perCamera.get(cam) + 1 : 1);
            }
        }

        // ── Populare views ───────────────────────────────────────────────────
        ((TextView) findViewById(R.id.tvMisiuniTotal)).setText("Total: " + misiuniTotal);
        ((TextView) findViewById(R.id.tvMisiuniActive)).setText("Active: " + misiuniActive);
        ((TextView) findViewById(R.id.tvMisiuniInactive)).setText("Inactive: " + misiuniInactive);

        ((TextView) findViewById(R.id.tvCamereTotal)).setText("Total: " + camereTotal);
        ((TextView) findViewById(R.id.tvCamereActive)).setText("Active (live): " + camereActive);

        ((TextView) findViewById(R.id.tvAlerteTotal)).setText("Total evenimente: " + alerteTotal);
        ((TextView) findViewById(R.id.tvAlerteActive)).setText("Alerte active: " + alerteActive);

        StatisticiView graficZone = findViewById(R.id.graficZone);
        graficZone.setTitlu("Misiuni pe tip zona");
        if (!perZona.isEmpty()) graficZone.setDate(perZona);

        StatisticiView graficAlerte = findViewById(R.id.graficAlerte);
        graficAlerte.setTitlu("Alerte pe camera");
        if (!perCamera.isEmpty()) graficAlerte.setDate(perCamera);
    }
}
