package com.example.thoigianbieu.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public  class TypeConvert{
    @TypeConverter
    public ArrayList<String> StringtoArray(String string){
        Gson gson = new Gson();
        Type typeList = new TypeToken<ArrayList<String>>(){}.getType();
        return gson.fromJson(string, typeList);
    }

    @TypeConverter
    public String ArraytoString(ArrayList<String> arr){
        return new Gson().toJson(arr);
    }

    @TypeConverter
    public Calendar LongtoCalendar(long l){
        if(l == 0)  return null;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(l);
        return cal;
    }

    @TypeConverter
    public long CalendarToLong(Calendar calendar){
        if(calendar == null)    return 0;
        return calendar.getTime().getTime();
    }
}
