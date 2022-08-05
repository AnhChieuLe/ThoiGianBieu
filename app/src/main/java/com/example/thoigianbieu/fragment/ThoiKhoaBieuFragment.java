package com.example.thoigianbieu.fragment;

import static android.content.Context.WINDOW_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

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
import java.util.List;

public class ThoiKhoaBieuFragment extends Fragment {

    boolean isHome;

    RecyclerView rcvThoiKhoaBieu;
    List<NgayHoc> listNgayHoc;
    ThoiKhoaBieuAdapter thoiKhoaBieuAdapter;
    NgayHocManager ngayHocManager;
    FloatingActionButton btnThemNgayHoc;
    Activity mActivity;

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

        if(isHome){
            btnThemNgayHoc.setVisibility(View.GONE);
        }

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
        }


        ItemOffsetDecoration decoration = new ItemOffsetDecoration(mActivity, R.dimen.item_decoration);
        rcvThoiKhoaBieu.addItemDecoration(decoration);

        rcvThoiKhoaBieu.setAdapter(thoiKhoaBieuAdapter);
    }

    private void setButton(){
        btnThemNgayHoc.hide();
        if(!isHome){
            rcvThoiKhoaBieu.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if(dy<0){
                        btnThemNgayHoc.show();
                    }else {
                        btnThemNgayHoc.hide();
                    }
                }
            });
        }
        btnThemNgayHoc.setOnClickListener(new View.OnClickListener() {
            final DialogThemNgayHocFragment dialog = new DialogThemNgayHocFragment(new DialogThemNgayHocFragment.LoadData() {
                @Override
                public void loadData() {
                    ngayHocManager = new NgayHocManager(getActivity());

                    listNgayHoc = ngayHocManager.getListNH();
                    thoiKhoaBieuAdapter.setData(listNgayHoc);
                }
            });
            @Override
            public void onClick(View v) {
               dialog.show(mActivity.getFragmentManager(), "addngayhoc");
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


        DialogThemNgayHocFragment dialog = new DialogThemNgayHocFragment(new DialogThemNgayHocFragment.LoadData() {
            @Override
            public void loadData() {
                ngayHocManager = new NgayHocManager(getActivity());
                listNgayHoc = ngayHocManager.getListNH();
                thoiKhoaBieuAdapter.setData(listNgayHoc);
            }
        });

        thoiKhoaBieuAdapter = new ThoiKhoaBieuAdapter(getActivity(), isHome, new ThoiKhoaBieuAdapter.iClickAddData() {
            @Override
            public void addData() {
                dialog.show(mActivity.getFragmentManager(), "addNH");
            }
        });
        btnThemNgayHoc = view.findViewById(R.id.btn_themngayhoc);
    }

    private void loadData(){
        ngayHocManager = new NgayHocManager(getActivity());

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
                if(listNgayHoc.size() == viewHolder.getBindingAdapterPosition()){
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                NgayHoc ngayHoc = listNgayHoc.get(position);
                NgayHocDatabase.getInstance(getActivity()).ngayHocDao().deleteNgayHoc(ngayHoc);
                loadData();
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvThoiKhoaBieu);
    }
}