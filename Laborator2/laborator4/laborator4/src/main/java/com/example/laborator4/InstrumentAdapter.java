package com.example.laborator4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.laborator4.InstrumentMuzical;

import java.util.List;


public class InstrumentAdapter extends ArrayAdapter<InstrumentMuzical.InstrumentTehnic> {
    public InstrumentAdapter(Context context, List<InstrumentMuzical.InstrumentTehnic> instrumente) {
        super(context, 0, instrumente);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstrumentMuzical.InstrumentTehnic instrument = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_instrument, parent, false);
        }

        TextView tvNume = convertView.findViewById(R.id.tvNumeItem);
        TextView tvDetalii = convertView.findViewById(R.id.tvDetaliiItem);
        TextView tvPret = convertView.findViewById(R.id.tvPretItem);

        tvNume.setText(instrument.denumire);
        tvDetalii.setText("Serie: " + instrument.serie + " | " + instrument.stare);
        tvPret.setText(instrument.pret + " RON");

        return convertView;
    }
}
