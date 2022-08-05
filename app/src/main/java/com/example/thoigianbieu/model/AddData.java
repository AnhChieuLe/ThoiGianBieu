package com.example.thoigianbieu.model;

import android.content.Context;

import androidx.room.Database;

import com.example.thoigianbieu.database.monhoc.MonHoc;
import com.example.thoigianbieu.database.monhoc.MonHocDatabase;
import com.example.thoigianbieu.database.sukien.SuKien;
import com.example.thoigianbieu.database.sukien.SuKienDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddData {
    public static void AddMonHoc(Context context){
        MonHocDatabase.getInstance(context).monHocDAO().deleteAll();
        ArrayList<String> buoiHoc = new ArrayList<>();

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar1.add(Calendar.DATE, -7);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar2.add(Calendar.DATE, 21);

        buoiHoc.add("2S");
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(
                new MonHoc("Thực hành chuyên sâu", "Thầy Sáu", "1B12", buoiHoc, calendar1,calendar2));


        buoiHoc.clear();
        buoiHoc.add("2C");
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(
                new MonHoc("Khai phá dữ liệu đa phương tiện", "Thầy Sáu", "1B12", buoiHoc, calendar1,calendar2));

        buoiHoc.clear();
        buoiHoc.add("3S");
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(
                new MonHoc("Truyền thông lí thuyết và ứng dụng", "Cô Điệp", "1A001", buoiHoc, calendar1,calendar2));

        buoiHoc.clear();
        buoiHoc.add("4C");
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(
                new MonHoc("Lập trình kĩ xảo hình ảnh", "Thầy Hóa", "1B12", buoiHoc, calendar1,calendar2));

        buoiHoc.clear();
        buoiHoc.add("5S");
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(
                new MonHoc("Thực hành chuyên sâu", "Thầy Hóa", "1B12", buoiHoc, calendar1,calendar2));

        buoiHoc.clear();
        buoiHoc.add("6S");
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(
                new MonHoc("Phát triển dịch vụ giá trị gia tăng trên mạng viễn thông", "Thầy Hào", "1B12", buoiHoc, calendar1,calendar2));
    }

    public static void AddMonHocCoBan(Context context){
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 3);
        MonHoc toan     = new MonHoc("Toán", "", "", new ArrayList<String>(), start, end);
        MonHoc ly       = new MonHoc("Vật lý", "", "", new ArrayList<String>(), start, end);
        MonHoc hoa      = new MonHoc("Hoá học", "", "", new ArrayList<String>(), start, end);
        MonHoc sinh     = new MonHoc("Sinh học", "", "", new ArrayList<String>(), start, end);
        MonHoc tienganh = new MonHoc("Tiếng Anh", "", "", new ArrayList<String>(), start, end);
        MonHoc van      = new MonHoc("Ngữ văn", "", "", new ArrayList<String>(), start, end);
        MonHoc su       = new MonHoc("Lịch sử", "", "", new ArrayList<String>(), start, end);
        MonHoc dia      = new MonHoc("Địa lý", "", "", new ArrayList<String>(), start, end);
        MonHoc gdcd     = new MonHoc("Giáo dục công dân", "", "", new ArrayList<String>(), start, end);
        MonHoc tinhoc   = new MonHoc("Tin học", "", "", new ArrayList<String>(), start, end);
        MonHoc congnghe = new MonHoc("Công nghệ", "", "", new ArrayList<String>(), start, end);
        MonHoc amnhac   = new MonHoc("Âm nhạc", "", "", new ArrayList<String>(), start, end);
        MonHoc mythuat  = new MonHoc("Mỹ thuật", "", "", new ArrayList<String>(), start, end);
        MonHoc theduc   = new MonHoc("Thể dục", "", "", new ArrayList<String>(), start, end);

        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(toan    );
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(ly      );
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(hoa     );
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(sinh    );
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(tienganh);
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(van     );
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(su      );
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(dia     );
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(gdcd    );
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(tinhoc  );
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(congnghe);
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(amnhac  );
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(mythuat );
        MonHocDatabase.getInstance(context).monHocDAO().insertMonHoc(theduc  );
    }
}