package com.example.thoigianbieu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.ngayhoc.NgayHoc;
import com.example.thoigianbieu.setting.SharePreferencesManager;

import java.util.List;

public class ThoiKhoaBieuAdapter extends RecyclerView.Adapter<ThoiKhoaBieuAdapter.ThoiKhoaBieuViewHolder>{

    List<NgayHoc> listNgayHoc;
    boolean isHome;
    Context context;

    public ThoiKhoaBieuAdapter(Context context, boolean isHome) {
        this.context = context;
        this.isHome = isHome;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<NgayHoc> listNgayHoc){
        this.listNgayHoc = listNgayHoc;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThoiKhoaBieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thoikhoabieu, parent, false);
        return new ThoiKhoaBieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThoiKhoaBieuViewHolder holder, int position) {
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

    @Override
    public int getItemCount() {
        return listNgayHoc==null?0:listNgayHoc.size();
    }

    public class ThoiKhoaBieuViewHolder extends RecyclerView.ViewHolder{
        TextView tvNgayHoc;
        TextView tvMonHocSang;
        TextView tvMonHocChieu;

        public ThoiKhoaBieuViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNgayHoc = itemView.findViewById(R.id.tv_thoikhoabieu_ngayhoc);
            tvMonHocSang = itemView.findViewById(R.id.tv_thoikhoabieu_monhocsang);
            tvMonHocChieu = itemView.findViewById(R.id.tv_thoikhoabieu_monhocchieu);
        }
    }
}
