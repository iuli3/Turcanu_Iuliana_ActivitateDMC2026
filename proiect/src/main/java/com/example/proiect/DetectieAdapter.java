package com.example.proiect;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DetectieAdapter extends ArrayAdapter<Detectie> {

    public DetectieAdapter(Context context, List<Detectie> detectii) {
        super(context, 0, detectii);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_detectie, parent, false);
        }
        Detectie d = getItem(position);

        TextView tvCamera = convertView.findViewById(R.id.tvCamera);
        TextView tvLocatie = convertView.findViewById(R.id.tvLocatie);
        TextView tvData = convertView.findViewById(R.id.tvData);
        TextView tvPersoane = convertView.findViewById(R.id.tvPersoane);
        TextView tvAlerta = convertView.findViewById(R.id.tvAlerta);

        tvCamera.setText(d.camera);
        tvLocatie.setText(d.locatie);
        tvData.setText(d.data);
        tvPersoane.setText(d.persoane + " persoane");

        if (d.alerta) {
            tvAlerta.setText("ALERTA");
            tvAlerta.setTextColor(Color.RED);
        } else {
            tvAlerta.setText("OK");
            tvAlerta.setTextColor(Color.parseColor("#4CAF50"));
        }

        return convertView;
    }
}
