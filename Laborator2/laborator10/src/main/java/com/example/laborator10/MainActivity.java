package com.example.laborator10;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "API_KEY";
    private static final String CITY_SEARCH_URL = "https://dataservice.accuweather.com/locations/v1/cities/search";
    private static final String FORECAST_BASE_URL = "https://dataservice.accuweather.com/forecasts/v1/daily/";

    private EditText etCity;
    private TextView tvResult;
    private Spinner spinnerDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        tvResult = findViewById(R.id.tvResult);
        spinnerDays = findViewById(R.id.spinnerDays);
        Button btnSearch = findViewById(R.id.btnSearch);

        // spinner
        String[] options = {"1 zi", "5 zile"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> {
            String city = etCity.getText().toString().trim();
            if (city.isEmpty()) {
                Toast.makeText(this, "Introduceti un oras", Toast.LENGTH_SHORT).show();
                return;
            }
            new CitySearchTask().execute(city);
        });
    }
    private String getSelectedEndpoint() {
        int pos = spinnerDays.getSelectedItemPosition();
        switch (pos) {
            case 1: return "5day";
            default: return "1day";
        }
    }

    // cautare oras+ obtinere cod
    private class CitySearchTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String urlStr = CITY_SEARCH_URL + "?apikey=" + API_KEY
                        + "&q=" + params[0].replace(" ", "%20");
                String response = makeRequest(urlStr);
                JSONArray results = new JSONArray(response);
                if (results.length() == 0) return null;
                return results.getJSONObject(0).getString("Key");
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String cityKey) {
            if (cityKey == null) {
                tvResult.setText("nu exista");
                return;
            }
            tvResult.setText("cod oras (Key): " + cityKey );
            new ForecastTask().execute(cityKey);
        }
    }

    // obtine forecast
    private class ForecastTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String endpoint = getSelectedEndpoint(); // "1day" sau "5day"
                String urlStr = FORECAST_BASE_URL + endpoint + "/" + params[0]
                        + "?apikey=" + API_KEY + "&metric=true";

                String response = makeRequest(urlStr);

                JSONObject json = new JSONObject(response);
                JSONArray dailyForecasts = json.getJSONArray("DailyForecasts");

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < dailyForecasts.length(); i++) {
                    JSONObject day = dailyForecasts.getJSONObject(i);

                    // extragere data
                    String date = day.getString("Date").substring(0, 10);

                    JSONObject temperature = day.getJSONObject("Temperature");
                    double min = temperature.getJSONObject("Minimum").getDouble("Value");
                    double max = temperature.getJSONObject("Maximum").getDouble("Value");

                    sb.append(date).append("\n")
                            .append("   Min: ").append(min).append(" °C\n")
                            .append("   Max: ").append(max).append(" °C\n\n");
                }
                return sb.toString().trim();

            } catch (Exception e) {
                return "err: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            String current = tvResult.getText().toString();
            tvResult.setText(current + "\n\n" + result);
        }
    }

    // http helper
    private String makeRequest(String urlStr) throws Exception {
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