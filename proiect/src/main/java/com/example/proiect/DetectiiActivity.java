package com.example.proiect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetectiiActivity extends AppCompatActivity {

    private static final int REQ_ADAUGA = 1;

    private DatabaseHelper db;
    private DetectieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detectii);

        db = new DatabaseHelper(this);
        ListView lvDetectii = findViewById(R.id.lvDetectii);
        Button btnAdauga = findViewById(R.id.btnAdaugaDetectie);

        adapter = new DetectieAdapter(this, db.getToateDetectiile());
        lvDetectii.setAdapter(adapter);

        lvDetectii.setOnItemLongClickListener((parent, view, position, id) -> {
            Detectie d = adapter.getItem(position);
            db.stergeDetectie(d.id);
            adapter.remove(d);
            Toast.makeText(this, "Detectie stearsa", Toast.LENGTH_SHORT).show();
            return true;
        });

        btnAdauga.setOnClickListener(v ->
                startActivityForResult(new Intent(this, AdaugareDetectieActivity.class), REQ_ADAUGA));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADAUGA && resultCode == RESULT_OK) {
            adapter.clear();
            adapter.addAll(db.getToateDetectiile());
        }
    }
}
