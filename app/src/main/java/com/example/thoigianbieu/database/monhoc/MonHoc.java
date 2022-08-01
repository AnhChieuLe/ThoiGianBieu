package com.example.thoigianbieu.database.monhoc;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "monhoc")
public class MonHoc implements Comparable, Serializable {
    @PrimaryKey
    @NonNull
    private String maMonHoc;
    @NonNull
    private String tenMonHoc;
    private String tenGiangVien;
    private String phongHoc;
    private ArrayList<String> buoiHoc;
    private Calendar ngayBatDau;
    private Calendar ngayKetThuc;

    public MonHoc(String maMonHoc, String tenMonHoc, String tenGiangVien, String phongHoc, ArrayList<String> buoiHoc, Calendar ngayBatDau, Calendar ngayKetThuc) {
        if(maMonHoc.equals("")){
            this.maMonHoc = convertToMaMonHoc(tenMonHoc);
        }
        else {
            this.maMonHoc = maMonHoc;
        }
        this.tenMonHoc = tenMonHoc;
        this.tenGiangVien = tenGiangVien;
        this.phongHoc = phongHoc;
        this.buoiHoc = buoiHoc;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public String getTenGiangVien() {
        return tenGiangVien;
    }

    public void setTenGiangVien(String tenGiangVien) {
        this.tenGiangVien = tenGiangVien;
    }

    public String getPhongHoc() {
        return phongHoc;
    }

    public void setPhongHoc(String phongHoc) {
        this.phongHoc = phongHoc;
    }

    public ArrayList<String> getBuoiHoc() {
        return buoiHoc;
    }

    public void setBuoiHoc(ArrayList<String> buoiHoc) {
        this.buoiHoc = buoiHoc;
    }

    public Calendar getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Calendar ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Calendar getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Calendar ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    @Override
    public int compareTo(Object obj) {
        MonHoc mh = (MonHoc) obj;
        return getDays(this.ngayKetThuc, Calendar.getInstance()) - getDays(mh.ngayKetThuc, Calendar.getInstance());
    }

    //Khoảng cách giữa 2 ngày
    public int getDays(Calendar c1, Calendar c2){
        long mili = c1.getTime().getTime() - c2.getTime().getTime();
        long days = TimeUnit.MICROSECONDS.toDays(mili);
        return (int) days;
    }

    //Chuyển chuỗi có dấu sang không dấu, loại bỏ dấu cách
    public static String convertToMaMonHoc(String str) {
        str = str.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        str = str.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        str = str.replaceAll("ì|í|ị|ỉ|ĩ", "i");
        str = str.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        str = str.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        str = str.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
        str = str.replaceAll("đ", "d");

        str = str.replaceAll("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
        str = str.replaceAll("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
        str = str.replaceAll("Ì|Í|Ị|Ỉ|Ĩ", "I");
        str = str.replaceAll("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
        str = str.replaceAll("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
        str = str.replaceAll("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
        str = str.replaceAll("Đ", "D");

        str = str.replaceAll(" ", "");
        return str.toLowerCase();
    }
}