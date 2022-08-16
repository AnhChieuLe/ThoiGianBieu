package com.example.thoigianbieu.database.sukien;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Calendar;
import java.util.List;

@Dao
public interface SuKienDao {
    @Insert
    void insertSuKien(SuKien suKien);

    @Insert
    void insertSuKien(List<SuKien> list);

    @Query("SELECT * FROM sukien")
    List<SuKien> getListSuKien();

    @Delete
    void deleteSuKien(SuKien suKien);

    @Update
    void updateSuKien(SuKien suKien);

    @Query("DELETE FROM sukien")
    void deleteSuKien();

    @Query( "SELECT * " +
            "FROM sukien " +
            "WHERE tieuDe LIKE '%' || :str || '%' " +
            "OR noiDung LIKE '%' || :str || '%'")
    List<SuKien> getListSuKien(String str);

    @Query( "SELECT * " +
            "FROM sukien " +
            "WHERE status = :str")
    List<SuKien> getListSuKien(boolean str);

    @Query("SELECT * FROM sukien WHERE ngayBatDau <= :calendar2 AND ngayKetThuc >= :calendar1")
    List<SuKien> getListSuKien(Calendar calendar1, Calendar calendar2);
}
