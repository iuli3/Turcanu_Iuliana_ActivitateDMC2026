package com.example.proiect;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        EditText etNume = findViewById(R.id.etNumeOperator);
        EditText etUsername = findViewById(R.id.etUsernameRegister);
        EditText etParola = findViewById(R.id.etParolaRegister);
        EditText etConfirma = findViewById(R.id.etConfirmaParola);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String nume = etNume.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String parola = etParola.getText().toString().trim();
            String confirma = etConfirma.getText().toString().trim();

            if (nume.isEmpty() || username.isEmpty() || parola.isEmpty() || confirma.isEmpty()) {
                Toast.makeText(this, "Completati toate campurile!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!parola.equals(confirma)) {
                Toast.makeText(this, "Parolele nu coincid!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (parola.length() < 4) {
                Toast.makeText(this, "Parola trebuie sa aiba minim 4 caractere!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean ok = db.registerUser(username, parola, nume);
            if (ok) {
                Toast.makeText(this, "Cont creat cu succes! Va puteti autentifica.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Username-ul este deja folosit!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
