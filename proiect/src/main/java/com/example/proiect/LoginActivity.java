package com.example.proiect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etParola = findViewById(R.id.etParola);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvMergiLaRegister = findViewById(R.id.tvMergiLaRegister);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String parola = etParola.getText().toString().trim();

            if (username.isEmpty() || parola.isEmpty()) {
                Toast.makeText(this, "Completati toate campurile!", Toast.LENGTH_SHORT).show();
                return;
            }

            String numeOperator = db.loginUser(username, parola);
            if (numeOperator != null) {
                getSharedPreferences("dronewatch_prefs", MODE_PRIVATE)
                        .edit()
                        .putString("operator", numeOperator)
                        .apply();
                db.adaugaLog("Autentificare", username + " s-a conectat");
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Username sau parola incorecte!", Toast.LENGTH_SHORT).show();
            }
        });

        tvMergiLaRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }
}
