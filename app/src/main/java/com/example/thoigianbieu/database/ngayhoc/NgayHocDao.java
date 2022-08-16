package com.example.thoigianbieu.database.ngayhoc;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Calendar;
import java.util.List;

@Dao
public interface NgayHocDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNgayHoc(NgayHoc ngayHoc);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNgayHoc(List<NgayHoc> list);

    @Query("SELECT * FROM ngayhoc")
    List<NgayHoc> getListNgayHoc();

    @Query("SELECT * FROM ngayhoc WHERE ngayHoc = :ngay")
    NgayHoc getNgayHoc(Calendar ngay);

    @Delete
    void deleteNgayHoc(NgayHoc ngayHoc);

    @Update
    void updateNgayHoc(NgayHoc ngayHoc);

    @Query("SELECT EXISTS(SELECT * FROM ngayhoc WHERE ngayHoc = :ngay)")
    Boolean isExist(Calendar ngay);
}
