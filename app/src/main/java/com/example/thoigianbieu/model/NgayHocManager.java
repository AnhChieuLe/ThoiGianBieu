package com.example.thoigianbieu.model;

import android.content.Context;
import android.util.Log;

import com.example.thoigianbieu.database.monhoc.MonHoc;
import com.example.thoigianbieu.database.monhoc.MonHocDatabase;
import com.example.thoigianbieu.database.ngayhoc.NgayHoc;
import com.example.thoigianbieu.database.ngayhoc.NgayHocDatabase;
import com.example.thoigianbieu.setting.SharePreferencesManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class NgayHocManager {
    List<NgayHoc> listNH;
    Context context;

    public NgayHocManager(Context context) {
        this.context = context;
        this.listNH = new ArrayList<>();
        this.listNH = NgayHocDatabase.getInstance(context).ngayHocDao().getListNgayHoc();
    }

    public void addMonHoc(){
        List<MonHoc> listMH = MonHocDatabase.getInstance(context).monHocDAO().getListMonHoc();
        List<NgayHoc> listFromMH = new ArrayList<>(convert(listMH));

        if(listMH.isEmpty()){
            return;
        }

        if (listNH.isEmpty()){
            listNH = listFromMH;
            return;
        }

        int n = listNH.size();
        for(int i = 0; i < n; i++){
            for(NgayHoc ngay:listFromMH){
                if(listNH.get(i).equals(ngay)){
                    listNH.get(i).addNgayHoc(ngay);
                }
                if(!isExist(ngay)){
                    listNH.add(ngay);
                }
            }
        }
    }

    private boolean isExist(NgayHoc ngayHoc){
        for(NgayHoc ngayHoc1:listNH){
            if(ngayHoc.equals(ngayHoc1)){
                return true;
            }
        }
        return false;
    }

    public void themThoiKhoaBieuTrong(){
        if(listNH.isEmpty() || listNH.size() == 1)    return;

        List<Calendar> listNgay = new ArrayList<>();
        for(NgayHoc ngayHoc:listNH)     listNgay.add(ngayHoc.getNgayHoc());

        Calendar min = (Calendar) Collections.min(listNgay).clone(); Calendar index = (Calendar) min.clone();
        Calendar max = (Calendar) Collections.max(listNgay).clone();

        List<NgayHoc> listAdd = new ArrayList<>();
        while (min.compareTo(max) < 0){
            if(!isExist(new NgayHoc(min))){
                listNH.add(new NgayHoc(min));
            }

            min.add(Calendar.DATE, 1);
        }
    }

    public List<NgayHoc> getListNH() {

        filter();

        subList();

        return listNH;
    }

    public void filter(){
        if(SharePreferencesManager.getTKBInclude()){
            addMonHoc();
        }

        if(SharePreferencesManager.getTKBIncludeNull()){
            themThoiKhoaBieuTrong();
        }

        if(!SharePreferencesManager.getTKBIncludePast()){
            listNH.removeIf(ngayHoc -> ngayHoc.compareTo(new NgayHoc(Calendar.getInstance())) < 0);
        }
    }

    public int getDefaultPosition(){
        if(listNH.isEmpty())    return 0;

        int position = 0;
        long min = listNH.get(0).getNgayHoc().compareTo(Calendar.getInstance());
        min = Math.abs(min);
        for (int i = 0; i < listNH.size(); i++){
            if(listNH.get(i).getNgayHoc().compareTo(Calendar.getInstance()) < min){
                min = listNH.get(i).getNgayHoc().compareTo(Calendar.getInstance());
                min = Math.abs(min);
                position = i;
            }
        }

        return position;
    }

    private List<NgayHoc> convert(List<MonHoc> listMonHoc){
        List<NgayHoc> listResult = new ArrayList<NgayHoc>();
        if(listMonHoc.isEmpty()){
            return listResult;
        }

        List<Calendar> listStart = new ArrayList<>();
        List<Calendar> listEnd = new ArrayList<>();
        for(MonHoc monHoc:listMonHoc){
            listStart.add(monHoc.getNgayBatDau());
            listEnd.add(monHoc.getNgayKetThuc());
        }

        Calendar min = Collections.min(listStart);
        Calendar max = Collections.max(listEnd);

        int dayOfWeek;
        do{
            NgayHoc ngayHoc = new NgayHoc(min);
            dayOfWeek = min.get(Calendar.DAY_OF_WEEK);
            for(MonHoc monHoc:listMonHoc){
                ArrayList<String> buoiHoc = monHoc.getBuoiHoc();
                for(String buoihoc:buoiHoc){
                    if(buoihoc.indexOf(dayOfWeek + "") == 0){
                        if(buoihoc.indexOf("S") == 1){
                            ngayHoc.addMonHocSang(monHoc.getTenMonHoc());
                        }
                        if(buoihoc.indexOf("C") == 1) {
                            ngayHoc.addMonHocChieu(monHoc.getTenMonHoc());
                        }
                    }
                }
            }

            if(ngayHoc.getMonHocSang().size() + ngayHoc.getMonHocChieu().size() != 0){
                listResult.add(ngayHoc);
            }

            min.add(Calendar.DATE, 1);

        }while (min.compareTo(max) <= 0);

        return listResult;
    }

    private void shortList(){
        Set<NgayHoc> listSet = new TreeSet<>(listNH);
        listNH = new ArrayList<>(listSet);
    }

    private void subList(){
        shortList();

        int max = SharePreferencesManager.getTKBCount();
        int defaultPosition = getDefaultPosition();

        if(listNH.size() < max)     return ;

        int startIndex = Math.max(0, defaultPosition - (int)max/2);
        int endIndex = Math.min(startIndex + max, listNH.size());

        listNH = listNH.subList(startIndex, endIndex);
    }

    public List<NgayHoc> getWeek(){
        List<NgayHoc> list = new ArrayList<>();
        List<NgayHoc> list2 = new ArrayList<>();

        if(SharePreferencesManager.getTKBInclude()){
            addMonHoc();
        }

        for(NgayHoc ngayHoc:listNH){
            if(ngayHoc.getNgayHoc().get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)
                    && ngayHoc.getNgayHoc().get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) {
                list.add(ngayHoc);
            }
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        do{
            if(!isExist(new NgayHoc(cal))){
                list.add(new NgayHoc(cal));
            }
            cal.add(Calendar.DATE, 1);
        } while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY);

        Set<NgayHoc> listSorted = new TreeSet<>(list);
        list = new ArrayList<>(listSorted);

        for(NgayHoc ngayHoc:list){
            if(ngayHoc.compareTo(new NgayHoc(Calendar.getInstance())) < 0){
                list2.add(ngayHoc);
            }
        }

        list.removeIf(ngayHoc -> ngayHoc.compareTo(new NgayHoc(Calendar.getInstance())) < 0);
        list.addAll(list2);

        return list;
    }
}