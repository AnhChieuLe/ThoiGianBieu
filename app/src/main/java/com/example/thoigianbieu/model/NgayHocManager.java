package com.example.thoigianbieu.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.thoigianbieu.database.monhoc.MonHoc;
import com.example.thoigianbieu.database.monhoc.MonHocDAO;
import com.example.thoigianbieu.database.monhoc.MonHocDatabase;
import com.example.thoigianbieu.database.ngayhoc.NgayHoc;
import com.example.thoigianbieu.database.ngayhoc.NgayHocDatabase;
import com.example.thoigianbieu.setting.SharePreferencesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
        Log.d("size", listMH.size() + "");

        if(listMH.isEmpty()){
            return;
        }

        List<NgayHoc> listFromMH = new ArrayList<>(convert(listMH));
        Log.d("size", listFromMH.size() + "");

        if (listNH.isEmpty()){
            listNH = new ArrayList<>(listFromMH);
            return;
        }

        List<NgayHoc> listAdd = new ArrayList<>();

        for(NgayHoc ngayHoc:listNH){
            for(NgayHoc ngayHoc1:listFromMH){
                if (ngayHoc.equals(ngayHoc1)){
                    ngayHoc.addNgayHoc(ngayHoc1);
                }
                if (!exitInListNgayHoc(ngayHoc1)){
                    listAdd.add(ngayHoc1);
                }
            }
        }

        listNH.addAll(listAdd);
    }

    public void themThoiKhoaBieuTrong(){
        List<Calendar> listNgay = new ArrayList<>();
        for(NgayHoc ngayHoc:listNH){
            listNgay.add(ngayHoc.getNgayHoc());
        }
        if(!listNgay.isEmpty()){
            Calendar min = Collections.min(listNgay); Calendar index = (Calendar)min.clone();
            Calendar max = Collections.max(listNgay);
            while (index.compareTo(max) <= 0){
                if(!listNH.contains(new NgayHoc(index))){
                    listNH.add(new NgayHoc(index));
                }
                index.add(Calendar.DATE, 1);
            }
        }
    }

    public void xoaNgayHocDaQua(){
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        listNH.removeIf(ngayHoc -> ngayHoc.getNgayHoc().compareTo(now) < 0);
    }

    private boolean exitInListNgayHoc(NgayHoc ngayHoc){
        if (listNH.isEmpty()){
            return false;
        }

        for(NgayHoc nh:listNH){
            if(ngayHoc.equals(nh)){
                return true;
            }
        }
        return false;
    }

    public List<NgayHoc> getListNH() {

        filter();

        listNH = subList();

        for(NgayHoc ngayHoc:listNH){
            Log.d("date", ngayHoc.getStringNgayHoc(context));
        }

        Collections.sort(listNH);

        return listNH;
    }

    private List<NgayHoc> subList(){
        int max = SharePreferencesManager.getTKBCount();
        int todayPosition = getToDayPosition();

        if(listNH.size() < max){
            return listNH;
        }

        int startIndex = Math.max(0, todayPosition - (int)max/2);
        int endIndex = Math.min(startIndex + max, listNH.size()-1);

        Log.d("index", startIndex + " " + endIndex);

        listNH = listNH.subList(startIndex, endIndex);

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
            xoaNgayHocDaQua();
        }
    }

    public int getToDayPosition(){
        if(listNH.isEmpty()){
            return 0;
        }

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

    public List<NgayHoc> getWeek(){
        List<NgayHoc> list = new ArrayList<>();
        List<NgayHoc> list2 = new ArrayList<>();

        filter();

        for(NgayHoc ngayHoc:listNH){
            if(ngayHoc.getNgayHoc().get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)
            && ngayHoc.getNgayHoc().get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) {
                list.add(ngayHoc);
            }
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        do{
            if(!exitInListNgayHoc(new NgayHoc(cal))){
                list.add(new NgayHoc(cal));
            }
            cal.add(Calendar.DATE, 1);
        } while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY);

        Collections.sort(list);
        for(NgayHoc ngayHoc:list){
            if(ngayHoc.compareTo(new NgayHoc(Calendar.getInstance())) < 0){
                list2.add(ngayHoc);
            }
        }
        
        list.removeIf(ngayHoc -> ngayHoc.compareTo(new NgayHoc(Calendar.getInstance())) < 0);
        list.addAll(list2);

        return list;
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

            listResult.add(ngayHoc);
            min.add(Calendar.DATE, 1);

        }while (min.compareTo(max) <= 0);

        return listResult;
    }
}