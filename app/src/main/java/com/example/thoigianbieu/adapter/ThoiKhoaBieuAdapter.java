package com.example.thoigianbieu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.ngayhoc.NgayHoc;
import com.google.android.material.divider.MaterialDivider;

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
        if(viewType == R.layout.item_thoikhoabieu)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thoikhoabieu, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add, parent, false);

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
            return;
        }

        NgayHoc ngayHoc = listNgayHoc.get(position);

        holder.tvNgayHoc.setText(ngayHoc.getStringNgayHoc(context));

        if(ngayHoc.isSunDay())
            holder.tvNgayHoc.setBackgroundResource(R.color.sunday);
        else if(ngayHoc.isToDay())
            holder.tvNgayHoc.setBackgroundResource(R.color.primary_yellow);
        else if(ngayHoc.isPast())
            holder.tvNgayHoc.setBackgroundResource(R.color.gray);
        else
            holder.tvNgayHoc.setBackgroundResource(R.color.normal_yellow);

        String sang = ngayHoc.getStringSang();
        String chieu = ngayHoc.getStringChieu();

        if(ngayHoc.getMonHocSang().size()==0)
            holder.tvMonHocSang.setText(R.string.trong);
        else
            holder.tvMonHocSang.setText(sang);

        if(ngayHoc.getMonHocChieu().size()==0)
            holder.tvMonHocChieu.setText(R.string.trong);
        else
            holder.tvMonHocChieu.setText(chieu);

        if(!isHome){
            holder.tvMonHocSang.setSingleLine(false);
            holder.tvMonHocChieu.setSingleLine(false);
        }else{
            holder.tvMonHocSang.setSingleLine(true);
            holder.tvMonHocChieu.setSingleLine(true);
        }

        
        if(!ngayHoc.getGhiChuSang().equals("") && !isHome){
            holder.tvGhiChuSang.setVisibility(View.VISIBLE);
            String ghichu = context.getResources().getString(R.string.ghichu, ngayHoc.getGhiChuSang());
            holder.tvGhiChuSang.setText(ghichu);
        }else
            holder.tvGhiChuSang.setVisibility(View.GONE);

        if(ngayHoc.getImportanceSang() == 2)
            holder.tvGhiChuSang.setTextColor(ContextCompat.getColor(context, R.color.red));
        else if(ngayHoc.getImportanceSang() == 1)
            holder.tvGhiChuSang.setTextColor(ContextCompat.getColor(context, R.color.text_title_yellow));
        else
            holder.tvGhiChuSang.setTextColor(ContextCompat.getColor(context, R.color.text_color));

        if(ngayHoc.getImportanceChieu() == 2)
            holder.tvGhiChuChieu.setTextColor(ContextCompat.getColor(context, R.color.red));
        else if(ngayHoc.getImportanceChieu() == 1)
            holder.tvGhiChuChieu.setTextColor(ContextCompat.getColor(context, R.color.text_title_yellow));
        else
            holder.tvGhiChuChieu.setTextColor(ContextCompat.getColor(context, R.color.text_color));

        if(!ngayHoc.getGhiChuChieu().equals("") && !isHome){
            holder.tvGhiChuChieu.setVisibility(View.VISIBLE);
            String ghichu = context.getResources().getString(R.string.ghichu, ngayHoc.getGhiChuChieu());
            holder.tvGhiChuChieu.setText(ghichu);
        }else
            holder.tvGhiChuChieu.setVisibility(View.GONE);


        if(ngayHoc.isDayOff()){
            if(ngayHoc.getMonHocSang().size() != 0)
                holder.tvMonHocSang.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            if(ngayHoc.getMonHocChieu().size() != 0)
                holder.tvMonHocChieu.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.tvMonHocSang.setPaintFlags(holder.tvMonHocSang.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvMonHocChieu.setPaintFlags(holder.tvMonHocChieu.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.linearSang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAddData.onItemLongClick(ngayHoc, true);
            }
        });

        holder.linearChieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAddData.onItemLongClick(ngayHoc, false);
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ThoiKhoaBieuViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder.btnAdd == null || isHome)   return;
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
        void onItemLongClick(NgayHoc ngayHoc, boolean isMorning);
    }

    public static class ThoiKhoaBieuViewHolder extends RecyclerView.ViewHolder{
        TextView tvNgayHoc;
        TextView tvMonHocSang;
        TextView tvMonHocChieu;
        TextView tvGhiChuSang;
        TextView tvGhiChuChieu;
        LinearLayout linearSang;
        LinearLayout linearChieu;

        Button btnAdd;
        CardView cardTKB;

        public ThoiKhoaBieuViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNgayHoc = itemView.findViewById(R.id.tv_thoikhoabieu_ngayhoc);
            tvMonHocSang = itemView.findViewById(R.id.tv_thoikhoabieu_monhocsang);
            tvMonHocChieu = itemView.findViewById(R.id.tv_thoikhoabieu_monhocchieu);
            tvGhiChuSang = itemView.findViewById(R.id.tv_ghichu_sang);
            tvGhiChuChieu = itemView.findViewById(R.id.tv_ghichu_chieu);
            linearSang = itemView.findViewById(R.id.linear_sang);
            linearChieu = itemView.findViewById(R.id.linear_chieu);
            cardTKB = itemView.findViewById(R.id.cardTKB);

            btnAdd = itemView.findViewById(R.id.btn_item_add);
        }
    }
}
