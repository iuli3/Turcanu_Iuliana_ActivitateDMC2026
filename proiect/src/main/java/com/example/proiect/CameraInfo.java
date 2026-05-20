package com.example.proiect;

import com.google.android.gms.maps.model.LatLng;
import java.util.Arrays;
import java.util.List;

public class CameraInfo {
    public String id;
    public String name;
    public String location;
    public boolean active;
    public LatLng position;
    public String streamUrl;
    public int dbId = -1;

    public CameraInfo(String id, String name, String location, boolean active,
                      LatLng position, String streamUrl) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.active = active;
        this.position = position;
        this.streamUrl = streamUrl;
    }

    public static List<CameraInfo> getCamere() {
        return Arrays.asList(
            new CameraInfo("CAM-01", "Piata Romana", "Piata Romana, Bucuresti",
                true, new LatLng(44.4479, 26.0971),
                "https://webcamromania.ro/webcam-orase/webcam-piata-romana-bucuresti/"),
            new CameraInfo("CAM-02", "Piata Sfatului", "Piata Sfatului, Brasov",
                true, new LatLng(45.6428, 25.5887),
                "https://webcamromania.ro/webcam-orase/webcam-brasov/"),
            new CameraInfo("CAM-03", "Piata Unirii", "Piata Unirii, Cluj-Napoca",
                true, new LatLng(46.7699, 23.5899),
                "https://webcamromania.ro/webcam-orase/webcam-cluj-napoca/"),
            new CameraInfo("CAM-04", "Centru Suceava", "Centru, Suceava",
                true, new LatLng(47.6515, 26.2573),
                "https://webcamromania.ro/webcam-orase/webcam-suceava-centru/"),
            new CameraInfo("CAM-05", "Gura Humorului", "Centru, Gura Humorului",
                true, new LatLng(47.5519, 25.8889),
                "https://webcamromania.ro/webcam-orase/webcam-gura-humorului/"),
            new CameraInfo("CAM-06", "Centru Slobozia", "Centru, Slobozia",
                false, new LatLng(44.5625, 27.3683),
                "https://webcamromania.ro/webcam-orase/webcam-slobozia/")
        );
    }
}
