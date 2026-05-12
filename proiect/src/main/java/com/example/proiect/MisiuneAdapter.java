package com.example.proiect;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MisiuneAdapter extends ArrayAdapter<Misiune> {

    public MisiuneAdapter(Context context, List<Misiune> misiuni) {
        super(context, 0, misiuni);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_misiune, parent, false);
        }
        Misiune m = getItem(position);

        TextView tvNume = convertView.findViewById(R.id.tvNumeMisiune);
        TextView tvStatus = convertView.findViewById(R.id.tvStatusMisiune);
        TextView tvTipZona = convertView.findViewById(R.id.tvTipZona);
        TextView tvPrioritate = convertView.findViewById(R.id.tvPrioritate);
        TextView tvData = convertView.findViewById(R.id.tvDataMisiune);
        TextView tvInterval = convertView.findViewById(R.id.tvIntervalMisiune);

        tvNume.setText(m.nume);
        tvTipZona.setText(m.tipZona);
        tvData.setText(m.data);
        tvInterval.setText(m.oraStart + " – " + m.oraStop);

        StringBuilder stele = new StringBuilder();
        int p = Math.round(m.prioritate);
        for (int i = 0; i < 5; i++) stele.append(i < p ? "★" : "☆");
        tvPrioritate.setText(stele.toString());

        if (m.activ) {
            tvStatus.setText("● ACTIV");
            tvStatus.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            tvStatus.setText("● INACTIV");
            tvStatus.setTextColor(Color.GRAY);
        }

        return convertView;
    }
}
