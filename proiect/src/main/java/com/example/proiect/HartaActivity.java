package com.example.proiect;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HartaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private List<CameraInfo> camere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harta);

        camere = CameraInfo.getCamere();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng centru = new LatLng(44.4628, 26.1106);

        googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(44.4648, 26.1086))
                .add(new LatLng(44.4648, 26.1126))
                .add(new LatLng(44.4608, 26.1126))
                .add(new LatLng(44.4608, 26.1086))
                .strokeColor(0xFF1565C0)
                .fillColor(0x181565C0)
                .strokeWidth(3f));

        for (CameraInfo cam : camere) {
            float hue = cam.active
                    ? BitmapDescriptorFactory.HUE_GREEN
                    : BitmapDescriptorFactory.HUE_RED;
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(cam.position)
                    .title(cam.id + " – " + cam.name)
                    .snippet(cam.active ? "LIVE" : "OFFLINE")
                    .icon(BitmapDescriptorFactory.defaultMarker(hue)));
            if (marker != null) marker.setTag(cam);
        }

        googleMap.setOnMarkerClickListener(marker -> {
            Object tag = marker.getTag();
            if (tag instanceof CameraInfo) {
                showCameraDialog((CameraInfo) tag);
                return true;
            }
            return false;
        });

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centru, 17f));
    }

    private void showCameraDialog(CameraInfo cam) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_camera_detalii);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.92);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView tvId = dialog.findViewById(R.id.tvCameraId);
        TextView tvNume = dialog.findViewById(R.id.tvCameraNume);
        TextView tvStatus = dialog.findViewById(R.id.tvCameraStatus);
        TextView tvLocatie = dialog.findViewById(R.id.tvCameraLocatie);
        TextView tvPersoane = dialog.findViewById(R.id.tvCameraPersoane);
        TextView tvVarf = dialog.findViewById(R.id.tvCameraVarf);
        StatisticiView grafic = dialog.findViewById(R.id.graficMiniCamera);
        Button btnStream = dialog.findViewById(R.id.btnVeziStream);
        Button btnInchide = dialog.findViewById(R.id.btnInchideDialog);

        tvId.setText(cam.id);
        tvNume.setText(cam.name);
        tvLocatie.setText(cam.location);

        if (cam.active) {
            tvStatus.setText("● LIVE");
            tvStatus.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            tvStatus.setText("● OFFLINE");
            tvStatus.setTextColor(Color.parseColor("#C62828"));
        }

        tvPersoane.setText("Persoane detectate azi: " + cam.persoanAzi);
        tvVarf.setText("Ora de varf: " + cam.oraDeParf);

        Map<String, Integer> dateGrafic = new LinkedHashMap<>();
        String[] ore = {"07h", "08h", "09h", "10h", "11h", "12h", "13h", "14h"};
        for (int i = 0; i < cam.ultimele8Ore.length && i < ore.length; i++) {
            dateGrafic.put(ore[i], cam.ultimele8Ore[i]);
        }
        grafic.setTitlu("Persoane per ora");
        grafic.setDate(dateGrafic);

        btnStream.setEnabled(cam.active && !cam.streamUrl.isEmpty());
        btnStream.setAlpha(cam.active && !cam.streamUrl.isEmpty() ? 1f : 0.5f);
        btnStream.setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraStreamActivity.class);
            intent.putExtra("url", cam.streamUrl);
            intent.putExtra("nume", cam.name);
            startActivity(intent);
            dialog.dismiss();
        });

        btnInchide.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
