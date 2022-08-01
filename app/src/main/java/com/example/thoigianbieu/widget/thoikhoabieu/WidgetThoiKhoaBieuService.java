package com.example.thoigianbieu.widget.thoikhoabieu;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.thoigianbieu.MainActivity;
import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.monhoc.MonHoc;
import com.example.thoigianbieu.database.monhoc.MonHocDAO;
import com.example.thoigianbieu.database.monhoc.MonHocDatabase;
import com.example.thoigianbieu.database.ngayhoc.NgayHoc;
import com.example.thoigianbieu.database.ngayhoc.NgayHocDao;
import com.example.thoigianbieu.database.ngayhoc.NgayHocDatabase;
import com.example.thoigianbieu.model.NgayHocManager;
import com.example.thoigianbieu.setting.SharePreferencesManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WidgetThoiKhoaBieuService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return (new ListProvider(this.getApplicationContext(), intent));
    }

    static class ListProvider implements RemoteViewsFactory{
        private final ArrayList<NgayHoc> listItem;
        private final Context context;
        private int appWidgetId;

        public ListProvider(Context context, Intent intent) {
            listItem = new ArrayList<>();
            this.context = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        private void addData(Context context){
            NgayHocManager manager = new NgayHocManager(context);
            listItem.clear();
            listItem.addAll(manager.getWeek());
        }

        @Override
        public void onCreate() {
            addData(context);
        }

        @Override
        public void onDataSetChanged() {
            addData(context);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return listItem.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            NgayHoc ngayHoc = listItem.get(position);

            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_thoikhoabieu_item);

            remoteView.setTextViewText(R.id.tv_widget_thoikhoabieu_ngayhoc, ngayHoc.getStringNgayHocInWeek(context));

            if(ngayHoc.isPast()){
                remoteView.setInt(R.id.tv_widget_thoikhoabieu_ngayhoc, "setBackgroundResource", R.drawable.background_title_past);
            }

            if(ngayHoc.isToDay()){
                remoteView.setInt(R.id.tv_widget_thoikhoabieu_ngayhoc, "setBackgroundResource", R.drawable.background_title_today);
            }
            if(ngayHoc.isSunDay()){
                remoteView.setInt(R.id.tv_widget_thoikhoabieu_ngayhoc, "setBackgroundResource", R.drawable.background_title_cn);
            }

            String trong = context.getResources().getString(R.string.trong);
            if(ngayHoc.getMonHocSang().size()!=0){
                remoteView.setTextViewText(R.id.tv_widget_thoikhoabieu_monhocsang, ngayHoc.getStringSang());
            }else {
                remoteView.setTextViewText(R.id.tv_widget_thoikhoabieu_monhocsang, trong);
            }
            if(ngayHoc.getMonHocChieu().size()!=0){
                remoteView.setTextViewText(R.id.tv_widget_thoikhoabieu_monhocchieu, ngayHoc.getStringChieu());
            }else {
                remoteView.setTextViewText(R.id.tv_widget_thoikhoabieu_monhocchieu, trong);
            }

            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction(ThoiKhoaBieuWidget.ACTION_UPDATE);
            remoteView.setOnClickFillInIntent(R.id.item_widget_thoikhoabieu, intent);

            return remoteView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}