package com.example.proiect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "dronewatch.db";
    private static final int DB_VERSION = 2;

    // ── Table: detectii (kept for backward compat) ────────────────────────
    static final String TABLE = "detectii";
    static final String COL_ID = "_id";
    static final String COL_CAMERA = "camera";
    static final String COL_LOCATIE = "locatie";
    static final String COL_DATA = "data";
    static final String COL_PERSOANE = "persoane";
    static final String COL_CONFIDENTA = "confidenta";
    static final String COL_ALERTA = "alerta";
    static final String COL_ACTIV = "activ";

    // ── Table: misiuni ────────────────────────────────────────────────────
    static final String TABLE_MISIUNI = "misiuni";
    static final String COL_NUME = "nume";
    static final String COL_TIP_ZONA = "tip_zona";
    static final String COL_ORA_START = "ora_start";
    static final String COL_ORA_STOP = "ora_stop";
    static final String COL_PRIORITATE = "prioritate";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CAMERA + " TEXT, " +
                COL_LOCATIE + " TEXT, " +
                COL_DATA + " TEXT, " +
                COL_PERSOANE + " INTEGER, " +
                COL_CONFIDENTA + " REAL, " +
                COL_ALERTA + " INTEGER, " +
                COL_ACTIV + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_MISIUNI + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NUME + " TEXT, " +
                COL_TIP_ZONA + " TEXT, " +
                COL_DATA + " TEXT, " +
                COL_ORA_START + " TEXT, " +
                COL_ORA_STOP + " TEXT, " +
                COL_ACTIV + " INTEGER, " +
                COL_PRIORITATE + " REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MISIUNI + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NUME + " TEXT, " +
                    COL_TIP_ZONA + " TEXT, " +
                    COL_DATA + " TEXT, " +
                    COL_ORA_START + " TEXT, " +
                    COL_ORA_STOP + " TEXT, " +
                    COL_ACTIV + " INTEGER, " +
                    COL_PRIORITATE + " REAL)");
        }
    }

    // ── Detectie CRUD ─────────────────────────────────────────────────────
    public long adaugaDetectie(Detectie d) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CAMERA, d.camera);
        cv.put(COL_LOCATIE, d.locatie);
        cv.put(COL_DATA, d.data);
        cv.put(COL_PERSOANE, d.persoane);
        cv.put(COL_CONFIDENTA, d.confidenta);
        cv.put(COL_ALERTA, d.alerta ? 1 : 0);
        cv.put(COL_ACTIV, d.activ ? 1 : 0);
        return db.insert(TABLE, null, cv);
    }

    public List<Detectie> getToateDetectiile() {
        List<Detectie> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE, null, null, null, null, null, COL_ID + " DESC");
        while (c.moveToNext()) {
            Detectie d = new Detectie();
            d.id = c.getInt(c.getColumnIndexOrThrow(COL_ID));
            d.camera = c.getString(c.getColumnIndexOrThrow(COL_CAMERA));
            d.locatie = c.getString(c.getColumnIndexOrThrow(COL_LOCATIE));
            d.data = c.getString(c.getColumnIndexOrThrow(COL_DATA));
            d.persoane = c.getInt(c.getColumnIndexOrThrow(COL_PERSOANE));
            d.confidenta = c.getFloat(c.getColumnIndexOrThrow(COL_CONFIDENTA));
            d.alerta = c.getInt(c.getColumnIndexOrThrow(COL_ALERTA)) == 1;
            d.activ = c.getInt(c.getColumnIndexOrThrow(COL_ACTIV)) == 1;
            lista.add(d);
        }
        c.close();
        return lista;
    }

    public void stergeDetectie(int id) {
        getWritableDatabase().delete(TABLE, COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    // ── Misiune CRUD ──────────────────────────────────────────────────────
    public long adaugaMisiune(Misiune m) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NUME, m.nume);
        cv.put(COL_TIP_ZONA, m.tipZona);
        cv.put(COL_DATA, m.data);
        cv.put(COL_ORA_START, m.oraStart);
        cv.put(COL_ORA_STOP, m.oraStop);
        cv.put(COL_ACTIV, m.activ ? 1 : 0);
        cv.put(COL_PRIORITATE, m.prioritate);
        return db.insert(TABLE_MISIUNI, null, cv);
    }

    public List<Misiune> getToateMisiunile() {
        List<Misiune> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_MISIUNI, null, null, null, null, null,
                COL_ID + " DESC");
        while (c.moveToNext()) {
            Misiune m = new Misiune();
            m.id = c.getInt(c.getColumnIndexOrThrow(COL_ID));
            m.nume = c.getString(c.getColumnIndexOrThrow(COL_NUME));
            m.tipZona = c.getString(c.getColumnIndexOrThrow(COL_TIP_ZONA));
            m.data = c.getString(c.getColumnIndexOrThrow(COL_DATA));
            m.oraStart = c.getString(c.getColumnIndexOrThrow(COL_ORA_START));
            m.oraStop = c.getString(c.getColumnIndexOrThrow(COL_ORA_STOP));
            m.activ = c.getInt(c.getColumnIndexOrThrow(COL_ACTIV)) == 1;
            m.prioritate = c.getFloat(c.getColumnIndexOrThrow(COL_PRIORITATE));
            lista.add(m);
        }
        c.close();
        return lista;
    }

    public void stergeMisiune(int id) {
        getWritableDatabase().delete(TABLE_MISIUNI, COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }
}
