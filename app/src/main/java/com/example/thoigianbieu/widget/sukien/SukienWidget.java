package com.example.thoigianbieu.widget.sukien;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.thoigianbieu.R;

public class SukienWidget extends AppWidgetProvider {

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_sukien);

        Intent intent = new Intent(context, WidgetSuKienService.class);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        view.setRemoteAdapter(appWidgetId, R.id.stackViewSuKien, intent);
        view.setEmptyView(R.id.stackViewSuKien, R.id.sukien_empty);

        appWidgetManager.updateAppWidget(appWidgetId, view);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stackViewSuKien);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for(int appWidgetId:appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}
