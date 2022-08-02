package com.example.thoigianbieu;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.example.thoigianbieu.model.AddData;
import com.example.thoigianbieu.setting.SharePreferencesManager;
import com.example.thoigianbieu.widget.sukien.SukienWidget;
import com.example.thoigianbieu.widget.thoikhoabieu.ThoiKhoaBieuWidget;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class MyApplication extends Application {
    public static final String CHANNEL_SUKIEN = "CHANNEL SUKIEN";
    public static final String CHANNEL_LICHHOC = "CHANNEL LICHHOC";

    @Override
    public void onCreate() {
        super.onCreate();
        SharePreferencesManager.init(getApplicationContext());

        updateAllWidget(this);

        setDarkMode();

        checkLogin();

        createNotificationChannel();

        //addData();
    }

    private void checkLogin(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account == null){
            SharePreferencesManager.setLogin(false);
        }else{
            SharePreferencesManager.setLogin(true);
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence nameSK = getString(R.string.channel_sukien);
        String descriptionSK = getString(R.string.channel_sukien_description);
        int importanceSK = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channelSK = new NotificationChannel(CHANNEL_SUKIEN, nameSK, importanceSK);
        channelSK.setDescription(descriptionSK);

        CharSequence nameLH = getString(R.string.channel_lichhoc);
        String descriptionLH = getString(R.string.channel_lichhoc_description);
        int importanceLH = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channelLH = new NotificationChannel(CHANNEL_LICHHOC, nameLH, importanceLH);
        channelLH.setDescription(descriptionLH);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channelLH);
        notificationManager.createNotificationChannel(channelSK);
    }

    private void addData(){
        boolean installed = SharePreferencesManager.getFirstInstalled();
        if (!installed){
            AddData.AddMonHoc(getApplicationContext());
            SharePreferencesManager.putFirstInstalled(true);
        }
    }

    public static void updateAllWidget(Application application){
        updateSukienWidget(application);
        updateTKBWidget(application);
    }

    public static void updateTKBWidget(Application application){
        Intent intent = new Intent(application, ThoiKhoaBieuWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int[] ids;
        ids = AppWidgetManager.getInstance(application).getAppWidgetIds(new ComponentName(application, ThoiKhoaBieuWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        application.sendBroadcast(intent);
    }

    public static void updateSukienWidget(Application application){
        Intent intent = new Intent(application, SukienWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int[] ids;
        ids = AppWidgetManager.getInstance(application).getAppWidgetIds(new ComponentName(application, SukienWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        application.sendBroadcast(intent);
    }

    public void setDarkMode(){
        int darkMode = SharePreferencesManager.getInterface();

        if(darkMode == 0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        if(darkMode == 1){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        if(darkMode == 2){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}
