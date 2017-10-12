package com.fskj.gaj.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class TitleTypeSP {
    public static void saveENTitleStr (Context context, List<String> titleList) {
        Gson gson = new Gson();
        String toJson = gson.toJson(titleList);
        SharedPreferences sp = Tools.getSharePreferences(context, "TITLE_EN");
        sp.edit().putString("str_title",toJson).commit();
    }
    public static List<String> getENTitleStr (Context context) {
        Gson gson = new Gson();
        SharedPreferences sp = Tools.getSharePreferences(context, "TITLE_EN");
        String json = sp.getString("str_title", "");
        List<String> titleList = gson.fromJson(json,new TypeToken<List<String>>(){}.getType());
        return titleList;
    }
    public static void saveZHTitleStr (Context context, List<String> titleList) {
        Gson gson = new Gson();
        String toJson = gson.toJson(titleList);
        SharedPreferences sp = Tools.getSharePreferences(context, "TITLE_ZH");
        sp.edit().putString("str_title",toJson).commit();
    }
    public static List<String> getZHTitleStr (Context context) {
        Gson gson = new Gson();
        SharedPreferences sp = Tools.getSharePreferences(context, "TITLE_ZH");
        String json = sp.getString("str_title", "");
        List<String> titleList = gson.fromJson(json,new TypeToken<List<String>>(){}.getType());
        return titleList;
    }
}
