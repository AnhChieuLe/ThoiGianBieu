package com.example.thoigianbieu.database.sukien;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.thoigianbieu.database.TypeConvert;

@Database(entities = {SuKien.class}, version = 1)
@TypeConverters(TypeConvert.class)
public abstract class SuKienDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "sukien.db";
    private static SuKienDatabase instance;

    public static synchronized SuKienDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), SuKienDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract SuKienDao suKienDao();
}