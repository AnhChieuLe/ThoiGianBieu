package com.example.thoigianbieu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.monhoc.MonHoc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MonHocAdapter extends RecyclerView.Adapter<MonHocAdapter.MonHocViewHolder> {

    private List<MonHoc> listMonHoc;
    private final ItemClick itemClick;
    Context context;

    public interface ItemClick{
        void itemClick(MonHoc monHoc);
    }

    public MonHocAdapter(Context context, ItemClick itemClick) {
        this.context = context;
        this.itemClick = itemClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<MonHoc> listMonHoc){
        this.listMonHoc = listMonHoc;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MonHocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monhoc, parent, false);
        return new MonHocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonHocViewHolder holder, int position) {
        MonHoc monHoc = listMonHoc.get(position);
        if(monHoc == null) {
            return;
        }
        holder.cardMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.itemClick(monHoc);
            }
        });
        if(monHoc.getNgayKetThuc().compareTo(Calendar.getInstance())<0){
            holder.cardMonHoc.setAlpha(0.75F);
        }
        holder.tenMonHoc.setText(monHoc.getTenMonHoc());
        holder.tenGiangVien.setText(R.string.giang_vien);
        holder.tenGiangVien.append(monHoc.getTenGiangVien());
        holder.phongHoc.setText(R.string.phong_hoc);
        holder.phongHoc.append(monHoc.getPhongHoc());

        ArrayList<String> buoiHoc = monHoc.getBuoiHoc();
        StringBuilder strBuoiHoc = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        if(buoiHoc.size() != 0){
            for (int i = 0; i < buoiHoc.size(); i++) {
                for (int j = 1; j < 8; j++) {
                    if (buoiHoc.get(i).indexOf("" + j) == 0) {
                        calendar.set(Calendar.DAY_OF_WEEK, j);
                        if (buoiHoc.get(i).indexOf("S") == 1) {
                            strBuoiHoc.append(context.getResources().getString(R.string.sang)).append(" ");
                        }
                        if (buoiHoc.get(i).indexOf("C") == 1) {
                            strBuoiHoc.append(context.getResources().getString(R.string.chieu)).append(" ");
                        }
                        strBuoiHoc.append(simpleDateFormat.format(calendar.getTime())).append(", ");
                        break;
                    }
                }
            }
        }

        String str = strBuoiHoc.toString();
        str = str.toLowerCase();
        if(!str.equals("")){
            str = str.substring(0,1).toUpperCase() + str.substring(1);
            str = str.substring(0, str.lastIndexOf(","));
        }
        holder.buoiHoc.setText(R.string.thoi_gian);
        holder.buoiHoc.append(str);
    }

    @Override
    public int getItemCount() {
        return listMonHoc==null?0:listMonHoc.size();
    }

    public class MonHocViewHolder extends RecyclerView.ViewHolder{
        private final TextView tenMonHoc;
        private final TextView tenGiangVien;
        private final TextView buoiHoc;
        private final TextView phongHoc;
        private final CardView cardMonHoc;


        public MonHocViewHolder(@NonNull View itemView) {
            super(itemView);

            tenMonHoc = itemView.findViewById(R.id.tv_monhoc_tenmonhoc);
            tenGiangVien = itemView.findViewById(R.id.tv_monhoc_tengiangvien);
            buoiHoc = itemView.findViewById(R.id.tv_monhoc_thoigian);
            phongHoc = itemView.findViewById(R.id.tv_monhoc_phonghoc);
            cardMonHoc = itemView.findViewById(R.id.card_monhoc);
        }
    }
}
