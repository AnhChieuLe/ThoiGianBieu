package com.example.thoigianbieu.fragment;

import static android.content.Context.WINDOW_SERVICE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.ngayhoc.NgayHoc;
import com.example.thoigianbieu.adapter.ThoiKhoaBieuAdapter;
import com.example.thoigianbieu.database.ngayhoc.NgayHocDatabase;
import com.example.thoigianbieu.model.ItemOffsetDecoration;
import com.example.thoigianbieu.model.NgayHocManager;
import com.example.thoigianbieu.setting.SharePreferencesManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThoiKhoaBieuFragment extends Fragment{

    boolean isHome;

    RecyclerView rcvThoiKhoaBieu;
    List<NgayHoc> listNgayHoc;
    ThoiKhoaBieuAdapter thoiKhoaBieuAdapter;
    NgayHocManager ngayHocManager;
    FloatingActionButton btnThemNgayHoc;
    Activity mActivity;
    Calendar changeTo;

    public ThoiKhoaBieuFragment(boolean isHome) {
        this.isHome = isHome;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thoikhoabieu, container, false);

        setControl(view);

        if(isHome)
            btnThemNgayHoc.setVisibility(View.GONE);

        setButton();

        loadData();

        SetRecyclerView();

        return view;
    }

    private void SetRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL);

        if (getRotation() == Surface.ROTATION_90 || getRotation() == Surface.ROTATION_270) {
            staggeredGridLayoutManager.setSpanCount(2);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        }

        //Đặt vị trí mặt định là ngày hôm nay
        if(isHome){
            linearLayoutManager.scrollToPosition(ngayHocManager.getDefaultPosition() + SharePreferencesManager.getTKBHome());
        }else {
            staggeredGridLayoutManager.scrollToPosition(ngayHocManager.getDefaultPosition());
        }

        if(isHome){
            //Bám view vào center
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(rcvThoiKhoaBieu);
            rcvThoiKhoaBieu.setLayoutManager(linearLayoutManager);
        }
        else {
            rcvThoiKhoaBieu.setLayoutManager(staggeredGridLayoutManager);
            SwipeToDelete();
            SwipeToSetDayOff();
        }


        ItemOffsetDecoration decoration = new ItemOffsetDecoration(mActivity, R.dimen.item_decoration);
        rcvThoiKhoaBieu.addItemDecoration(decoration);

        rcvThoiKhoaBieu.setAdapter(thoiKhoaBieuAdapter);
    }

    private void setButton(){
        btnThemNgayHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               themNgayHoc(true);
            }
        });
    }

    private int getRotation(){
        Display display = ((WindowManager) mActivity.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        return display.getRotation();
    }

    private void setControl(View view) {
        rcvThoiKhoaBieu = view.findViewById(R.id.rcv_thoikhoabieu);
        listNgayHoc = new ArrayList<>();

        thoiKhoaBieuAdapter = new ThoiKhoaBieuAdapter(getActivity(), isHome, new ThoiKhoaBieuAdapter.iClickAddData() {
            @Override
            public void addData() {
                themNgayHoc(true);
            }

            @Override
            public void hideButton() {
                btnThemNgayHoc.hide();
            }

            @Override
            public void showButton() {
                btnThemNgayHoc.show();
            }

            @Override
            public void onItemLongClick(NgayHoc ngayHoc, boolean isMorning) {
                setEditText(ngayHoc, isMorning);
            }
        });
        btnThemNgayHoc = view.findViewById(R.id.btn_themngayhoc);
    }

    private void setEditText(NgayHoc ngayHoc, boolean isMorning){
        LinearLayout container = new LinearLayout(mActivity);
        View view = getLayoutInflater().inflate(R.layout.themghichu_dialog, container);
        EditText content = view.findViewById(R.id.edt_content);
        RadioGroup importance = view.findViewById(R.id.group_importance);
        RadioButton high = view.findViewById(R.id.radio_importance_high);
        RadioButton normal = view.findViewById(R.id.radio_importance_normal);
        RadioButton low = view.findViewById(R.id.radio_importance_low);

        if(isMorning){
            switch (ngayHoc.getImportanceSang()){
                case 2:
                    high.setChecked(true);
                    break;
                case 1:
                    normal.setChecked(true);
                    break;
                case 0:
                    low.setChecked(true);
                    break;
            }
        }else{
            switch (ngayHoc.getImportanceChieu()){
                case 2:
                    high.setChecked(true);
                    break;
                case 1:
                    normal.setChecked(true);
                    break;
                case 0:
                    low.setChecked(true);
                    break;
            }
        }

        if(isMorning)
            content.setText(ngayHoc.getGhiChuSang());
        else
            content.setText(ngayHoc.getGhiChuChieu());

        DialogInterface.OnClickListener onClickPositive = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NgayHoc ngay = NgayHocDatabase.getInstance(mActivity).ngayHocDao().getNgayHoc(ngayHoc.getNgayHoc());

                if(ngay == null)    ngay = new NgayHoc(ngayHoc.getNgayHoc());

                if(isMorning)   ngay.setGhiChuSang(content.getText().toString().trim());
                else            ngay.setGhiChuChieu(content.getText().toString().trim());

                switch (importance.getCheckedRadioButtonId()){
                    case R.id.radio_importance_high:
                        if(isMorning)   ngay.setImportanceSang(2);
                        else    ngay.setImportanceChieu(2);
                        break;
                    case R.id.radio_importance_normal:
                        if(isMorning)   ngay.setImportanceSang(1);
                        else    ngay.setImportanceChieu(1);
                        break;
                    case R.id.radio_importance_low:
                        if(isMorning)   ngay.setImportanceSang(0);
                        else    ngay.setImportanceChieu(0);
                        break;
                }

                NgayHocDatabase.getInstance(mActivity).ngayHocDao().insertNgayHoc(ngay);
                loadData();
                dialog.dismiss();
            }
        };

        DialogInterface.OnClickListener onClickNegative = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(view)
                .setPositiveButton(R.string.them, onClickPositive)
                .setNegativeButton(R.string.huy, onClickNegative);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
        dialog.show();
        content.requestFocus();
    }

    private void themNgayHoc(boolean isNew){
        DialogThemNgayHocFragment dialog = new DialogThemNgayHocFragment(new DialogThemNgayHocFragment.DialogInterface() {
            @Override
            public void loadData() {
                ngayHocManager = new NgayHocManager(getActivity());
                listNgayHoc = ngayHocManager.getListNH();
                thoiKhoaBieuAdapter.setData(listNgayHoc);
            }

            @Override
            public void setNgay(Calendar ngay) {
                changeTo = ngay;
            }
        }, isNew);

        dialog.show(mActivity.getFragmentManager(), "addNH");
    }

    private void DayOff(NgayHoc ngayHoc, boolean set){
        List<NgayHoc> list = NgayHocDatabase.getInstance(mActivity).ngayHocDao().getListNgayHoc();

        ngayHoc.setEmptyMonHoc();

        for (NgayHoc ngayHoc1:list){
            if(!ngayHoc.getNgayHoc().equals(ngayHoc1.getNgayHoc()))     continue;
            ngayHoc = ngayHoc1;
        }

        ngayHoc.setDayOff(set);

        if(changeTo != null)
            ngayHoc.setChangeTo(changeTo);

        NgayHocDatabase.getInstance(mActivity).ngayHocDao().insertNgayHoc(ngayHoc);
        loadData();
        changeTo = null;
    }

    private void loadData(){
        ngayHocManager = new NgayHocManager(mActivity);

        listNgayHoc = ngayHocManager.getListNH();
        thoiKhoaBieuAdapter.setData(listNgayHoc);
    }

    private void SwipeToDelete(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getBindingAdapterPosition();
                if(listNgayHoc.size() == position)  return 0;
                NgayHoc ngayHoc = listNgayHoc.get(position);
                if(ngayHoc.isEmpty())   return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(R.string.canhbao)
                        .setMessage(R.string.xacnhanxoangayhoc)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int position = viewHolder.getBindingAdapterPosition();
                                NgayHoc ngayHoc = listNgayHoc.get(position);

                                if(ngayHoc.isDayOff()){
                                    ngayHoc.setEmptyMonHoc();
                                    NgayHocDatabase.getInstance(getActivity()).ngayHocDao().insertNgayHoc(ngayHoc);
                                }else
                                    NgayHocDatabase.getInstance(getActivity()).ngayHocDao().deleteNgayHoc(ngayHoc);

                                loadData();
                            }
                        })
                        .setNegativeButton(R.string.huy, new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
                dialog.show();
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvThoiKhoaBieu);
    }

    private void SwipeToSetDayOff(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getBindingAdapterPosition();

                if(listNgayHoc.size() == position)  return 0;

                NgayHoc ngayHoc = listNgayHoc.get(position);

                if(ngayHoc.isEmpty())   return 0;

                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                NgayHoc ngayHoc = listNgayHoc.get(position);
                showDayOffDialog(ngayHoc);
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvThoiKhoaBieu);
    }

    private void showDayOffDialog(NgayHoc ngayHoc){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        if(ngayHoc.isDayOff()){
            builder.setTitle(mActivity.getResources().getString(R.string.xoangaynghi))
                    .setMessage(R.string.xoalichnghidadat)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DayOff(ngayHoc, false);
                        }
                    })
                    .setNegativeButton(R.string.themlichhocbu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            themNgayHoc(false);
                        }
                    })
                    .setNeutralButton(R.string.huy, new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(android.content.DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
        }else{
            builder.setTitle(mActivity.getResources().getString(R.string.datngaynghi))
                    .setMessage(R.string.datngaynghivataolichhocbu)
                    .setPositiveButton(R.string.themlichhocbu, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DayOff(ngayHoc, true);
                            dialogInterface.dismiss();
                            themNgayHoc(false);
                        }
                    })
                    .setNegativeButton(R.string.khongthem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DayOff(ngayHoc, true);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNeutralButton(R.string.huy, new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(android.content.DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            loadData();
                        }
                    });
        }

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
        dialog.show();
    }
}