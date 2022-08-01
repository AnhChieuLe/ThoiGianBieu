package com.example.thoigianbieu.database.monhoc;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.thoigianbieu.database.TypeConvert;

@Database(entities = {MonHoc.class}, version = 1)
@TypeConverters(TypeConvert.class)
public abstract class MonHocDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "monhoc.db";
    public static MonHocDatabase instance;

    public static synchronized MonHocDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), MonHocDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract MonHocDAO monHocDAO();
}
