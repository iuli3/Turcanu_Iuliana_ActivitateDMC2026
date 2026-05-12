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
        List<Misiune> misiuni = db.getToateMisiunile();

        int totalMisiuni = misiuni.size();
        int active = 0;
        int inactive = 0;
        Map<String, Integer> perZona = new LinkedHashMap<>();

        for (Misiune m : misiuni) {
            if (m.activ) active++; else inactive++;
            String zona = (m.tipZona != null && !m.tipZona.isEmpty()) ? m.tipZona : "Altele";
            String label = zona.length() > 9 ? zona.substring(0, 7) + ".." : zona;
            perZona.put(label, perZona.containsKey(label) ? perZona.get(label) + 1 : 1);
        }

        // Demo data when DB is empty, so the chart is never blank
        if (perZona.isEmpty()) {
            perZona.put("Piata", 3);
            perZona.put("Industr.", 5);
            perZona.put("Rezid.", 2);
            perZona.put("Intrsc.", 4);
        }

        TextView tvTotal = findViewById(R.id.tvTotalDetectii);
        TextView tvActive = findViewById(R.id.tvTotalPersoane);
        TextView tvInactive = findViewById(R.id.tvTotalAlerte);
        StatisticiView grafic = findViewById(R.id.graficDetectii);

        tvTotal.setText("Total misiuni: " + totalMisiuni);
        tvActive.setText("Misiuni active: " + active);
        tvInactive.setText("Misiuni inactive: " + inactive);
        grafic.setTitlu("Misiuni pe tip zona");
        grafic.setDate(perZona);
    }
}
