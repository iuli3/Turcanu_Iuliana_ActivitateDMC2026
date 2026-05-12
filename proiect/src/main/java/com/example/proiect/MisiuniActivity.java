package com.example.proiect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MisiuniActivity extends AppCompatActivity {

    private static final int REQ_ADAUGA = 10;
    private DatabaseHelper db;
    private MisiuneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misiuni);

        db = new DatabaseHelper(this);
        ListView lvMisiuni = findViewById(R.id.lvMisiuni);
        Button btnAdauga = findViewById(R.id.btnAdaugaMisiune);

        adapter = new MisiuneAdapter(this, db.getToateMisiunile());
        lvMisiuni.setAdapter(adapter);

        lvMisiuni.setOnItemLongClickListener((parent, view, position, id) -> {
            Misiune m = adapter.getItem(position);
            db.stergeMisiune(m.id);
            adapter.remove(m);
            Toast.makeText(this, "Misiune stearsa", Toast.LENGTH_SHORT).show();
            return true;
        });

        btnAdauga.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(this, AdaugareMisiuneActivity.class), REQ_ADAUGA));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADAUGA && resultCode == RESULT_OK) {
            adapter.clear();
            adapter.addAll(db.getToateMisiunile());
        }
    }
}
