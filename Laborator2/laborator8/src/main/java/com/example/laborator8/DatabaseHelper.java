package com.example.laborator8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "phones.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "phones";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BRAND = "brand";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_PRICE = "price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BRAND + " TEXT, " +
                COLUMN_MODEL + " TEXT, " +
                COLUMN_PRICE + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 1. Metoda de inserare
    public long insertPhone(Phone phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BRAND, phone.getBrand());
        values.put(COLUMN_MODEL, phone.getModel());
        values.put(COLUMN_PRICE, phone.getPrice());
        return db.insert(TABLE_NAME, null, values);
    }

    // 2. Metoda de selectie a tuturor inregistrarilor
    public List<Phone> getAllPhones() {
        List<Phone> phoneList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Phone phone = new Phone();
                phone.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                phone.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)));
                phone.setModel(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL)));
                phone.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                phoneList.add(phone);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return phoneList;
    }

    // 3. Metoda de selectie a obiectului care are valoarea string (brand) egala cu o valoare primita
    public List<Phone> getPhonesByBrand(String brand) {
        List<Phone> phoneList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_BRAND + "=?", new String[]{brand}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Phone phone = new Phone();
                phone.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                phone.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)));
                phone.setModel(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL)));
                phone.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                phoneList.add(phone);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return phoneList;
    }

    // selectie in fct de interval pret
    public List<Phone> getPhonesInRange(double min, double max) {
        List<Phone> phoneList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_PRICE + " BETWEEN ? AND ?", new String[]{String.valueOf(min), String.valueOf(max)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Phone phone = new Phone();
                phone.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                phone.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)));
                phone.setModel(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODEL)));
                phone.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                phoneList.add(phone);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return phoneList;
    }

    public int deletePhoneByModel(String model) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_MODEL + " = ?", new String[]{model});
    }

    public void incrementPriceForBrandsStartingWith(String letter) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_PRICE + " = " + COLUMN_PRICE + " + 1 WHERE " + COLUMN_BRAND + " LIKE ?";
        db.execSQL(sql, new Object[]{letter + "%"});
    }
}
