package com.example.laborator8;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Phone.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PhoneDao phoneDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "phones_database")
                            .allowMainThreadQueries() // Nota: In productie se folosesc thread-uri separate, dar pentru lab e mai simplu asa
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
