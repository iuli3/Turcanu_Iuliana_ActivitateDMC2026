package com.example.proiect;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AlerteActiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerte_active);

        ListView lvAlerte = findViewById(R.id.lvAlerte);
        TextView tvNrAlerte = findViewById(R.id.tvNrAlerte);

        DatabaseHelper db = new DatabaseHelper(this);
        List<Misiune> misiuniCuAlerta = db.getMisiuniCuAlerta();

        tvNrAlerte.setText(misiuniCuAlerta.size() + " alerte active");

        MisiuneAdapter adapter = new MisiuneAdapter(this, misiuniCuAlerta);
        lvAlerte.setAdapter(adapter);

        lvAlerte.setOnItemLongClickListener((parent, view, position, id) -> {
            Misiune m = adapter.getItem(position);
            db.dismissAlertaMisiune(m.id);
            adapter.remove(m);
            tvNrAlerte.setText(adapter.getCount() + " alerte active");
            return true;
        });
    }
}
