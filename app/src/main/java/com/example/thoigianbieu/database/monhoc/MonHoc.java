package com.example.thoigianbieu.database.monhoc;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.example.thoigianbieu.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "monhoc")
public class MonHoc implements Comparable, Serializable {
    @PrimaryKey(autoGenerate = true)
    private int maMonHoc;
    @NonNull
    private String tenMonHoc;
    private String tenGiangVien;
    private String phongHoc;
    private ArrayList<String> buoiHoc;
    private Calendar ngayBatDau;
    private Calendar ngayKetThuc;

    public MonHoc(@NonNull String tenMonHoc, String tenGiangVien, String phongHoc, ArrayList<String> buoiHoc, Calendar ngayBatDau, Calendar ngayKetThuc) {
        this.tenMonHoc = tenMonHoc;
        this.tenGiangVien = tenGiangVien;
        this.phongHoc = phongHoc;
        this.buoiHoc = buoiHoc;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    public int getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(int maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    @NonNull
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

    public boolean isPast(){
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return this.ngayKetThuc.compareTo(now) < 0;
    }

    public String getStringBuoiHoc(Context context){
        StringBuilder strBuoiHoc = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        if(buoiHoc.size() != 0){
            for (int i = 0; i < buoiHoc.size(); i++) {
                for (int j = 1; j < 8; j++) {
                    if (buoiHoc.get(i).indexOf("" + j) == 0) {
                        calendar.set(Calendar.DAY_OF_WEEK, j);
                        if (buoiHoc.get(i).indexOf("S") == 1) {
                            strBuoiHoc.append(context.getResources().getString(R.string.sang)).append(" ");
                        }
                        if (buoiHoc.get(i).indexOf("C") == 1) {
                            strBuoiHoc.append(context.getResources().getString(R.string.chieu)).append(" ");
                        }
                        strBuoiHoc.append(simpleDateFormat.format(calendar.getTime())).append(", ");
                        break;
                    }
                }
            }
        }

        String str = strBuoiHoc.toString();
        str = str.toLowerCase();
        if(!str.equals("")){
            str = str.substring(0,1).toUpperCase() + str.substring(1);
            str = str.substring(0, str.lastIndexOf(","));
        }
        return str;
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
}