package com.example.thoigianbieu.database.sukien;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "sukien")
public class SuKien implements Comparable, Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String tieuDe;
    @NonNull
    private String noiDung;
    @NonNull
    private Calendar ngayBatDau;
    private Calendar ngayKetThuc;
    private long nhacNho;
    private boolean status;

    public SuKien(@NonNull String tieuDe, @NonNull String noiDung, @NonNull Calendar ngayBatDau, Calendar ngayKetThuc, long nhacNho) {
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.nhacNho = nhacNho;
        this.status = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(@NonNull String tieuDe) {
        this.tieuDe = tieuDe;
    }

    @NonNull
    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(@NonNull String noiDung) {
        this.noiDung = noiDung;
    }

    @NonNull
    public Calendar getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(@NonNull Calendar ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Calendar getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Calendar ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public long getNhacNho() {
        return nhacNho;
    }

    public void setNhacNho(long nhacNho) {
        this.nhacNho = nhacNho;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public int compareTo(Object obj) {
        SuKien sk = (SuKien) obj;
        return getMinutes(this.ngayBatDau, Calendar.getInstance()) - getMinutes(sk.ngayBatDau, Calendar.getInstance());
    }

    //Khoảng cách giữa 2 mốc thời gian
    public int getMinutes(Calendar c1, Calendar c2){
        long mili = c1.getTime().getTime() - c2.getTime().getTime();
        long minutes = TimeUnit.MICROSECONDS.toMinutes(mili);
        return (int) minutes;
    }

    public String getStringThoiGian(){
        StringBuilder stringBuilder = new StringBuilder("");

        int days = getDayDistance(Calendar.getInstance(), (Calendar) ngayBatDau.clone());

        if(days == 0){
            stringBuilder.append("hôm nay, ");
        }else if(days == 1){
            stringBuilder.append("hôm qua, ");
        }else if(days == -1){
            stringBuilder.append("ngày mai, ");
        }else if(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) == ngayBatDau.get(Calendar.WEEK_OF_YEAR)
                && Calendar.getInstance().get(Calendar.YEAR) == ngayBatDau.get(Calendar.YEAR)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            stringBuilder.append(simpleDateFormat.format(ngayBatDau.getTime())).append(", ");
        }else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
            stringBuilder.append(simpleDateFormat.format(ngayBatDau.getTime())).append(" - ");
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        stringBuilder.append(format.format(ngayBatDau.getTime()));

        return stringBuilder.substring(0,1).toUpperCase() + stringBuilder.substring(1);
    }

    private int getDayDistance(Calendar c1, Calendar c2){
        c1.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DATE), 0, 0, 0);
        c1.set(Calendar.MILLISECOND, 0);

        c2.set(c1.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DATE), 0, 0, 0);
        c2.set(Calendar.MILLISECOND, 0);

        return (int)TimeUnit.MILLISECONDS.toDays(c1.getTimeInMillis() - c2.getTimeInMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuKien)) return false;
        SuKien suKien = (SuKien) o;
        return     getTieuDe().equals(suKien.getTieuDe())
                && getNoiDung().equals(suKien.getNoiDung())
                && getNgayBatDau().equals(suKien.getNgayBatDau())
                && getNgayKetThuc().equals(suKien.getNgayKetThuc());
    }
}