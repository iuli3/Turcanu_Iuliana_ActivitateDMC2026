package com.example.proiect;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;


public class RaportZilnicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raport_zilnic);

        Calendar azi = Calendar.getInstance();
        String dataAzi = azi.get(Calendar.DAY_OF_MONTH) + "/" +
                (azi.get(Calendar.MONTH) + 1) + "/" +
                azi.get(Calendar.YEAR);

        String[] LUNI = {"Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie",
                "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"};
        String dataFormatata = azi.get(Calendar.DAY_OF_MONTH) + " " +
                LUNI[azi.get(Calendar.MONTH)] + " " + azi.get(Calendar.YEAR);

        DatabaseHelper db = new DatabaseHelper(this);
        List<Misiune> toateMisiunile = db.getToateMisiunile();
        List<CameraInfo> camere = CameraInfo.getCamere();

        int misiuniAzi = 0;
        int misiuniActiveAzi = 0;
        for (Misiune m : toateMisiunile) {
            if (dataAzi.equals(m.data)) {
                misiuniAzi++;
                if (m.activ) misiuniActiveAzi++;
            }
        }

        int camereActive = 0;
        for (CameraInfo c : camere) {
            if (c.active) camereActive++;
        }

        TextView tvData = findViewById(R.id.tvDataRaport);
        TextView tvMisiuniAzi = findViewById(R.id.tvRaportMisiuniAzi);
        TextView tvMisiuniActive = findViewById(R.id.tvRaportMisiuniActive);
        TextView tvCamereActive = findViewById(R.id.tvRaportCamere);
        TextView tvStatusGeneral = findViewById(R.id.tvStatusGeneral);

        tvData.setText("Raport pentru " + dataFormatata);
        tvMisiuniAzi.setText(String.valueOf(misiuniAzi));
        tvMisiuniActive.setText(String.valueOf(misiuniActiveAzi));
        tvCamereActive.setText(camereActive + " / " + camere.size());

        if (misiuniActiveAzi > 0) {
            tvStatusGeneral.setText("Operational — " + misiuniActiveAzi + " misiuni active azi");
            tvStatusGeneral.setTextColor(0xFF2E7D32);
        } else {
            tvStatusGeneral.setText("Nicio activitate inregistrata azi");
            tvStatusGeneral.setTextColor(0xFF546E7A);
        }
    }
}
