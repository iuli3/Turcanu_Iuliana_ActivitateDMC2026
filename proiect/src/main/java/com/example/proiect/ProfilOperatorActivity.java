package com.example.proiect;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ProfilOperatorActivity extends AppCompatActivity {

    private static final String PREFS = "dronewatch_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_operator);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String operator = prefs.getString("operator", "Nesetat");
        boolean notificari = prefs.getBoolean("notificari", true);

        DatabaseHelper db = new DatabaseHelper(this);
        List<Misiune> misiuni = db.getToateMisiunile();
        List<Detectie> detectii = db.getToateDetectiile();

        int totalMisiuni = misiuni.size();
        int misiuniActive = 0;
        int misiuniCuAlerta = 0;
        for (Misiune m : misiuni) {
            if (m.activ) misiuniActive++;
            if (m.alertaAuto) misiuniCuAlerta++;
        }

        int totalDetectii = detectii.size();
        int totalAlerte = 0;
        for (Detectie d : detectii) {
            if (d.alerta) totalAlerte++;
        }

        int totalLog = db.getLoguri().size();

        TextView tvNume = findViewById(R.id.tvNumeOperator);
        TextView tvInitiale = findViewById(R.id.tvInitiale);
        TextView tvNotificari = findViewById(R.id.tvNotificariStatus);
        TextView tvTotalMisiuni = findViewById(R.id.tvStatMisiuni);
        TextView tvMisiuniActive = findViewById(R.id.tvStatActive);
        TextView tvTotalDetectii = findViewById(R.id.tvStatDetectii);
        TextView tvTotalAlerte = findViewById(R.id.tvStatAlerte);
        TextView tvTotalActiuni = findViewById(R.id.tvStatActiuni);

        tvNume.setText(operator);

        String initiale = operator.trim().isEmpty() ? "?" :
                String.valueOf(operator.trim().charAt(0)).toUpperCase();
        tvInitiale.setText(initiale);

        tvNotificari.setText("Notificari: " + (notificari ? "Activate" : "Dezactivate"));
        tvTotalMisiuni.setText(String.valueOf(totalMisiuni));
        tvMisiuniActive.setText(String.valueOf(misiuniActive));
        tvTotalDetectii.setText(String.valueOf(totalDetectii));
        tvTotalAlerte.setText(String.valueOf(totalAlerte));
        tvTotalActiuni.setText(String.valueOf(totalLog));
    }
}
