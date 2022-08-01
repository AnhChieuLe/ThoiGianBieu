package com.example.thoigianbieu.database.ngayhoc;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.thoigianbieu.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Entity(tableName = "ngayhoc")
public class NgayHoc implements Comparable, Serializable {
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

        if(soSanhNgay(this.ngayHoc)){
            String str = context.getResources().getString(R.string.homnay);
            ngay.append(str).append(", ");
        }

        SimpleDateFormat format = new SimpleDateFormat("EEEE - dd/MM/yyyy");
        ngay.append(format.format(this.ngayHoc.getTime()));

        String str = ngay.toString().toLowerCase(Locale.ROOT);
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }

    private static boolean soSanhNgay(Calendar calendar){
        return calendar.get(Calendar.DAY_OF_MONTH)==Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                && calendar.get(Calendar.MONTH)==Calendar.getInstance().get(Calendar.MONTH)
                && calendar.get(Calendar.YEAR)==Calendar.getInstance().get(Calendar.YEAR);
    }

    public boolean isToDay(){
        return soSanhNgay(this.ngayHoc);
    }

    public boolean isPast(){
        return this.ngayHoc.compareTo(Calendar.getInstance()) < 0;
    }

    public boolean isSunDay(){
        return this.ngayHoc.get(Calendar.DAY_OF_WEEK) == 1;
    }

    public String getStringNgayHocInWeek(Context context){
        StringBuilder ngay = new StringBuilder("");

        if(soSanhNgay(this.ngayHoc)){
            String str = context.getResources().getString(R.string.homnay);
            ngay.append(str).append(", ");
        }

        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        ngay.append(format.format(this.ngayHoc.getTime()));

        String str = ngay.toString().toLowerCase(Locale.ROOT);
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NgayHoc)) return false;
        NgayHoc ngayHoc = (NgayHoc) obj;
        return getCalendarEqual(this.ngayHoc, ngayHoc.ngayHoc);
    }

    public boolean getCalendarEqual(Calendar c1, Calendar c2){
        boolean result =   c1.get(Calendar.YEAR)   == c2.get(Calendar.YEAR)
                        && c1.get(Calendar.MONTH)  == c2.get(Calendar.MONTH)
                        && c1.get(Calendar.DATE)   == c2.get(Calendar.DATE);
        return result;
    }

    @Override
    public int compareTo(Object obj) {
        NgayHoc ngayHoc = (NgayHoc) obj;
        return (int) (this.ngayHoc.getTimeInMillis() - ngayHoc.getNgayHoc().getTimeInMillis());
    }

    public void addNgayHoc(NgayHoc ngayHoc){
        monHocSang.addAll(ngayHoc.monHocSang);
        monHocChieu.addAll(ngayHoc.monHocChieu);
    }
}
