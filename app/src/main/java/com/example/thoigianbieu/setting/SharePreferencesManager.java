package com.example.thoigianbieu.setting;

import android.app.Application;
import android.content.Context;

public class SharePreferencesManager {

    public static final String PREF_FIRST_INSTALLED    = "PREF_FIRST_INSTALLED";
    public static final String PREF_LOGIN              = "PREF_LOGIN";
    public static final String PREF_MAIL               = "PREF_MAIL";

    public static final String PREF_TKB_COUNT          = "PREF_TKB_COUNT";
    public static final String PREF_TKB_HOME           = "PREF_TKB_HOME";
    public static final String PREF_TKB_INCLUDE_MH     = "PREF_TKB_INCLUDE_MH";
    public static final String PREF_TKB_INCLUDE_NULL   = "PREF_TKB_INCLUDE_NULL";
    public static final String PREF_TKB_INCLUDE_PAST   = "PREF_TKB_INCLUDE_PAST";

    public static final String PREF_SK_STYLE_HOME      = "PREF_SK_STYLE_HOME";
    public static final String PREF_SK_STYLE           = "PREF_SK_STYLE";
    public static final String PREF_SK_COLUMN_COUNT    = "PREF_SK_COLUMN_COUNT";
    public static final String PREF_SK_HOME_COUNT      = "PREF_SK_HOME_COUNT";

    public static final String PREF_INTERFACE          = "PREF_INTERFACE";
    public static final String PREF_COLOR              = "PREF_COLOR";

    private static SharePreferencesManager instance;
    private MySharePreferences mySharePreferences;

    public static void init(Context context){
        instance = new SharePreferencesManager();
        instance.mySharePreferences = new MySharePreferences(context);
    }

    public static SharePreferencesManager getInstance(){
        if(instance == null){
            instance = new SharePreferencesManager();
        }
        return instance;
    }

    public static void putFirstInstalled(boolean value){
        SharePreferencesManager.getInstance().mySharePreferences.putBooleanValue(PREF_FIRST_INSTALLED, value);
    }

    public static boolean getFirstInstalled(){
        return SharePreferencesManager.getInstance().mySharePreferences.getBooleanValue(PREF_FIRST_INSTALLED);
    }

    public static void setLogin(boolean value){
        SharePreferencesManager.getInstance().mySharePreferences.putBooleanValue(PREF_LOGIN, value);
    }

    public static boolean getLogin(){
        return SharePreferencesManager.getInstance().mySharePreferences.getBooleanValue(PREF_LOGIN);
    }

    public static void putEmail(String value){
        SharePreferencesManager.getInstance().mySharePreferences.putStringValue(PREF_MAIL, value);
    }

    public static String getEmail(){
        return SharePreferencesManager.getInstance().mySharePreferences.getStringValue(PREF_MAIL);
    }

    public static int getTKBCount(){
        String str = SharePreferencesManager.getInstance().mySharePreferences.getStringValue(PREF_TKB_COUNT);
        return Integer.parseInt(str);
    }

    public static int getTKBHome(){
        String str = SharePreferencesManager.getInstance().mySharePreferences.getStringValue(PREF_TKB_HOME);
        return Integer.parseInt(str);
    }

    public static int getInterface(){
        String str = SharePreferencesManager.getInstance().mySharePreferences.getStringValue(PREF_INTERFACE);
        return Integer.parseInt(str);
    }

    public static int getColor(){
        String color = SharePreferencesManager.getInstance().mySharePreferences.getStringValue(PREF_COLOR);
        return Integer.parseInt(color);
    }

    public static int getStyleSuKien(){
        String str = SharePreferencesManager.getInstance().mySharePreferences.getStringValue(PREF_SK_STYLE);
        return Integer.parseInt(str);
    }

    public static int getStyleSuKienHome(){
        String str =  SharePreferencesManager.getInstance().mySharePreferences.getStringValue(PREF_SK_STYLE_HOME);
        return Integer.parseInt(str);
    }

    public static int getSoCotSuKien(){
        String str = SharePreferencesManager.getInstance().mySharePreferences.getStringValue(PREF_SK_COLUMN_COUNT);
        return Integer.parseInt(str);
    }
    public static boolean getTKBInclude(){
        return SharePreferencesManager.getInstance().mySharePreferences.getBooleanValue(PREF_TKB_INCLUDE_MH);
    }

    public static int getSKHomeCount(){
        String str = SharePreferencesManager.getInstance().mySharePreferences.getStringValue(PREF_SK_HOME_COUNT);
        return Integer.parseInt(str);
    }

    public static boolean getTKBIncludeNull(){
        return SharePreferencesManager.getInstance().mySharePreferences.getBooleanValue(PREF_TKB_INCLUDE_NULL);
    }

    public static boolean getTKBIncludePast(){
        return SharePreferencesManager.getInstance().mySharePreferences.getBooleanValue(PREF_TKB_INCLUDE_PAST);
    }
}
