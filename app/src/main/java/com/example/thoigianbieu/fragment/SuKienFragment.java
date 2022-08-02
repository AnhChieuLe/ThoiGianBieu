package com.example.thoigianbieu.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.thoigianbieu.MainActivity;
import com.example.thoigianbieu.R;
import com.example.thoigianbieu.SuKienActivity;
import com.example.thoigianbieu.adapter.SuKienAdaptter;
import com.example.thoigianbieu.database.sukien.SuKien;
import com.example.thoigianbieu.database.sukien.SuKienDatabase;
import com.example.thoigianbieu.model.ItemOffsetDecoration;
import com.example.thoigianbieu.model.SuKienSnapHelper;
import com.example.thoigianbieu.setting.SharePreferencesManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class SuKienFragment extends Fragment{

    boolean isHome;

    RecyclerView rcvSuKien;
    SuKienAdaptter adaptter;
    List<SuKien> listSuKien;
    Button btnThemSuKien;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    loadData();
                }
            });

    public SuKienFragment(boolean isHome) {
        this.isHome = isHome;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sukien, container, false);
        setControl(view);

        setRecyclerView();
        hideButton();
        setOnBottomButtonClick();

        return view;
    }

    private void setRecyclerView(){
        int style = SharePreferencesManager.getStyleSuKien();
        int styleHome = SharePreferencesManager.getStyleSuKienHome();
        int column = Math.max(SharePreferencesManager.getSoCotSuKien(), 1);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(column, RecyclerView.VERTICAL);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), column);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        if(!isHome){
            if(style==0){
                rcvSuKien.setLayoutManager(staggeredGridLayoutManager);
            }else if(style == 1){
                rcvSuKien.setLayoutManager(gridLayoutManager);
            }else {
                rcvSuKien.setLayoutManager(linearLayoutManager);
            }

            SwipeToDelete();
        }
        else {
            if(styleHome==0){
                rcvSuKien.setLayoutManager(staggeredGridLayoutManager);
            }else if(styleHome == 1){
                rcvSuKien.setLayoutManager(gridLayoutManager);
                //Bám itemView to top
                SnapHelper snapHelper = new SuKienSnapHelper();
                snapHelper.attachToRecyclerView(rcvSuKien);
            }else {
                rcvSuKien.setLayoutManager(linearLayoutManager);
                //Bám itemView to top
                SnapHelper snapHelper = new SuKienSnapHelper();
                snapHelper.attachToRecyclerView(rcvSuKien);
            }
        }

        ItemOffsetDecoration decoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_decoration);
        rcvSuKien.addItemDecoration(decoration);

        loadData();
        rcvSuKien.setAdapter(adaptter);
    }

    private void hideButton(){
        rcvSuKien.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void setControl(View view) {
        rcvSuKien = view.findViewById(R.id.rcv_sukien);
        adaptter = new SuKienAdaptter(new SuKienAdaptter.ItemClick() {
            @Override
            public void clickItem(SuKien suKien) {
                itemClick(suKien);
            }
            @Override
            public void clickAdd() {
                Intent intent = new Intent(getActivity(), SuKienActivity.class);
                activityResultLauncher.launch(intent);
            }
        }, isHome);
        btnThemSuKien = view.findViewById(R.id.btn_sukien_themsukien);
    }

    private void itemClick(SuKien suKien){
        Intent intent = new Intent(getActivity(), SuKienActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sukien", suKien);
        intent.putExtra("package", bundle);
        activityResultLauncher.launch(intent);
    }

    public void loadData(){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        Calendar calendar2 = (Calendar) calendar1.clone();
        calendar2.add(Calendar.DATE, 1);
        if(isHome){
            listSuKien = SuKienDatabase.getInstance(getActivity()).suKienDao().getListSuKien(calendar1, calendar2);
        } else {
            listSuKien = SuKienDatabase.getInstance(getActivity()).suKienDao().getListSuKien();
        }
        adaptter.setData(listSuKien);
    }

    private void setOnBottomButtonClick(){
        btnThemSuKien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SuKienActivity.class);
                activityResultLauncher.launch(intent);
            }
        });
        if(listSuKien.size() == 0){
            btnThemSuKien.setVisibility(View.GONE);
        }
        if(!isHome){
            btnThemSuKien.setVisibility(View.GONE);
        }
    }

    private void SwipeToDelete(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                if(viewHolder.getBindingAdapterPosition() == listSuKien.size())     return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.canhbao)
                        .setMessage("Xác nhận xoá sự kiện này")
                        .setPositiveButton(R.string.xoa, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int position = viewHolder.getBindingAdapterPosition();
                                SuKien suKien = listSuKien.get(position);
                                SuKienDatabase.getInstance(getActivity()).suKienDao().deleteSuKien(suKien);
                                dialog.dismiss();
                                loadData();
                            }
                        })
                        .setNegativeButton(R.string.huy, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                loadData();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
                dialog.show();
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvSuKien);
    }
}

