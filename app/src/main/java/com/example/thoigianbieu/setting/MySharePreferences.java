package com.example.thoigianbieu.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharePreferences {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public MySharePreferences(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void putBooleanValue(String key, boolean value){
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    public void putStringValue(String key, String value){
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key){
        return sharedPreferences.getString(key, "0");
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }
}
