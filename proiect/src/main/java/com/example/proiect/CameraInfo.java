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
    public int persoanAzi;
    public String oraDeParf;
    public int[] ultimele8Ore;
    public String streamUrl;

    public CameraInfo(String id, String name, String location, boolean active,
                      LatLng position, int persoanAzi, String oraDeParf,
                      int[] ultimele8Ore, String streamUrl) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.active = active;
        this.position = position;
        this.persoanAzi = persoanAzi;
        this.oraDeParf = oraDeParf;
        this.ultimele8Ore = ultimele8Ore;
        this.streamUrl = streamUrl;
    }

    public static List<CameraInfo> getCamere() {
        return Arrays.asList(
            new CameraInfo("CAM-01", "Intrare Principala", "Sector Nord, Poarta A",
                true, new LatLng(44.4638, 26.1106), 45, "09:00",
                new int[]{3, 8, 15, 12, 5, 2, 0, 0},
                "https://www.youtube.com/embed/rnXIjl_Rzy4"),
            new CameraInfo("CAM-02", "Flancul Stang", "Sector Vest, Perimetru",
                true, new LatLng(44.4628, 26.1126), 23, "14:00",
                new int[]{0, 2, 3, 5, 8, 5, 0, 0},
                "https://www.youtube.com/embed/83VPsAPWiME"),
            new CameraInfo("CAM-03", "Iesire Spate", "Sector Sud, Poarta B",
                false, new LatLng(44.4618, 26.1106), 0, "—",
                new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                ""),
            new CameraInfo("CAM-04", "Flancul Drept", "Sector Est, Perimetru",
                true, new LatLng(44.4628, 26.1086), 31, "11:00",
                new int[]{0, 5, 10, 12, 3, 1, 0, 0},
                "https://www.youtube.com/embed/wUQc3RoLAPs")
        );
    }
}
