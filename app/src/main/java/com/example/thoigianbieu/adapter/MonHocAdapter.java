package com.example.thoigianbieu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.monhoc.MonHoc;

import java.util.List;

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
        if(monHoc == null)  return;

        holder.cardMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.itemClick(monHoc);
            }
        });

        if(monHoc.isPast()){
            holder.cardMonHoc.setAlpha(0.75F);
        }

        holder.tenMonHoc.setText(monHoc.getTenMonHoc());

        holder.tenGiangVien.setText(R.string.teacher);
        holder.tenGiangVien.append(monHoc.getTenGiangVien().equals("")?context.getString(R.string.chuathem):monHoc.getTenGiangVien());

        holder.phongHoc.setText(R.string.room);
        holder.phongHoc.append(monHoc.getPhongHoc().equals("")?context.getString(R.string.chuathem):monHoc.getPhongHoc());

        holder.buoiHoc.setText(R.string.time);
        holder.buoiHoc.append(monHoc.getStringBuoiHoc(context));
    }

    @Override
    public int getItemCount() {
        return listMonHoc==null?0:listMonHoc.size();
    }

    public static class MonHocViewHolder extends RecyclerView.ViewHolder{
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
