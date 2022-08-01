package com.example.thoigianbieu.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.monhoc.MonHoc;
import com.example.thoigianbieu.database.monhoc.MonHocDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DialogThemMonHocFragment extends DialogFragment {

    EditText edtMaMonHoc, edtTenMonHoc, edtTenGiangVien, edtPhongHoc, edtSoTuan, edtSoBuoi;
    TextView tvNgayBatDau, tvNgayKetThuc;
    CheckBox[] Sang = new CheckBox[7];
    CheckBox[] Chieu = new CheckBox[7];
    Button btnThem, btnHuy;

    Calendar ngayBatDau = Calendar.getInstance();
    Calendar ngayKetThuc = Calendar.getInstance();
    LoadData loadData;
    MonHoc monHoc;

    public DialogThemMonHocFragment(){
        super();
    }

    @SuppressLint("ValidFragment")
    public DialogThemMonHocFragment(MonHoc monHoc, LoadData loadData) {
        super();
        this.loadData = loadData;
        this.monHoc = monHoc;
    }

    @SuppressLint("ValidFragment")
    public DialogThemMonHocFragment(LoadData loadData) {
        super();
        this.loadData = loadData;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = setDialog(savedInstanceState);
        setControl(dialog.getWindow().getDecorView());

        if(monHoc != null){
            setData();
        }

        buttonClick();
        setFocus();
        setNgay();

        return dialog;
    }

    private Dialog setDialog(Bundle bundle){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_themmonhoc);

        Window window = dialog.getWindow();
        if(window == null){
            return super.onCreateDialog(bundle);
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;

        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        return dialog;
    }

    public interface LoadData{
        void LoadData();
    }

    @SuppressLint("SetTextI18n")
    private void setData(){
        edtMaMonHoc.setText(monHoc.getMaMonHoc());
        edtTenMonHoc.setText(monHoc.getTenMonHoc());
        edtTenGiangVien.setText(monHoc.getTenGiangVien());
        edtPhongHoc.setText(monHoc.getPhongHoc());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ngayBatDau = monHoc.getNgayBatDau();
        tvNgayBatDau.setText(getString(R.string.ngay) + simpleDateFormat.format(ngayBatDau.getTime()));
        ngayKetThuc = monHoc.getNgayKetThuc();
        tvNgayKetThuc.setText(getString(R.string.ngay) + simpleDateFormat.format(ngayKetThuc.getTime()));

        ArrayList<String> listBuoiHoc = monHoc.getBuoiHoc();

        for(int i = 0; i < listBuoiHoc.size(); i++){
            for(int j = 1; j < 8; j++){
                if(listBuoiHoc.get(i).equals(j + "S")){
                    Sang[j-1].setChecked(true);
                }
                if(listBuoiHoc.get(i).equals(j + "C")){
                    Chieu[j-1].setChecked(true);
                }
            }
        }
        setSoTuan(listBuoiHoc);
        btnThem.setText(R.string.capnhat);
    }

    private MonHoc getData(){
        String maMonHoc, tenMonHoc, tenGiangVien, phongHoc;

        maMonHoc = edtMaMonHoc.getText().toString();
        tenMonHoc = edtTenMonHoc.getText().toString();
        tenGiangVien = edtTenGiangVien.getText().toString();
        phongHoc = edtPhongHoc.getText().toString();

        MonHoc newMH = new MonHoc(maMonHoc, tenMonHoc, tenGiangVien, phongHoc, getListBH(), ngayBatDau, ngayKetThuc);

        return newMH;
    }

    private ArrayList<String> getListBH(){
        ArrayList<String> listBuoiHoc = new ArrayList<String>();
        boolean[] sang = new boolean[7];
        boolean[] chieu = new boolean[7];

        for(int i = 0; i < 7; i++){
            sang[i] = Sang[i].isChecked();
            chieu[i] = Chieu[i].isChecked();
        }

        for(int i = 0; i < 7; i++){
            if(sang[i]){
                listBuoiHoc.add((i+1) + "S");
            }
            if(chieu[i]){
                listBuoiHoc.add((i+1) + "C");
            }
        }

        return listBuoiHoc;
    }

    private Calendar chonNgay(TextView tvDate, Calendar calendar){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                tvDate.setText(getString(R.string.ngay) + d + "/" +(m+1) + "/" + y);
                calendar.set(y, m, d);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        datePickerDialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
        datePickerDialog.show();
        return calendar;
    }

    private void setControl(View view) {
        edtMaMonHoc =       view.findViewById(R.id.edt_monhoc_dialog_mamonhoc);
        edtTenMonHoc =      view.findViewById(R.id.edt_monhoc_dialog_tenmonhoc);
        edtTenGiangVien =   view.findViewById(R.id.edt_monhoc_dialog_tengiangvien);
        edtPhongHoc =       view.findViewById(R.id.edt_monhoc_dialog_phonghoc);
        edtSoTuan =         view.findViewById(R.id.edt_monhoc_dialog_sotuan);
        edtSoBuoi =         view.findViewById(R.id.edt_monhoc_dialog_sobuoi);

        tvNgayBatDau =      view.findViewById(R.id.tv_monhoc_dialog_ngaybatdau);
        tvNgayKetThuc =     view.findViewById(R.id.tv_monhoc_dialog_ngayketthuc);

        Sang[0] =   view.findViewById(R.id.T1S);
        Sang[1] =   view.findViewById(R.id.T2S);
        Sang[2] =   view.findViewById(R.id.T3S);
        Sang[3] =   view.findViewById(R.id.T4S);
        Sang[4] =   view.findViewById(R.id.T5S);
        Sang[5] =   view.findViewById(R.id.T6S);
        Sang[6] =   view.findViewById(R.id.T7S);

        Chieu[0] =  view.findViewById(R.id.T1C);
        Chieu[1] =  view.findViewById(R.id.T2C);
        Chieu[2] =  view.findViewById(R.id.T3C);
        Chieu[3] =  view.findViewById(R.id.T4C);
        Chieu[4] =  view.findViewById(R.id.T5C);
        Chieu[5] =  view.findViewById(R.id.T6C);
        Chieu[6] =  view.findViewById(R.id.T7C);

        btnThem  =  view.findViewById(R.id.btn_monhoc_them);
        btnHuy   =  view.findViewById(R.id.btn_monhoc_huy);
    }

    private void setFocus(){
        edtMaMonHoc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(edtMaMonHoc.getText().toString().equals("")){
                    edtMaMonHoc.setBackgroundResource(R.drawable.custom_edittext_warning);
                }
                else {
                    edtMaMonHoc.setBackgroundResource(R.drawable.custom_edittext);
                }
            }
        });

        edtTenMonHoc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(edtTenMonHoc.getText().toString().equals("")){
                    edtTenMonHoc.setBackgroundResource(R.drawable.custom_edittext_warning);
                }
                else {
                    edtTenMonHoc.setBackgroundResource(R.drawable.custom_edittext);
                }
            }
        });

        edtTenGiangVien.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(edtTenGiangVien.getText().toString().equals("")){
                    edtTenGiangVien.setBackgroundResource(R.drawable.custom_edittext_warning);
                }
                else {
                    edtTenGiangVien.setBackgroundResource(R.drawable.custom_edittext);
                }
            }
        });

        edtPhongHoc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(edtPhongHoc.getText().toString().equals("")){
                    edtPhongHoc.setBackgroundResource(R.drawable.custom_edittext_warning);
                }
                else {
                    edtPhongHoc.setBackgroundResource(R.drawable.custom_edittext);
                }
            }
        });

        for(int i = 0; i < 7; i++){
            Sang[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setSoTuan(getListBH());
                }
            });
            Chieu[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setSoTuan(getListBH());
                }
            });
        }
    }

    private void setNgay(){
        tvNgayBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ngayBatDau = chonNgay(tvNgayBatDau, ngayBatDau);
                setSoTuan(getListBH());
            }
        });

        tvNgayKetThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ngayKetThuc = chonNgay(tvNgayKetThuc, ngayKetThuc);
                if(ngayKetThuc.compareTo(Calendar.getInstance()) > 0){
                    setSoTuan(getListBH());
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.thongbao))
                            .setMessage(R.string.confirm_end_monhoc)
                            .setPositiveButton(R.string.van_them, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setSoTuan(getListBH());
                                    builder.create().dismiss();
                                }
                            })
                            .setNegativeButton(R.string.huy, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    builder.create().dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.text_color));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.text_title_yellow));
                }
            }
        });
    }

    private void buttonClick(){
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monHoc = getData();

                StringBuilder mhBT = new StringBuilder("");
                for(MonHoc mh:checkLichHoc(monHoc)){
                    mhBT.append(mh.getTenMonHoc()).append("\n");
                }

                if(checkLichHoc(monHoc).size()>0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.trung_lich)
                            .setMessage(mhBT.substring(0, mhBT.lastIndexOf("\n")).toString())
                            .setPositiveButton(R.string.van_them, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MonHocDatabase.getInstance(getActivity()).monHocDAO().insertMonHoc(monHoc);
                                    getDialog().dismiss();
                                    loadData.LoadData();
                                }
                            })
                            .setNegativeButton(R.string.huy, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    builder.create().dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.text_color));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.text_title_yellow));
                }
                else{
                    MonHocDatabase.getInstance(getActivity()).monHocDAO().insertMonHoc(monHoc);
                    getDialog().dismiss();
                    loadData.LoadData();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }

    private void setSoTuan(ArrayList<String> listBuoiHoc){
        try{
            int soTuan = 0;
            int soBuoi = 0;
            Calendar index = (Calendar) ngayBatDau.clone();

            do{
                for(String str:listBuoiHoc){
                    if(String.valueOf(index.get(Calendar.DAY_OF_WEEK)).equals(str.substring(0,1))){
                        soBuoi ++;
                    }
                }
                index.add(Calendar.DATE, 1);
            }while(index.compareTo(ngayKetThuc) <= 0);

            soTuan = (int)Math.ceil((double) soBuoi/listBuoiHoc.size());

            edtSoBuoi.setText(soBuoi + getString(R.string.buoi));
            edtSoTuan.setText(soTuan + getString(R.string.tuan));
        }catch (Exception e){
            //do notthing
        }
    }

    private List<MonHoc> checkLichHoc(MonHoc monHoc){
        ArrayList<String> listBuoiHoc = getListBH();

        List<MonHoc> listMonHoc = MonHocDatabase.getInstance(getActivity()).monHocDAO().getListMonHoc();

        List<MonHoc> lmh = new ArrayList<>();

        for(MonHoc mh:listMonHoc){
            for(String bh:mh.getBuoiHoc()){
                for(String str:listBuoiHoc){
                    if(str.equals(bh) && !mh.getMaMonHoc().equals(monHoc.getMaMonHoc())){
                        lmh.add(mh);
                    }
                }
            }
        }
        return lmh;
    }
}