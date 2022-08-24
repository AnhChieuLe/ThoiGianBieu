package com.example.thoigianbieu.widget.sukien;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.thoigianbieu.MainActivity;
import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.sukien.SuKien;
import com.example.thoigianbieu.database.sukien.SuKienDao;
import com.example.thoigianbieu.database.sukien.SuKienDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WidgetSuKienService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewProvider(this.getApplicationContext(), intent);
    }

    static class ListViewProvider implements RemoteViewsFactory{

        private List<SuKien> listItem;
        private Context context;
        private int appWidgetId;
        private SuKienDao dao;

        public ListViewProvider(Context context, Intent intent){
            this.context = context;
            listItem = new ArrayList<>();
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        private void addData(){
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.HOUR_OF_DAY, 0);
            calendar1.set(Calendar.MINUTE, 0);
            calendar1.set(Calendar.SECOND, 0);
            calendar1.set(Calendar.MILLISECOND, 0);

            Calendar calendar2 = (Calendar) calendar1.clone();
            calendar2.add(Calendar.DATE, 1);
            listItem = dao.getListSuKien(calendar1, calendar2);
        }

        @Override
        public void onCreate() {
            dao = SuKienDatabase.getInstance(context).suKienDao();
        }

        @Override
        public void onDataSetChanged() {
            addData();
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

            SuKien suKien = listItem.get(position);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_sukien_item);

            remoteViews.setTextViewText(R.id.tv_widget_sukien_tieude, suKien.getTieuDe());
            remoteViews.setTextViewText(R.id.tv_widget_sukien_thoigian, suKien.getStringThoiGian(context));
            remoteViews.setTextViewText(R.id.tv_widget_sukien_noidung, suKien.getNoiDung());

            return remoteViews;
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
