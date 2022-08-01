package com.example.thoigianbieu.database.ngayhoc;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.thoigianbieu.database.TypeConvert;

@Database(entities = {NgayHoc.class}, version = 1)
@TypeConverters(TypeConvert.class)
public abstract class NgayHocDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "ngayhoc.db";
    public static NgayHocDatabase instance;

    public static synchronized NgayHocDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), NgayHocDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract NgayHocDao ngayHocDao();
}
