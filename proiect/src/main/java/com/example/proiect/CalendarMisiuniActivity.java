package com.example.proiect;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalendarMisiuniActivity extends AppCompatActivity {

    private static final String[] LUNI = {
            "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie",
            "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"
    };

    private MisiuneAdapter misiuneAdapter;
    private List<Misiune> toateMisiunile;
    private Set<String> dateCuMisiuni;
    private TextView tvZiSelectata;
    private TextView tvLunaAn;
    private GridView gridCalendar;
    private Calendar calendarAfisat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_misiuni);

        tvZiSelectata = findViewById(R.id.tvZiSelectata);
        tvLunaAn = findViewById(R.id.tvLunaAn);
        gridCalendar = findViewById(R.id.gridCalendar);
        ListView lvMisiuniZi = findViewById(R.id.lvMisiuniZi);
        Button btnPrev = findViewById(R.id.btnLunaPrecedenta);
        Button btnNext = findViewById(R.id.btnLunaUrmatoare);

        toateMisiunile = new DatabaseHelper(this).getToateMisiunile();
        misiuneAdapter = new MisiuneAdapter(this, new ArrayList<>());
        lvMisiuniZi.setAdapter(misiuneAdapter);

        dateCuMisiuni = new HashSet<>();
        for (Misiune m : toateMisiunile) {
            dateCuMisiuni.add(m.data);
        }

        calendarAfisat = Calendar.getInstance();
        actualizeazaGrid();

        Calendar azi = Calendar.getInstance();
        filtreazaPentruData(azi.get(Calendar.DAY_OF_MONTH),
                azi.get(Calendar.MONTH) + 1,
                azi.get(Calendar.YEAR));

        btnPrev.setOnClickListener(v -> {
            calendarAfisat.add(Calendar.MONTH, -1);
            actualizeazaGrid();
        });

        btnNext.setOnClickListener(v -> {
            calendarAfisat.add(Calendar.MONTH, 1);
            actualizeazaGrid();
        });
    }

    private void actualizeazaGrid() {
        int luna = calendarAfisat.get(Calendar.MONTH);
        int an = calendarAfisat.get(Calendar.YEAR);
        tvLunaAn.setText(LUNI[luna] + " " + an);

        Calendar primaZi = Calendar.getInstance();
        primaZi.set(an, luna, 1);
        // DAY_OF_WEEK: 1=Dum, 2=Lun, ..., 7=Sam → offset luni-first
        int offset = (primaZi.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        int zileLuna = primaZi.getActualMaximum(Calendar.DAY_OF_MONTH);

        final List<Integer> cells = new ArrayList<>();
        for (int i = 0; i < offset; i++) cells.add(null);
        for (int d = 1; d <= zileLuna; d++) cells.add(d);
        while (cells.size() % 7 != 0) cells.add(null);

        gridCalendar.setAdapter(new CalendarGridAdapter(this, cells, luna + 1, an));

        gridCalendar.setOnItemClickListener((parent, view, position, id) -> {
            Integer zi = cells.get(position);
            if (zi != null) filtreazaPentruData(zi, luna + 1, an);
        });
    }

    private void filtreazaPentruData(int zi, int luna, int an) {
        String dataStr = zi + "/" + luna + "/" + an;
        List<Misiune> filtrate = new ArrayList<>();
        for (Misiune m : toateMisiunile) {
            if (dataStr.equals(m.data)) filtrate.add(m);
        }
        misiuneAdapter.clear();
        misiuneAdapter.addAll(filtrate);
        if (filtrate.isEmpty()) {
            tvZiSelectata.setText("Nicio misiune pentru " + dataStr);
        } else {
            tvZiSelectata.setText("Misiuni pentru " + dataStr + " (" + filtrate.size() + "):");
        }
    }

    private class CalendarGridAdapter extends BaseAdapter {
        private final Context ctx;
        private final List<Integer> cells;
        private final int luna;
        private final int an;

        CalendarGridAdapter(Context ctx, List<Integer> cells, int luna, int an) {
            this.ctx = ctx;
            this.cells = cells;
            this.luna = luna;
            this.an = an;
        }

        @Override public int getCount() { return cells.size(); }
        @Override public Object getItem(int pos) { return cells.get(pos); }
        @Override public long getItemId(int pos) { return pos; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout cell = new LinearLayout(ctx);
            cell.setOrientation(LinearLayout.VERTICAL);
            cell.setGravity(Gravity.CENTER);
            cell.setPadding(4, 10, 4, 10);

            TextView tvZi = new TextView(ctx);
            tvZi.setGravity(Gravity.CENTER);
            tvZi.setTextSize(14);

            View dot = new View(ctx);
            LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(10, 10);
            dotParams.gravity = Gravity.CENTER_HORIZONTAL;
            dotParams.topMargin = 4;
            dot.setLayoutParams(dotParams);

            Integer zi = cells.get(position);
            if (zi != null) {
                tvZi.setText(String.valueOf(zi));
                tvZi.setTextColor(Color.parseColor("#37474F"));

                String dataStr = zi + "/" + luna + "/" + an;

                // Bulina albastra daca exista misiuni in ziua respectiva
                if (dateCuMisiuni.contains(dataStr)) {
                    GradientDrawable bulina = new GradientDrawable();
                    bulina.setShape(GradientDrawable.OVAL);
                    bulina.setColor(Color.parseColor("#1565C0"));
                    dot.setBackground(bulina);
                }

                // Highlight ziua de azi cu cerc albastru pe numar
                Calendar azi = Calendar.getInstance();
                if (zi == azi.get(Calendar.DAY_OF_MONTH)
                        && luna == azi.get(Calendar.MONTH) + 1
                        && an == azi.get(Calendar.YEAR)) {
                    GradientDrawable bg = new GradientDrawable();
                    bg.setShape(GradientDrawable.OVAL);
                    bg.setColor(Color.parseColor("#1565C0"));
                    tvZi.setBackground(bg);
                    tvZi.setTextColor(Color.WHITE);
                    tvZi.setPadding(14, 6, 14, 6);
                }
            }

            cell.addView(tvZi);
            cell.addView(dot);
            return cell;
        }
    }
}
