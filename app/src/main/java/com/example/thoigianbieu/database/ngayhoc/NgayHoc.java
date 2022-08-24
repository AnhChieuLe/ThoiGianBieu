package com.example.thoigianbieu.database.ngayhoc;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.thoigianbieu.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

@Entity(tableName = "ngayhoc")
public class NgayHoc implements Comparator<NgayHoc>, Comparable<NgayHoc>,Serializable {
    @PrimaryKey
    private Calendar ngayHoc;
    private ArrayList<String> monHocSang;
    private ArrayList<String> monHocChieu;
    private boolean notification;

    public NgayHoc(Calendar ngayHoc) {
        ngayHoc.set(Calendar.HOUR_OF_DAY, 0);
        ngayHoc.set(Calendar.MINUTE, 0);
        ngayHoc.set(Calendar.SECOND, 0);
        ngayHoc.set(Calendar.MILLISECOND, 0);
        this.ngayHoc = (Calendar) ngayHoc.clone();
        monHocSang = new ArrayList<>();
        monHocChieu = new ArrayList<>();
        notification = false;
    }

    public Calendar getNgayHoc() {
        return ngayHoc;
    }

    public void setNgayHoc(Calendar ngayHoc) {
        ngayHoc.set(Calendar.HOUR_OF_DAY, 0);
        ngayHoc.set(Calendar.MINUTE, 0);
        ngayHoc.set(Calendar.SECOND, 0);
        ngayHoc.set(Calendar.MILLISECOND, 0);
        this.ngayHoc = ngayHoc;
    }

    public ArrayList<String> getMonHocSang() {
        return monHocSang;
    }

    public void addMonHocSang(String tenMonHoc){
        this.monHocSang.add(tenMonHoc);
    }

    public void setMonHocSang(ArrayList<String> monHocSang) {
        this.monHocSang = monHocSang;
    }

    public String getStringSang(){
        StringBuilder stringBuilder = new StringBuilder("");
        for(int i = 0; i < this.monHocSang.size(); i++){
            stringBuilder.append(this.monHocSang.get(i));
            if(i != this.monHocSang.size() - 1){
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public ArrayList<String> getMonHocChieu() {
        return monHocChieu;
    }

    public void addMonHocChieu(String tenMonHoc){
        this.monHocChieu.add(tenMonHoc);
    }

    public String getStringChieu(){
        StringBuilder stringBuilder = new StringBuilder("");
        for(int i = 0; i < this.monHocChieu.size(); i++){
            stringBuilder.append(this.monHocChieu.get(i));
            if(i != this.monHocChieu.size() - 1){
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public void setMonHocChieu(ArrayList<String> monHocChieu) {
        this.monHocChieu = monHocChieu;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public String getStringNgayHoc(Context context){
        StringBuilder ngay = new StringBuilder("");

        if(isToDay()){
            String str = context.getResources().getString(R.string.homnay);
            ngay.append(str);
        }
        SimpleDateFormat format;
        if(ngayHoc.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR))
            format = new SimpleDateFormat("EEEE - dd/MM/yyyy", Locale.getDefault());
        else
            format = new SimpleDateFormat("EEEE - dd/MM", Locale.getDefault());

        ngay.append(format.format(this.ngayHoc.getTime()));

        String str = ngay.toString().toLowerCase(Locale.ROOT);
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }

    public boolean isToDay(){
        return  this.ngayHoc.get(Calendar.DATE)     ==  Calendar.getInstance().get(Calendar.DATE)
            &&  this.ngayHoc.get(Calendar.MONTH)    ==  Calendar.getInstance().get(Calendar.MONTH)
            &&  this.ngayHoc.get(Calendar.YEAR)     ==  Calendar.getInstance().get(Calendar.YEAR);
    }

    public boolean isPast(){
        return this.ngayHoc.compareTo(Calendar.getInstance()) < 0;
    }

    public boolean isSunDay(){
        return this.ngayHoc.get(Calendar.DAY_OF_WEEK) == 1;
    }

    public String getStringNgayHocInWeek(Context context){
        StringBuilder ngay = new StringBuilder("");

        if(isToDay()){
            String str = context.getResources().getString(R.string.homnay);
            ngay.append(str);
        }

        SimpleDateFormat format = new SimpleDateFormat("EEEE", Locale.getDefault());
        ngay.append(format.format(this.ngayHoc.getTime()));

        String str = ngay.toString().toLowerCase(Locale.ROOT);
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }

    public void addNgayHoc(NgayHoc ngayHoc){
        monHocSang.addAll(ngayHoc.monHocSang);
        monHocChieu.addAll(ngayHoc.monHocChieu);
    }

    @Override
    public int compare(NgayHoc o1, NgayHoc o2) {
        return (int) (o1.ngayHoc.getTimeInMillis() - o2.ngayHoc.getTimeInMillis());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NgayHoc)) return false;
        NgayHoc ngayHoc = (NgayHoc) obj;
        return this.ngayHoc.equals(ngayHoc.ngayHoc);
    }

    @Override
    public int compareTo(NgayHoc ngay) {
        return (int) (this.ngayHoc.getTimeInMillis() - ngay.ngayHoc.getTimeInMillis());
    }
}