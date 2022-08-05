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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.ngayhoc.NgayHoc;
import com.example.thoigianbieu.database.ngayhoc.NgayHocDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class DialogThemNgayHocFragment extends DialogFragment {

    TextView tvNgay;
    EditText edtSang, edtChieu;
    Button btnThem, btnHuy;

    Calendar ngay;
    LoadData loadData;

    public interface LoadData{
        void loadData();
    }

    @SuppressLint("ValidFragment")
    public DialogThemNgayHocFragment(LoadData loadData) {
        super();
        this.loadData = loadData;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog  = setDialog(savedInstanceState);

        setControl(dialog.getWindow().getDecorView());

        setEvent();

        return dialog;
    }

    private void setEvent() {
        tvNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonNgay(tvNgay, ngay);
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NgayHoc ngayHoc = getNgayHoc();
                if(ngayHoc!=null){
                    NgayHocDatabase.getInstance(getActivity()).ngayHocDao().insertNgayHoc(ngayHoc);
                    loadData.loadData();
                    getDialog().dismiss();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    private NgayHoc getNgayHoc(){
        String sang = edtSang.getText().toString().trim();
        String chieu = edtChieu.getText().toString().trim();

        if((sang + chieu).equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getString(R.string.thieuthongtin))
                    .setMessage(R.string.khongdetrongbuoihoc)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
            dialog.show();
            return null;
        }

        NgayHoc ngayHoc = new NgayHoc(ngay);

        if(!sang.equals("")){
            ngayHoc.addMonHocSang(sang);
        }
        if(!chieu.equals("")){
            ngayHoc.addMonHocChieu(chieu);
        }

        return ngayHoc;
    }

    private void chonNgay(TextView tvDate, Calendar calendar){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                SimpleDateFormat format = new SimpleDateFormat("EEEE, dd/MM/yyyy", Locale.getDefault());
                calendar.set(y, m, d);

                boolean isExist = NgayHocDatabase.getInstance(getActivity()).ngayHocDao().getNgayHoc(calendar) != null;
                if(isExist){
                    NgayHoc ngayHoc = NgayHocDatabase.getInstance(getActivity()).ngayHocDao().getNgayHoc(calendar);
                    edtSang.setText(ngayHoc.getStringSang());
                    edtChieu.setText(ngayHoc.getStringChieu());
                }else {
                    edtSang.getText().clear();
                    edtChieu.getText().clear();
                }

                tvDate.setText(format.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        datePickerDialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.show();
    }

    private void setControl(View decorView) {
        tvNgay = decorView.findViewById(R.id.tv_ngayhoc_ngay);
        edtSang = decorView.findViewById(R.id.edt_ngayhoc_monhocsang);
        edtChieu = decorView.findViewById(R.id.edt_ngayhoc_monhocchieu);
        btnThem  = decorView.findViewById(R.id.btn_ngayhoc_them);
        btnHuy  = decorView.findViewById(R.id.btn_ngayhoc_huy);

        ngay = Calendar.getInstance();
        ngay.set(Calendar.HOUR_OF_DAY, 0);
        ngay.set(Calendar.MINUTE, 0);
        ngay.set(Calendar.SECOND, 0);
        ngay.set(Calendar.MILLISECOND, 0);
    }

    private Dialog setDialog(Bundle bundle){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_themngayhoc);

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
}
