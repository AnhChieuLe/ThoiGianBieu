package com.example.thoigianbieu.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.sukien.SuKien;
import com.example.thoigianbieu.setting.SharePreferencesManager;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class SuKienAdaptter extends RecyclerView.Adapter<SuKienAdaptter.SuKienViewHolder> {

    List<SuKien> listSukien;
    ItemClick itemClick;
    boolean isHome;

    public SuKienAdaptter(ItemClick itemClick, boolean isHome) {
        this.itemClick = itemClick;
        this.isHome = isHome;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<SuKien> listSukien){
        this.listSukien = listSukien;
        notifyDataSetChanged();
    }

    public interface ItemClick{
        public void click(SuKien suKien);
    }

    @NonNull
    @Override
    public SuKienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sukien, parent, false);
        return new SuKienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuKienViewHolder holder, int position) {
        SuKien suKien = listSukien.get(position);
        holder.cardSuKien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(suKien);
            }
        });

        holder.tvTieuDe.setText(suKien.getTieuDe());
        holder.tvNoiDung.setText(suKien.getNoiDung());
        holder.tvThoiGian.setText(suKien.getStringThoiGian());
    }

    @Override
    public int getItemCount() {
        if(listSukien == null){
            return 0;
        }
        if(isHome){
            int count = SharePreferencesManager.getSKHomeCount();
            if(count == 0){
                return listSukien.size();
            }
            return Math.min(listSukien.size(), count);
        }
        return listSukien.size();
    }

    public static class SuKienViewHolder extends RecyclerView.ViewHolder{

        TextView tvTieuDe, tvNoiDung, tvThoiGian;
        CardView cardSuKien;

        public SuKienViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTieuDe = itemView.findViewById(R.id.tv_sukien_tieude);
            tvNoiDung = itemView.findViewById(R.id.tv_sukien_noidung);
            tvThoiGian = itemView.findViewById(R.id.tv_sukien_thoigian);
            cardSuKien = itemView.findViewById(R.id.card_sukien);
        }
    }
}
