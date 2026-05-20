package com.example.proiect;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LogActivitateActivity extends AppCompatActivity {

    private LogAdapter adapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_activitate);

        db = new DatabaseHelper(this);
        ListView lvLog = findViewById(R.id.lvLog);
        TextView tvNrInregistrari = findViewById(R.id.tvNrInregistrari);
        Button btnStergeLog = findViewById(R.id.btnStergeLog);

        List<LogEntry> loguri = db.getLoguri();
        tvNrInregistrari.setText(loguri.size() + " inregistrari");

        adapter = new LogAdapter(this, loguri);
        lvLog.setAdapter(adapter);

        btnStergeLog.setOnClickListener(v -> {
            db.stergeToateLogurile();
            adapter.clear();
            tvNrInregistrari.setText("0 inregistrari");
            Toast.makeText(this, "Jurnal sters", Toast.LENGTH_SHORT).show();
        });
    }

    private static class LogAdapter extends ArrayAdapter<LogEntry> {

        LogAdapter(Context context, List<LogEntry> loguri) {
            super(context, 0, loguri);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_log, parent, false);
            }
            LogEntry e = getItem(position);

            View indicator = convertView.findViewById(R.id.viewIndicator);
            TextView tvActiune = convertView.findViewById(R.id.tvLogActiune);
            TextView tvDetalii = convertView.findViewById(R.id.tvLogDetalii);
            TextView tvTimestamp = convertView.findViewById(R.id.tvLogTimestamp);

            tvActiune.setText(e.actiune);
            tvDetalii.setText(e.detalii);
            tvTimestamp.setText(e.timestamp);

            convertView.setBackgroundColor(position % 2 == 0
                    ? Color.parseColor("blue")
                    : Color.parseColor("red"));

            int culoare;
            if (e.actiune.toLowerCase().contains("sters") ||
                    e.actiune.toLowerCase().contains("sters")) {
                culoare = Color.parseColor("#C62828");
            } else if (e.actiune.toLowerCase().contains("adaugat") ||
                    e.actiune.toLowerCase().contains("salvat")) {
                culoare = Color.parseColor("#2E7D32");
            } else if (e.actiune.toLowerCase().contains("setari")) {
                culoare = Color.parseColor("#1565C0");
            } else {
                culoare = Color.parseColor("#F57C00");
            }
            indicator.setBackgroundColor(culoare);



            return convertView;
        }
    }
}
