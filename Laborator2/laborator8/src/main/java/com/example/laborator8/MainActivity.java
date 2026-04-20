package com.example.laborator8;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etBrand, etModel, etPrice, etSearchBrand, etMinPrice, etMaxPrice, etDeleteModel, etUpdateLetter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        etBrand = findViewById(R.id.etBrand);
        etModel = findViewById(R.id.etModel);
        etPrice = findViewById(R.id.etPrice);
        etSearchBrand = findViewById(R.id.etSearchBrand);
        etMinPrice = findViewById(R.id.etMinPrice);
        etMaxPrice = findViewById(R.id.etMaxPrice);
        etDeleteModel = findViewById(R.id.etDeleteModel);
        etUpdateLetter = findViewById(R.id.etUpdateLetter);
        listView = findViewById(R.id.listView);

        Button btnInsert = findViewById(R.id.btnInsert);
        Button btnShowAll = findViewById(R.id.btnShowAll);
        Button btnSearchBrand = findViewById(R.id.btnSearchBrand);
        Button btnFilterRange = findViewById(R.id.btnFilterRange);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnUpdate = findViewById(R.id.btnUpdate);

        btnInsert.setOnClickListener(v -> {
            String brand = etBrand.getText().toString();
            String model = etModel.getText().toString();
            String priceStr = etPrice.getText().toString();
            if (!brand.isEmpty() && !model.isEmpty() && !priceStr.isEmpty()) {
                double price = Double.parseDouble(priceStr);
                Phone phone = new Phone(brand, model, price);
                dbHelper.insertPhone(phone);
                Toast.makeText(this, "Phone Saved!", Toast.LENGTH_SHORT).show();
                etBrand.setText("");
                etModel.setText("");
                etPrice.setText("");
                showAll();
            }
        });

        btnShowAll.setOnClickListener(v -> showAll());

        btnSearchBrand.setOnClickListener(v -> {
            String brand = etSearchBrand.getText().toString();
            if (!brand.isEmpty()) {
                List<Phone> phones = dbHelper.getPhonesByBrand(brand);
                updateListView(phones);
            }
        });

        btnFilterRange.setOnClickListener(v -> {
            String minStr = etMinPrice.getText().toString();
            String maxStr = etMaxPrice.getText().toString();
            if (!minStr.isEmpty() && !maxStr.isEmpty()) {
                double min = Double.parseDouble(minStr);
                double max = Double.parseDouble(maxStr);
                List<Phone> phones = dbHelper.getPhonesInRange(min, max);
                updateListView(phones);
            }
        });

        btnDelete.setOnClickListener(v -> {
            String model = etDeleteModel.getText().toString();
            if (!model.isEmpty()) {
                dbHelper.deletePhoneByModel(model);
                Toast.makeText(this, "Model deleted!", Toast.LENGTH_SHORT).show();
                etDeleteModel.setText("");
                showAll();
            }
        });

        btnUpdate.setOnClickListener(v -> {
            String letter = etUpdateLetter.getText().toString();
            if (!letter.isEmpty()) {
                dbHelper.incrementPriceForBrandsStartingWith(letter);
                showAll();
            }
        });

        showAll();
    }

    private void showAll() {
        List<Phone> phones = dbHelper.getAllPhones();
        updateListView(phones);
    }

    private void updateListView(List<Phone> phones) {
        ArrayAdapter<Phone> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, phones);
        listView.setAdapter(adapter);
    }
}
