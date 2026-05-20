package com.example.proiect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "dronewatch.db";
    private static final int DB_VERSION = 7;

    //useri
    static final String TABLE_UTILIZATORI = "utilizatori";
    static final String COL_USERNAME = "username";
    static final String COL_PAROLA = "parola";
    static final String COL_NUME_OP = "nume_operator";

    //camere
    static final String TABLE_CAMERE_CUSTOM = "camere_custom";
    static final String COL_LAT = "lat";
    static final String COL_LNG = "lng";
    static final String COL_STREAM_URL = "stream_url";


    //detectii- inval
    static final String TABLE = "detectii";
    static final String COL_ID = "_id";
    static final String COL_CAMERA = "camera";
    static final String COL_LOCATIE = "locatie";
    static final String COL_DATA = "data";
    static final String COL_PERSOANE = "persoane";
    static final String COL_CONFIDENTA = "confidenta";
    static final String COL_ALERTA = "alerta";
    static final String COL_ACTIV = "activ";

    //misiuni
    static final String TABLE_MISIUNI = "misiuni";
    static final String COL_NUME = "nume";
    static final String COL_TIP_ZONA = "tip_zona";
    static final String COL_ORA_START = "ora_start";
    static final String COL_ORA_STOP = "ora_stop";
    static final String COL_PRIORITATE = "prioritate";
    static final String COL_ALERTA_AUTO = "alerta_auto";
    static final String COL_RECURENTA = "recurenta";

    //tabel logs
    static final String TABLE_LOG = "log_activitate";
    static final String COL_ACTIUNE = "actiune";
    static final String COL_DETALII = "detalii";
    static final String COL_TIMESTAMP = "timestamp";

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
                COL_PRIORITATE + " REAL, " +
                COL_ALERTA_AUTO + " INTEGER DEFAULT 0, " +
                COL_RECURENTA + " INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE " + TABLE_LOG + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ACTIUNE + " TEXT, " +
                COL_DETALII + " TEXT, " +
                COL_TIMESTAMP + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_UTILIZATORI + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PAROLA + " TEXT, " +
                COL_NUME_OP + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_CAMERE_CUSTOM + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NUME + " TEXT, " +
                COL_LOCATIE + " TEXT, " +
                COL_LAT + " REAL, " +
                COL_LNG + " REAL, " +
                COL_STREAM_URL + " TEXT)");

        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        //insert data
        //useri
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_UTILIZATORI +
                " (username, parola, nume_operator) VALUES ('admin','admin','Administrator')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_UTILIZATORI +
                " (username, parola, nume_operator) VALUES ('operator1','1234','Ionescu Maria')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_UTILIZATORI +
                " (username, parola, nume_operator) VALUES ('operator2','1234','Popescu Dan')");

        //misiuni
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Patrulare zona industriala','Industrial','14/5/2026','06:00','14:00',0,4.0,1,0)");
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Supraveghere depozit vest','Depozit','15/5/2026','20:00','06:00',0,5.0,1,0)");
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Control acces perimetru','Urban','15/5/2026','08:00','16:00',0,3.5,0,1)");
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Monitorizare parc central','Parc','16/5/2026','07:00','19:00',0,2.5,0,0)");
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Patrulare zona rezidentiala','Rezidential','17/5/2026','22:00','06:00',0,3.0,1,1)");


        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Patrulare zona nord','Urban','18/5/2026','08:00','16:00',1,4.5,1,1)");
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Monitorizare perimetru est','Industrial','18/5/2026','06:00','22:00',1,5.0,1,0)");
        // MAINE (19/5/2026)
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Control acces depozit central','Depozit','19/5/2026','07:00','19:00',1,4.0,1,1)");
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Supraveghere zona sud','Rezidential','19/5/2026','20:00','06:00',1,3.5,0,0)");
        // viitor
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Patrulare noapte parc','Parc','20/5/2026','22:00','06:00',1,2.5,0,0)");
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Monitorizare trafic intrare','Urban','21/5/2026','06:00','22:00',1,3.0,1,1)");
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Inspectie zona industriala','Industrial','23/5/2026','08:00','17:00',1,4.5,1,0)");
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Supraveghere depozit nord','Depozit','25/5/2026','00:00','23:59',1,5.0,1,1)");
        db.execSQL("INSERT INTO " + TABLE_MISIUNI +
                " (nume, tip_zona, data, ora_start, ora_stop, activ, prioritate, alerta_auto, recurenta)" +
                " VALUES ('Patrulare perimetru complet','Urban','27/5/2026','07:00','15:00',1,4.0,0,1)");

        //alerte
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-04','Poarta Vest','2026-05-14 01:33',0,0.82,1,0)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-06','Depozit Sud','2026-05-14 03:10',0,0.90,1,0)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-02','Parcare Vest','2026-05-15 22:45',0,0.76,0,0)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-01','Intrare Nord','2026-05-15 04:18',0,0.94,1,0)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-03','Perimetru Est','2026-05-16 00:52',0,0.87,1,0)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-05','Depozit Central','2026-05-16 11:30',0,0.71,0,0)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-02','Parcare Vest','2026-05-17 23:08',0,0.78,1,0)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-05','Depozit Central','2026-05-17 14:30',0,0.95,1,0)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-01','Intrare Nord','2026-05-17 02:17',0,0.88,1,0)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-01','Intrare Nord','2026-05-18 07:42',0,0.91,1,1)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-03','Perimetru Est','2026-05-18 09:15',0,0.85,1,1)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-06','Depozit Sud','2026-05-18 11:47',0,0.93,1,1)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-02','Parcare Vest','2026-05-18 13:05',0,0.69,0,1)");
        // MAINE
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-04','Poarta Vest','2026-05-19 02:00',0,0.88,1,1)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-01','Intrare Nord','2026-05-19 06:30',0,0.92,1,1)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-05','Depozit Central','2026-05-19 08:15',0,0.80,1,1)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-03','Perimetru Est','2026-05-20 01:12',0,0.96,1,1)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-06','Depozit Sud','2026-05-21 03:44',0,0.89,1,1)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-02','Parcare Vest','2026-05-23 22:00',0,0.74,0,1)");
        db.execSQL("INSERT INTO " + TABLE +
                " (camera, locatie, data, persoane, confidenta, alerta, activ)" +
                " VALUES ('CAM-01','Intrare Nord','2026-05-25 04:55',0,0.91,1,1)");

        // loguri
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Sistem pornit','Aplicatie initializata cu date demo','2026-05-14 06:00')");
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Alerta detectata','CAM-04 — Poarta Vest','2026-05-14 01:33')");
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Alerta rezolvata','CAM-06 — Depozit Sud','2026-05-14 03:25')");
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Misiune creata','Supraveghere depozit vest','2026-05-15 07:00')");
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Alerta detectata','CAM-01 — Intrare Nord','2026-05-15 04:18')");
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Operator logat','operator1','2026-05-16 08:00')");
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Alerta detectata','CAM-03 — Perimetru Est','2026-05-16 00:52')");
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Misiune creata','Patrulare zona rezidentiala','2026-05-17 06:30')");
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Alerta detectata','CAM-02 — Parcare Vest','2026-05-17 23:08')");
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Operator logat','admin','2026-05-18 08:00')");
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Alerta detectata','CAM-01 — Intrare Nord','2026-05-18 07:42')");
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Alerta detectata','CAM-03 — Perimetru Est','2026-05-18 09:15')");
        db.execSQL("INSERT INTO " + TABLE_LOG +
                " (actiune, detalii, timestamp) VALUES" +
                " ('Misiune creata','Patrulare zona nord','2026-05-18 08:05')");
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
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_MISIUNI +
                    " ADD COLUMN " + COL_ALERTA_AUTO + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_MISIUNI +
                    " ADD COLUMN " + COL_RECURENTA + " INTEGER DEFAULT 0");
        }
        if (oldVersion < 4) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_LOG + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_ACTIUNE + " TEXT, " +
                    COL_DETALII + " TEXT, " +
                    COL_TIMESTAMP + " TEXT)");
        }
        if (oldVersion < 5) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_UTILIZATORI + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_USERNAME + " TEXT UNIQUE, " +
                    COL_PAROLA + " TEXT, " +
                    COL_NUME_OP + " TEXT)");
        }
        if (oldVersion < 6) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CAMERE_CUSTOM + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NUME + " TEXT, " +
                    COL_LOCATIE + " TEXT, " +
                    COL_LAT + " REAL, " +
                    COL_LNG + " REAL, " +
                    COL_STREAM_URL + " TEXT)");
        }
        if (oldVersion < 7) {
            seedData(db);
        }
    }

    //inval
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

    //inval
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

    //add misiune
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
        cv.put(COL_ALERTA_AUTO, m.alertaAuto ? 1 : 0);
        cv.put(COL_RECURENTA, m.recurenta ? 1 : 0);
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
            m.alertaAuto = c.getInt(c.getColumnIndexOrThrow(COL_ALERTA_AUTO)) == 1;
            m.recurenta = c.getInt(c.getColumnIndexOrThrow(COL_RECURENTA)) == 1;
            lista.add(m);
        }
        c.close();
        return lista;
    }

    public void stergeMisiune(int id) {
        getWritableDatabase().delete(TABLE_MISIUNI, COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public List<Misiune> getMisiuniCuAlerta() {
        List<Misiune> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_MISIUNI, null,
                COL_ALERTA_AUTO + "=1", null, null, null, COL_ID + " DESC");
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
            m.alertaAuto = c.getInt(c.getColumnIndexOrThrow(COL_ALERTA_AUTO)) == 1;
            m.recurenta = c.getInt(c.getColumnIndexOrThrow(COL_RECURENTA)) == 1;
            lista.add(m);
        }
        c.close();
        return lista;
    }

    public void dismissAlertaMisiune(int id) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ALERTA_AUTO, 0);
        getWritableDatabase().update(TABLE_MISIUNI, cv,
                COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void adaugaLog(String actiune, String detalii) {
        String ts = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date());
        ContentValues cv = new ContentValues();
        cv.put(COL_ACTIUNE, actiune);
        cv.put(COL_DETALII, detalii);
        cv.put(COL_TIMESTAMP, ts);
        getWritableDatabase().insert(TABLE_LOG, null, cv);
    }

    public List<LogEntry> getLoguri() {
        List<LogEntry> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_LOG, null, null, null, null, null,
                COL_ID + " DESC");
        while (c.moveToNext()) {
            LogEntry e = new LogEntry();
            e.id = c.getInt(c.getColumnIndexOrThrow(COL_ID));
            e.actiune = c.getString(c.getColumnIndexOrThrow(COL_ACTIUNE));
            e.detalii = c.getString(c.getColumnIndexOrThrow(COL_DETALII));
            e.timestamp = c.getString(c.getColumnIndexOrThrow(COL_TIMESTAMP));
            lista.add(e);
        }
        c.close();
        return lista;
    }

    public void stergeToateLogurile() {
        getWritableDatabase().delete(TABLE_LOG, null, null);
    }

    public boolean registerUser(String username, String parola, String numeOperator) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(COL_USERNAME, username);
            cv.put(COL_PAROLA, parola);
            cv.put(COL_NUME_OP, numeOperator);
            long result = getWritableDatabase().insert(TABLE_UTILIZATORI, null, cv);
            return result != -1;
        } catch (Exception e) {
            return false;
        }
    }

    // camere
    public long adaugaCameraCustom(String nume, String locatie, double lat, double lng, String streamUrl) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NUME, nume);
        cv.put(COL_LOCATIE, locatie);
        cv.put(COL_LAT, lat);
        cv.put(COL_LNG, lng);
        cv.put(COL_STREAM_URL, streamUrl);
        return getWritableDatabase().insert(TABLE_CAMERE_CUSTOM, null, cv);
    }

    public List<CameraInfo> getCamereCustom() {
        List<CameraInfo> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_CAMERE_CUSTOM, null, null, null, null, null, COL_ID + " ASC");
        int index = 7;
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndexOrThrow(COL_ID));
            String nume = c.getString(c.getColumnIndexOrThrow(COL_NUME));
            String locatie = c.getString(c.getColumnIndexOrThrow(COL_LOCATIE));
            double lat = c.getDouble(c.getColumnIndexOrThrow(COL_LAT));
            double lng = c.getDouble(c.getColumnIndexOrThrow(COL_LNG));
            String url = c.getString(c.getColumnIndexOrThrow(COL_STREAM_URL));
            com.google.android.gms.maps.model.LatLng pos =
                    new com.google.android.gms.maps.model.LatLng(lat, lng);
            CameraInfo cam = new CameraInfo(
                    "CAM-" + String.format("%02d", index++),
                    nume, locatie, true, pos,
                    url != null ? url : "");
            cam.dbId = id;
            lista.add(cam);
        }
        c.close();
        return lista;
    }

    public void stergeCameraCustom(int id) {
        getWritableDatabase().delete(TABLE_CAMERE_CUSTOM, COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public String loginUser(String username, String parola) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_UTILIZATORI, new String[]{COL_NUME_OP},
                COL_USERNAME + "=? AND " + COL_PAROLA + "=?",
                new String[]{username, parola}, null, null, null);
        String numeOperator = null;
        if (c.moveToFirst()) {
            numeOperator = c.getString(0);
        }
        c.close();
        return numeOperator;
    }
}
