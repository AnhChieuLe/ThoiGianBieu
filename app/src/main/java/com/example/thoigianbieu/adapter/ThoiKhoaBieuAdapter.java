package com.example.thoigianbieu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.ngayhoc.NgayHoc;

import java.util.Calendar;
import java.util.List;

public class ThoiKhoaBieuAdapter extends RecyclerView.Adapter<ThoiKhoaBieuAdapter.ThoiKhoaBieuViewHolder>{

    List<NgayHoc> listNgayHoc;
    boolean isHome;
    Context context;

    iClickAddData clickAddData;

    public ThoiKhoaBieuAdapter(Context context, boolean isHome, iClickAddData clickAddData) {
        this.context = context;
        this.isHome = isHome;
        this.clickAddData = clickAddData;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<NgayHoc> listNgayHoc){
        this.listNgayHoc = listNgayHoc;
        this.listNgayHoc.sort(new NgayHoc(Calendar.getInstance()));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThoiKhoaBieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == R.layout.item_thoikhoabieu){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thoikhoabieu, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add, parent, false);
        }
        return new ThoiKhoaBieuViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == listNgayHoc.size())?R.layout.item_add :R.layout.item_thoikhoabieu;
    }

    @Override
    public void onBindViewHolder(@NonNull ThoiKhoaBieuViewHolder holder, int position) {
        if(position == listNgayHoc.size()){
            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAddData.addData();
                }
            });
            holder.btnAdd.setText(R.string.themthoikhoabieu);
        }else {
            NgayHoc ngayHoc = listNgayHoc.get(position);

            holder.tvNgayHoc.setText(ngayHoc.getStringNgayHoc(context));

            if(ngayHoc.isSunDay()){
                holder.tvNgayHoc.setBackgroundResource(R.color.date_chunhat);
            }
            else if(ngayHoc.isToDay()){
                holder.tvNgayHoc.setBackgroundResource(R.color.primary_yellow);
            }
            else if(ngayHoc.isPast()){
                holder.tvNgayHoc.setBackgroundResource(R.color.gray);
            }else{
                holder.tvNgayHoc.setBackgroundResource(R.color.normal_yellow);
            }

            String sang = ngayHoc.getStringSang();
            String chieu = ngayHoc.getStringChieu();

            if(ngayHoc.getMonHocSang().size()==0){
                holder.tvMonHocSang.setText(R.string.trong);
            }else {
                holder.tvMonHocSang.setText(sang);
            }

            if(ngayHoc.getMonHocChieu().size()==0){
                holder.tvMonHocChieu.setText(R.string.trong);
            }else {
                holder.tvMonHocChieu.setText(chieu);
            }

            if(!isHome){
                holder.tvMonHocSang.setSingleLine(false);
                holder.tvMonHocChieu.setSingleLine(false);
            }else{
                holder.tvMonHocSang.setSingleLine(true);
                holder.tvMonHocChieu.setSingleLine(true);
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ThoiKhoaBieuViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder.btnAdd == null)   return;
        clickAddData.showButton();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ThoiKhoaBieuViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(holder.btnAdd == null)   return;
        clickAddData.hideButton();
    }

    @Override
    public int getItemCount() {
        return listNgayHoc.size()+1;
    }

    public interface iClickAddData{
        void addData();
        void hideButton();
        void showButton();
    }

    public static class ThoiKhoaBieuViewHolder extends RecyclerView.ViewHolder{
        TextView tvNgayHoc;
        TextView tvMonHocSang;
        TextView tvMonHocChieu;
        Button btnAdd;

        public ThoiKhoaBieuViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNgayHoc = itemView.findViewById(R.id.tv_thoikhoabieu_ngayhoc);
            tvMonHocSang = itemView.findViewById(R.id.tv_thoikhoabieu_monhocsang);
            tvMonHocChieu = itemView.findViewById(R.id.tv_thoikhoabieu_monhocchieu);

            btnAdd = itemView.findViewById(R.id.btn_item_add);
        }
    }
}
