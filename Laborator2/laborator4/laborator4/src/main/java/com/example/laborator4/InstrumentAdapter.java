package com.example.laborator4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.laborator4.InstrumentMuzical;

import java.util.List;
import com.example.laborator4.R;


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
        TextView tvStatus = convertView.findViewById(R.id.tvStatusItem);

        if (instrument != null) {
            tvNume.setText(instrument.denumire);
            tvPret.setText(instrument.pret + " RON");

            String detalii = "Serie: " + instrument.serie + " | Stare: " + instrument.stare;
            tvDetalii.setText(detalii);

            String statusText = (instrument.esteValid ? "Disponibil" : "Stoc epuizat");
            if (instrument.dataAchizitie != null) {
                statusText += " | Achiziționat la: " + instrument.dataAchizitie.toString();
            }
            tvStatus.setText(statusText);
            tvStatus.setTextColor(instrument.esteValid ?
                    getContext().getResources().getColor(android.R.color.holo_green_dark) :
                    getContext().getResources().getColor(android.R.color.holo_red_dark));
        }

        return convertView;
    }
}
