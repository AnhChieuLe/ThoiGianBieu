package com.example.thoigianbieu.database.monhoc;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Dao
public interface MonHocDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMonHoc(MonHoc monHoc);

    @Query("SELECT * FROM monhoc")
    List<MonHoc> getListMonHoc();

    @Delete
    void deleteMonHoc(MonHoc monHoc);

    @Update
    void updateMonHoc(MonHoc monHoc);

    @Query( "SELECT * " +
            "FROM monhoc " +
            "WHERE tenMonHoc LIKE '%' || :str || '%'" +
            "OR tenGiangVien LIKE '%' || :str || '%'")
    List<MonHoc> getListMonHoc(String str);

    @Query("DELETE FROM monhoc")
    void deleteAll();

    @Query("SELECT MIN(ngayBatDau) FROM monhoc")
    Calendar getMinDate();

    @Query("SELECT MAX(ngayKetThuc) FROM monhoc")
    Calendar getMaxDate();
}