package com.example.thoigianbieu.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.sukien.SuKien;
import com.example.thoigianbieu.setting.SharePreferencesManager;

import java.util.List;

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
        void clickItem(SuKien suKien);
        void clickAdd();
        void hideButton();
        void showButton();
    }

    @Override
    public int getItemViewType(int position) {
        return position == listSukien.size()?R.layout.item_add :R.layout.item_sukien;
    }

    @NonNull
    @Override
    public SuKienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == R.layout.item_add){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sukien, parent, false);
        }
        return new SuKienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuKienViewHolder holder, int position) {
        if(position == listSukien.size()){
            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.clickAdd();
                }
            });
            holder.btnAdd.setText(R.string.themsukien);
//            if(isHome){
//                holder.cardAdd.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
//            }
        }else {
            SuKien suKien = listSukien.get(position);
            holder.cardSuKien.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.clickItem(suKien);
                }
            });

            holder.tvTieuDe.setText(suKien.getTieuDe());
            holder.tvNoiDung.setText(suKien.getNoiDung());
            holder.tvThoiGian.setText(suKien.getStringThoiGian());
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull SuKienViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(holder.cardAdd == null)  return;
        if(!isHome) return;

        itemClick.hideButton();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SuKienViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder.cardAdd == null)  return;
        if(!isHome) return;

        itemClick.showButton();
    }

    @Override
    public int getItemCount() {
        return getCount()+1;
    }

    private int getCount(){
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
        Button btnAdd;
        CardView cardAdd;

        public SuKienViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTieuDe = itemView.findViewById(R.id.tv_sukien_tieude);
            tvNoiDung = itemView.findViewById(R.id.tv_sukien_noidung);
            tvThoiGian = itemView.findViewById(R.id.tv_sukien_thoigian);
            cardSuKien = itemView.findViewById(R.id.card_sukien);

            btnAdd = itemView.findViewById(R.id.btn_item_add);
            cardAdd = itemView.findViewById(R.id.card_add);
        }
    }
}
