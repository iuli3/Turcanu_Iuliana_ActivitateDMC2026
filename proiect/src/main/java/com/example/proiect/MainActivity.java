package com.example.proiect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS = "dronewatch_prefs";
    private static final String KEY_OPERATOR = "operator";
    private static final String KEY_NOTIF = "notificari";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etOperator = findViewById(R.id.etUtilizator);
        Switch swNotificari = findViewById(R.id.swNotificari);
        Button btnSalveaza = findViewById(R.id.btnSalveazaSetari);
        Button btnMisiuni = findViewById(R.id.btnMisiuni);
        Button btnHarta = findViewById(R.id.btnHarta);
        Button btnStatistici = findViewById(R.id.btnStatistici);
        Button btnAlerteActive = findViewById(R.id.btnAlerteActive);
        Button btnRaportZilnic = findViewById(R.id.btnRaportZilnic);
        Button btnProfilOperator = findViewById(R.id.btnProfilOperator);
        Button btnLogActivitate = findViewById(R.id.btnLogActivitate);
        Button btnSetariAvansate = findViewById(R.id.btnSetariAvansate);
        TextView tvBunVenit = findViewById(R.id.tvBunVenit);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String operatorSalvat = prefs.getString(KEY_OPERATOR, "");
        boolean notificariSalvate = prefs.getBoolean(KEY_NOTIF, true);

        etOperator.setText(operatorSalvat);
        swNotificari.setChecked(notificariSalvate);
        if (!operatorSalvat.isEmpty()) {
            tvBunVenit.setText("Bun venit, " + operatorSalvat + "!");
        }

        btnSalveaza.setOnClickListener(v -> {
            String operator = etOperator.getText().toString().trim();
            if (operator.isEmpty()) {
                Toast.makeText(this, "Introduceti numele operatorului!", Toast.LENGTH_SHORT).show();
                return;
            }
            prefs.edit()
                    .putString(KEY_OPERATOR, operator)
                    .putBoolean(KEY_NOTIF, swNotificari.isChecked())
                    .apply();
            tvBunVenit.setText("Bun venit, " + operator + "!");
            Toast.makeText(this, "Setari salvate!", Toast.LENGTH_SHORT).show();
        });

        btnMisiuni.setOnClickListener(v ->
                startActivity(new Intent(this, MisiuniActivity.class)));
        btnHarta.setOnClickListener(v ->
                startActivity(new Intent(this, HartaActivity.class)));
        btnStatistici.setOnClickListener(v ->
                startActivity(new Intent(this, StatisticiActivity.class)));
        btnAlerteActive.setOnClickListener(v ->
                startActivity(new Intent(this, AlerteActiveActivity.class)));
        btnRaportZilnic.setOnClickListener(v ->
                startActivity(new Intent(this, RaportZilnicActivity.class)));
        btnProfilOperator.setOnClickListener(v ->
                startActivity(new Intent(this, ProfilOperatorActivity.class)));
        btnLogActivitate.setOnClickListener(v ->
                startActivity(new Intent(this, LogActivitateActivity.class)));
        btnSetariAvansate.setOnClickListener(v ->
                startActivity(new Intent(this, SetariAvansateActivity.class)));
    }
}
