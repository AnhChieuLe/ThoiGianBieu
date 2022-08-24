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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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
import java.util.Locale;
import java.util.Objects;

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
    boolean isSave;
    boolean isChange;
    long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sukien);

        setToolbar();
        setControl();

        getData();
        setData();

        setNgayBatDauClick();
        setNgayKetThucClick();

        checkTitle();
        checkNoiDung();
    }

    private void setToolbar(){
        toolbar = findViewById(R.id.toolbar_sukien);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

        nhacNho = 0;
        isSave = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.sukien_toolbar_menu, menu);

        itemSave = menu.findItem(R.id.sukien_toolbar_save);
        itemDelete = menu.findItem(R.id.sukien_toolbar_delete);
        itemNotification = menu.findItem(R.id.sukien_toolbar_notification);

        if(!isSave){
           itemSave.setVisible(false);
           itemDelete.setVisible(false);
           itemNotification.setVisible(false);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if(isChange){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.canhbao)
                    .setMessage(R.string.sukienchualuu)
                    .setPositiveButton(R.string.dontsave, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isSave = true;
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.huy, new DialogInterface.OnClickListener() {
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
        if(isSave || (edtTieuDe.getText().toString().trim().equals("") && edtNoiDung.getText().toString().trim().equals(""))){
            super.onBackPressed();
            setResult(Result.RESULT_SAVE);
            finish();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.sukien_toolbar_save:
                save();
                break;
            case R.id.sukien_toolbar_delete:
                delete();
                break;
            case R.id.sukien_toolbar_notification:
                setNotification(suKien);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra("package");
        if(bundle != null){
            suKien = (SuKien) bundle.getSerializable("sukien");
            isSave = true;
        }
    }

    private void setData(){
        if(!isSave)  return;
        edtTieuDe.setText(suKien.getTieuDe());
        edtNoiDung.setText(suKien.getNoiDung());

        ngayBatDau = suKien.getNgayBatDau();
        ngayKetThuc = suKien.getNgayKetThuc();

        SimpleDateFormat format;
        if(ngayBatDau.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR))
            format = new SimpleDateFormat("HH:mm - dd/MM", Locale.getDefault());
        else
            format = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault());
        tvNgayBatDau.setText(getString(R.string.batdau, format.format(ngayBatDau.getTime())));


        if(ngayKetThuc.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR))
            format = new SimpleDateFormat("HH:mm - dd/MM", Locale.getDefault());
        else
            format = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault());
        tvNgayKetThuc.setText(getString(R.string.ketthuc, format.format(ngayKetThuc.getTime())));


        tvNhacNho.setText(getString(R.string.nhactruoc, suKien.getNhacNho()));
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
                isChange = true;
                if(edtNoiDung.getText() == null) return;
                if(s.toString().trim().equals("")){
                    itemSave.setVisible(false);
                }else if(!edtNoiDung.getText().toString().trim().equals("")){
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
            public void afterTextChanged(Editable s) {
                isChange = true;
                if(edtTieuDe.getText() == null) return;
                if(s.toString().trim().equals("")){
                    itemSave.setVisible(false);
                }else if(!edtTieuDe.getText().toString().trim().equals("")){
                    itemSave.setVisible(true);
                }
            }
        });
    }

    private void getSuKien(){
        if(edtTieuDe.getText() == null || edtNoiDung.getText() == null) return;
        String title = edtTieuDe.getText().toString().trim();
        String noidung = edtNoiDung.getText().toString().trim();

        if(ngayBatDau == null)
            ngayBatDau = Calendar.getInstance();
        if(ngayKetThuc == null)
            ngayKetThuc = Calendar.getInstance();
        if(ngayKetThuc.compareTo(ngayBatDau) < 0)
            ngayKetThuc = (Calendar) ngayBatDau.clone();

        if(!isSave){
            suKien = new SuKien(title, noidung, ngayBatDau, ngayKetThuc, nhacNho);
        }else {
            suKien.setTieuDe(title);
            suKien.setNoiDung(noidung);
            suKien.setNgayBatDau(ngayBatDau);
            suKien.setNgayKetThuc(ngayKetThuc);
            suKien.setNhacNho(nhacNho);
        }

        Context context = getApplicationContext();
        context.getString(R.string.nhactruoc);
    }

    private void save(){
        getSuKien();

        itemDelete.setVisible(true);
        itemNotification.setVisible(true);

        isSave = true;
        isChange = false;

        id = SuKienDatabase.getInstance(this).suKienDao().insertSuKienWithResult(suKien);
        suKien.setId((int) id);
    }

    private void delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.canhbao)
                .setMessage(R.string.xacnhanxoasukien)
                .setPositiveButton(R.string.xoa, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SuKienDatabase.getInstance(SuKienActivity.this).suKienDao().deleteSuKien(suKien);
                        dialog.dismiss();
                        SuKienActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.huy, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
        dialog.show();
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
                        }
                        tv.setText(getString(R.string.nhactruoc, nhacNho));
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
                .setTitle(R.string.tuychinh)
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

                if(ngayBatDau != null && ngayKetThuc != null && ngayBatDau.compareTo(ngayKetThuc) > 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SuKienActivity.this);
                    String title = getResources().getString(R.string.saithoigian);
                    builder.setTitle(title)
                            .setMessage(R.string.thoigianketthucphailonhonbatdau)
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
                    return;
                }
                SimpleDateFormat format;
                if(calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR))
                    format = new SimpleDateFormat("HH:mm - dd/MM", Locale.getDefault());
                else
                    format = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault());

                String str = getResources().getString(isBatDau?R.string.batdau:R.string.ketthuc, format.format(calendar.getTime()));
                date.setText(str);
                isChange = true;
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
                ngayBatDau = Calendar.getInstance();
                ngayBatDau.set(Calendar.SECOND, 0);
                ngayBatDau.set(Calendar.MILLISECOND, 0);
                chonNgay(tvNgayBatDau, ngayBatDau, true);
            }
        });
    }

    private void setNgayKetThucClick(){
        tvNgayKetThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ngayKetThuc = Calendar.getInstance();
                ngayKetThuc.set(Calendar.SECOND, 0);
                ngayKetThuc.set(Calendar.MILLISECOND, 0);
                chonNgay(tvNgayKetThuc, ngayKetThuc, false);
            }
        });
    }

    public void setNotification(SuKien suKien){
        if(suKien.getNgayBatDau().compareTo(Calendar.getInstance()) <= 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.notification)
                    .setMessage(suKien.getNgayKetThuc().compareTo(Calendar.getInstance()) <= 0?R.string.sukiendaketthuc:R.string.sukiendangdienra)
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

        alarmManager.set(AlarmManager.RTC_WAKEUP, suKien.getNgayBatDau().getTimeInMillis() - nhacNho* 60000L, pendingIntent);
    }
}