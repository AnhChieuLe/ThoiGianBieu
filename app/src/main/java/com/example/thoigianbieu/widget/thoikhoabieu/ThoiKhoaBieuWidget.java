package com.example.thoigianbieu.widget.thoikhoabieu;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.thoigianbieu.MainActivity;
import com.example.thoigianbieu.R;
import com.example.thoigianbieu.database.ngayhoc.NgayHoc;

import java.util.Calendar;

public class ThoiKhoaBieuWidget extends AppWidgetProvider {
    public static final String ACTION_UPDATE = "ACTION_UPDATE";

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        Intent intent = new Intent(context, WidgetThoiKhoaBieuService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_thoikhoabieu);
        view.setRemoteAdapter(R.id.stackViewWidget, intent);

        String time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE);
        view.setTextViewText(R.id.tv_widget_time,"Last update: " + time);

        appWidgetManager.updateAppWidget(appWidgetId, view);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stackViewWidget);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for(int appWidgetId:appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ACTION_UPDATE.equals(intent.getAction())){
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.stackViewWidget);
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
        }

        super.onReceive(context, intent);
    }
}