package com.example.thoigianbieu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.thoigianbieu.database.sukien.SuKien;
import com.example.thoigianbieu.database.sukien.SuKienDatabase;
import com.example.thoigianbieu.model.Result;
import com.example.thoigianbieu.notification.NotificationReceiver;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SuKienActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextInputLayout inputLayoutTitile;
    TextInputEditText edtTieuDe, edtNoiDung;
    TextView tvNgayBatDau, tvNgayKetThuc, tvNhacNho;
    SuKien suKien;
    Menu menu;
    MenuItem itemSave, itemDelete, itemNotification;

    Calendar ngayBatDau;
    Calendar ngayKetThuc;
    static int nhacNho;
    boolean saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sukien);

        setToobar();
        setControl();

        getData();
        setData();

        setNgayBatDauClick();
        setNgayKetThucClick();

        checkTitle();
        checkNoiDung();
    }

    private void setToobar(){
        toolbar = findViewById(R.id.toolbar_sukien);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setControl() {
        inputLayoutTitile = findViewById(R.id.layout_title);

        edtTieuDe = findViewById(R.id.edt_sukien_activity_tieude);
        edtNoiDung = findViewById(R.id.tv_sukien_activity_noidung);
        tvNgayBatDau = findViewById(R.id.tv_sukien_activity_batdau);
        tvNgayKetThuc = findViewById(R.id.tv_sukien_activity_ketthuc);
        tvNhacNho = findViewById(R.id.tv_sukien_activity_nhacnho);

        ngayBatDau = Calendar.getInstance();
        ngayBatDau.set(Calendar.SECOND, 0);
        ngayBatDau.set(Calendar.MILLISECOND, 0);

        ngayKetThuc = Calendar.getInstance();
        ngayKetThuc.set(Calendar.SECOND, 0);
        ngayKetThuc.set(Calendar.MILLISECOND, 0);

        nhacNho = 0;
        saved = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.sukien_toolbar_menu, menu);

        itemSave = menu.findItem(R.id.sukien_toolbar_save);
        itemDelete = menu.findItem(R.id.sukien_toolbar_delete);
        itemNotification = menu.findItem(R.id.sukien_toolbar_notification);

        if(!saved){
           itemSave.setVisible(false);
           itemDelete.setVisible(false);
           itemNotification.setVisible(false);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if(saved || (edtTieuDe.getText().toString().trim().equals("") && edtNoiDung.getText().toString().trim().equals(""))){
            super.onBackPressed();
            setResult(Result.RESULT_SAVE);
            finish();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.canhbao)
                    .setMessage(R.string.sukienchualuu)
                    .setPositiveButton(R.string.khong_luu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saved = true;
                            builder.create().dismiss();
                            onBackPressed();
                            finish();
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
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sukien_toolbar_save:
                save();
                break;
            case R.id.sukien_toolbar_delete:
                delete();
                break;
            case R.id.sukien_toolbar_notification:
                setNotification(suKien);
                break;
            default:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra("package");
        if(bundle != null){
            suKien = (SuKien) bundle.getSerializable("sukien");
            saved = true;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData(){
        if(saved){
            edtTieuDe.setText(suKien.getTieuDe());
            edtNoiDung.setText(suKien.getNoiDung());

            ngayBatDau = suKien.getNgayBatDau();
            ngayKetThuc = suKien.getNgayKetThuc();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("HH:mm - dd/MM/yyyy");

            tvNgayBatDau.setText(getString(R.string.batdau) + format.format(ngayBatDau.getTime()));
            tvNgayKetThuc.setText(getString(R.string.ketthuc) + format.format(ngayKetThuc.getTime()));
            tvNhacNho.setText(getString(R.string.nhacnho) + suKien.getNhacNho() + getString(R.string.phut));
        }
    }

    private void checkTitle(){
        edtTieuDe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().equals("")){
                    itemSave.setVisible(false);
                }
                else {
                    itemSave.setVisible(true);
                }
            }
        });
    }

    private void checkNoiDung(){
        edtNoiDung.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().contains("- ")) {
                    Editable ab = new SpannableStringBuilder(editable.toString().replace("- ", " • "));
                    editable.replace(0, editable.length(), ab);
                }
            }
        });
    }

    private void save(){
        String title = edtTieuDe.getText().toString();
        String noidung = edtNoiDung.getText().toString();

        if(!saved){
            suKien = new SuKien(title, noidung, ngayBatDau, ngayKetThuc, nhacNho);
            SuKienDatabase.getInstance(this).suKienDao().insertSuKien(suKien);
        }else {
            suKien.setTieuDe(title);
            suKien.setNoiDung(noidung);
            suKien.setNgayBatDau(ngayBatDau);
            suKien.setNgayKetThuc(ngayKetThuc);
            suKien.setNhacNho(nhacNho);

            SuKienDatabase.getInstance(this).suKienDao().updateSuKien(suKien);
        }

        itemDelete.setVisible(true);
        itemNotification.setVisible(true);

        saved = true;
    }

    private void delete(){
        SuKienDatabase.getInstance(this).suKienDao().deleteSuKien(suKien);
        onBackPressed();
    }

    private void setNhacNho(TextView tv){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] listItem = {getString(R.string.minute_0), getString(R.string.minute_5), getString(R.string.minute_15), getString(R.string.minute_30), getString(R.string.tuychinh)};
        builder.setTitle(R.string.nhacnho)
                .setItems(listItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which){
                            case 0:
                                nhacNho = 0;
                                break;
                            case 1:
                                nhacNho = 5;
                                break;
                            case 2:
                                nhacNho = 15;
                                break;
                            case 3:
                                nhacNho = 30;
                                break;
                            case 4:
                                setEditText();
                                break;
                            default:
                                nhacNho = 0;
                        }
                        tv.setText(getString(R.string.nhactruoc) + nhacNho + getString(R.string.phut));
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
        dialog.show();
    }

    private void setEditText(){
        EditText edit = new EditText(this);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        DialogInterface.OnClickListener onClickPositive = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nhacNho = Integer.parseInt(edit.getText().toString().trim());
                dialog.dismiss();
            }
        };

        DialogInterface.OnClickListener onClickNegative = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(edit)
                .setTitle("Tuỳ chỉnh")
                .setPositiveButton(R.string.ok, onClickPositive)
                .setNegativeButton(R.string.huy, onClickNegative);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
        dialog.show();
    }

    private void chonNgay(TextView date, Calendar calendar, boolean isBatDau){
        Calendar index = (Calendar)calendar.clone();
        //Set TimePickerDialog
        TimePickerDialog.OnTimeSetListener listenerTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                if(ngayBatDau.compareTo(ngayKetThuc) > 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SuKienActivity.this);
                    builder.setTitle("Sai thời gian")
                            .setMessage("Thời gian kết thúc phải lớn hơn thời gian bắt đầu")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    calendar.set(index.get(Calendar.YEAR), index.get(Calendar.MONTH), index.get(Calendar.DATE)
                            , index.get(Calendar.HOUR_OF_DAY), index.get(Calendar.MINUTE));
                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
                    dialog.show();
                }{
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
                    String str = isBatDau?"Bắt đầu: ":"Kết thúc: ";
                    date.setText(str + format.format(calendar.getTime()));
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(SuKienActivity.this, listenerTime,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);

        //Set DatePickerDialog
        DatePickerDialog.OnDateSetListener listenerDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DATE, dayOfMonth);

                timePickerDialog.show();
            }
        };

        //Show dialog
        DatePickerDialog dialog = new DatePickerDialog(this, listenerDate,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
        dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        dialog.show();
    }

    private void setNgayBatDauClick(){
        tvNgayBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonNgay(tvNgayBatDau, ngayBatDau, true);
            }
        });
    }

    private void setNgayKetThucClick(){
        tvNgayKetThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonNgay(tvNgayKetThuc, ngayKetThuc, false);
            }
        });
    }

    public void setNotification(SuKien suKien){
        if(suKien.getNgayBatDau().compareTo(Calendar.getInstance()) < 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông báo")
                    .setMessage(suKien.getNgayKetThuc().compareTo(Calendar.getInstance()) <= 0?"Sự kiện đã kết thúc":"Sự kiện đang diễn ra")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
            dialog.show();
            return;
        }

        setNhacNho(tvNhacNho);

        Intent intent = new Intent(this, NotificationReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sukien", this.suKien);
        intent.putExtra("package", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, suKien.getNgayBatDau().getTimeInMillis() - nhacNho* 60000L - 10000, pendingIntent);
    }
}
