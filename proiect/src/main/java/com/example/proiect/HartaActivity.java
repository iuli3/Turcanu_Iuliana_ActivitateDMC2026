package com.example.proiect;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class HartaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private List<CameraInfo> camere;
    private GoogleMap googleMap;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harta);

        db = new DatabaseHelper(this);
        camere = new ArrayList<>(CameraInfo.getCamere());
        camere.addAll(db.getCamereCustom());

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        FloatingActionButton fab = findViewById(R.id.fabAdaugaCamera);
        fab.setOnClickListener(v ->
                Toast.makeText(this, "Apasati lung pe harta pentru a alege locatia camerei",
                        Toast.LENGTH_LONG).show());
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        LatLng romania = new LatLng(45.9432, 24.9668);

        for (CameraInfo cam : camere) {
            addMarkerForCamera(cam);
        }

        map.setOnMarkerClickListener(marker -> {
            Object tag = marker.getTag();
            if (tag instanceof CameraInfo) {
                showCameraDialog((CameraInfo) tag);
                return true;
            }
            return false;
        });

        map.setOnMapLongClickListener(latLng ->
                showAdaugaCameraDialog(latLng));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(romania, 7f));
    }

    private void addMarkerForCamera(CameraInfo cam) {
        float hue;
        if (cam.dbId != -1) {
            hue = BitmapDescriptorFactory.HUE_AZURE;
        } else if (cam.active) {
            hue = BitmapDescriptorFactory.HUE_GREEN;
        } else {
            hue = BitmapDescriptorFactory.HUE_RED;
        }

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(cam.position)
                .title(cam.id + " – " + cam.name)
                .snippet(cam.location)
                .icon(BitmapDescriptorFactory.defaultMarker(hue)));

        if (marker != null) marker.setTag(cam);
    }

    private void showAdaugaCameraDialog(LatLng latLng) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_adauga_camera_custom);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.92);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView tvCoord = dialog.findViewById(R.id.tvCoordonate);
        TextInputEditText etNume = dialog.findViewById(R.id.etNumeCamera);
        TextInputEditText etLocatie = dialog.findViewById(R.id.etLocatieCamera);
        TextInputEditText etUrl = dialog.findViewById(R.id.etStreamUrl);
        Button btnSalveaza = dialog.findViewById(R.id.btnSalveazaCamera);
        Button btnAnuleaza = dialog.findViewById(R.id.btnAnuleazaCamera);

        String coordText = String.format("Lat: %.5f, Lng: %.5f", latLng.latitude, latLng.longitude);
        tvCoord.setText(coordText);

        btnSalveaza.setOnClickListener(v -> {
            String nume = etNume.getText() != null ? etNume.getText().toString().trim() : "";
            String locatie = etLocatie.getText() != null ? etLocatie.getText().toString().trim() : "";
            String url = etUrl.getText() != null ? etUrl.getText().toString().trim() : "";

            if (nume.isEmpty()) {
                Toast.makeText(this, "Introduceti numele camerei!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (locatie.isEmpty()) {
                Toast.makeText(this, "Introduceti descrierea locatiei!", Toast.LENGTH_SHORT).show();
                return;
            }

            long id = db.adaugaCameraCustom(nume, locatie, latLng.latitude, latLng.longitude, url);
            db.adaugaLog("Camera adaugata", nume + " la " + coordText);

            CameraInfo nouaCam = new CameraInfo(
                    "CAM-" + (camere.size() + 1), nume, locatie, true, latLng, url);
            nouaCam.dbId = (int) id;
            camere.add(nouaCam);
            addMarkerForCamera(nouaCam);

            Toast.makeText(this, "Camera adaugata cu succes!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnAnuleaza.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
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

        TextView tvId      = dialog.findViewById(R.id.tvCameraId);
        TextView tvNume    = dialog.findViewById(R.id.tvCameraNume);
        TextView tvStatus  = dialog.findViewById(R.id.tvCameraStatus);
        TextView tvLocatie = dialog.findViewById(R.id.tvCameraLocatie);
        TextView tvVreme   = dialog.findViewById(R.id.tvVreme);
        Button btnStream   = dialog.findViewById(R.id.btnVeziStream);
        Button btnInchide  = dialog.findViewById(R.id.btnInchideDialog);

        tvId.setText(cam.id);
        tvNume.setText(cam.name);
        tvLocatie.setText(cam.location);

        WeatherHelper.getWeather(cam.position.latitude, cam.position.longitude,
                rezultat -> tvVreme.setText(rezultat));

        if (cam.dbId != -1) {
            tvStatus.setText("● CUSTOM");
            tvStatus.setTextColor(Color.parseColor("#1565C0"));
        } else if (cam.active) {
            tvStatus.setText("● LIVE");
            tvStatus.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            tvStatus.setText("● OFFLINE");
            tvStatus.setTextColor(Color.parseColor("#C62828"));
        }

        boolean canStream = !cam.streamUrl.isEmpty();
        btnStream.setEnabled(canStream);
        btnStream.setAlpha(canStream ? 1f : 0.45f);
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
