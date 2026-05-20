package com.example.proiect;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherHelper {

    private static final String API_KEY = "WEATHER_KEY";

    private static final String GEO_URL =
            "https://dataservice.accuweather.com/locations/v1/cities/geoposition/search";
    private static final String CONDITIONS_URL =
            "https://dataservice.accuweather.com/currentconditions/v1/";
    private static final String FORECAST_URL =
            "https://dataservice.accuweather.com/forecasts/v1/daily/1day/";

    public interface WeatherCallback {
        void onResult(String rezultat);
    }

    public static void getWeather(double lat, double lng, WeatherCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            String rezultat;
            try {
                // obtinere city key din coordonate
                String geoUrl = GEO_URL + "?apikey=" + API_KEY
                        + "&q=" + lat + "," + lng;
                String geoResponse = makeRequest(geoUrl);
                JSONObject location = new JSONObject(geoResponse);
                String cityKey = location.getString("Key");

                // temp+stare
                String condUrl = CONDITIONS_URL + cityKey + "?apikey=" + API_KEY + "&metric=true";
                String condResponse = makeRequest(condUrl);
                JSONArray conditions = new JSONArray(condResponse);
                JSONObject current = conditions.getJSONObject(0);

                String weatherText = current.getString("WeatherText");
                double temp = current.getJSONObject("Temperature")
                        .getJSONObject("Metric").getDouble("Value");

                // forecast min max
                String forecastUrl = FORECAST_URL + cityKey + "?apikey=" + API_KEY + "&metric=true";
                String forecastResponse = makeRequest(forecastUrl);
                JSONObject forecast = new JSONObject(forecastResponse);
                JSONObject azi = forecast.getJSONArray("DailyForecasts").getJSONObject(0);
                double min = azi.getJSONObject("Temperature").getJSONObject("Minimum").getDouble("Value");
                double max = azi.getJSONObject("Temperature").getJSONObject("Maximum").getDouble("Value");

                rezultat = String.format("%.1f°C — %s\nMin: %.1f°C  Max: %.1f°C", temp, weatherText, min, max);
            } catch (Exception e) {
                rezultat = "Indisponibil";
            }

            String finalRezultat = rezultat;
            handler.post(() -> callback.onResult(finalRezultat));
        });
    }

    private static String makeRequest(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        reader.close();
        conn.disconnect();
        return sb.toString();
    }
}
