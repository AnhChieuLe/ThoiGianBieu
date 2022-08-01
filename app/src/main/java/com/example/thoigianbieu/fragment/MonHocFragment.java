package com.example.thoigianbieu.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.adapter.MonHocAdapter;
import com.example.thoigianbieu.database.monhoc.MonHoc;
import com.example.thoigianbieu.database.monhoc.MonHocDatabase;
import com.example.thoigianbieu.database.sukien.SuKien;
import com.example.thoigianbieu.database.sukien.SuKienDatabase;
import com.example.thoigianbieu.model.ItemOffsetDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonHocFragment extends Fragment {

    RecyclerView rcvMonHoc;
    List<MonHoc> listMonHoc;
    MonHocAdapter monHocAdapter;
    FloatingActionButton btnThemMonHoc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monhoc, container, false);

        setControl(view);

        setRecyclervView();

        SwipeToDelete();

        HideButton();

        ButtonClick();

        return view;
    }

    //RecyclerView
    private void setRecyclervView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcvMonHoc.setLayoutManager(linearLayoutManager);
        loadData();
        rcvMonHoc.setAdapter(monHocAdapter);

        // Set Decoration
        ItemOffsetDecoration decoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_decoration);
        rcvMonHoc.addItemDecoration(decoration);
    }

    //Vuốt để xóa
    private void SwipeToDelete(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                MonHoc monHoc = listMonHoc.get(position);
                MonHocDatabase.getInstance(getActivity()).monHocDAO().deleteMonHoc(monHoc);
                loadData();
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvMonHoc);
    }

    // Show | Hide Button
    private void HideButton(){
        rcvMonHoc.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    btnThemMonHoc.hide();
                }
                else {
                    btnThemMonHoc.show();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    // Button click
    private void ButtonClick(){
        btnThemMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogThemMonHocFragment dialog = new DialogThemMonHocFragment(new DialogThemMonHocFragment.LoadData() {
                    @Override
                    public void LoadData() {
                        loadData();
                    }
                });
                dialog.show(getActivity().getFragmentManager(), "add");
            }
        });
    }

    private void setControl(View view) {
        monHocAdapter = new MonHocAdapter(getActivity(), new MonHocAdapter.ItemClick() {
            @Override
            public void itemClick(MonHoc monHoc) {
                DialogThemMonHocFragment dialog = new DialogThemMonHocFragment(monHoc, new DialogThemMonHocFragment.LoadData() {
                    @Override
                    public void LoadData() {
                        loadData();
                    }
                });
                dialog.show(getActivity().getFragmentManager(), "edit");
            }
        });
        rcvMonHoc = view.findViewById(R.id.rcv_monhoc);
        btnThemMonHoc = view.findViewById(R.id.btn_themmonhoc);
    }

    public void loadData(){
        listMonHoc = MonHocDatabase.getInstance(getActivity()).monHocDAO().getListMonHoc();
        monHocAdapter.setData(listMonHoc);
    }
}
