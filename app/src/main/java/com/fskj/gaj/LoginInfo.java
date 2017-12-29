package com.fskj.gaj;

import android.content.Context;
import android.content.SharedPreferences;

import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.LoginResultVo;
import com.fskj.gaj.vo.PubnoticeVo;
import com.fskj.gaj.vo.SignCommitVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class LoginInfo {
    private static final String SIGN_INFO = "LoginInfo";


    public static void savePubNoticeInfo(Context context, List<PubnoticeVo> voList){
        Gson gson=new Gson();
        String jsonPubNotice= gson.toJson(voList);
        SharedPreferences sh = Tools.getSharePreferences(context,SIGN_INFO);
        SharedPreferences.Editor editor = sh.edit();
        editor.putString("json_pubNotice",jsonPubNotice);
        editor.commit();
    }
    public static List<PubnoticeVo> getPubNoticeInfo(Context context) {
        Gson gson = new Gson();
        SharedPreferences sh = Tools.getSharePreferences(context,SIGN_INFO);
        String json = sh.getString("json_pubNotice", "");
        List<PubnoticeVo> historyVoList = gson.fromJson(json,new TypeToken<List<PubnoticeVo>>(){}.getType());
        return historyVoList;
    }


    public static void saveIDuty(Context context,int duty) {
        SharedPreferences sp = Tools.getSharePreferences(context, SIGN_INFO);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("duty",duty);
        edit.commit();
    }

    public static int getIduty(Context context) {
        SharedPreferences sp = Tools.getSharePreferences(context, SIGN_INFO);
        int duty = sp.getInt("duty", 0);
        return duty;
    }

    public static void saveLoginCommitInfo(Context context, LoginCommitVo vo) {
        if (vo == null) {
            return;
        }
        SharedPreferences sp = Tools.getSharePreferences(context, SIGN_INFO);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("username",vo.getUsername());
        edit.putString("password",vo.getPassword());
        edit.commit();
    }
    public static LoginCommitVo getLoginCommitInfo(Context context) {
        SharedPreferences sp = Tools.getSharePreferences(context, SIGN_INFO);
        String username = sp.getString("username", "");
        String password = sp.getString("password","");
        LoginCommitVo vo = new LoginCommitVo();
        vo.setUsername(username);
        vo.setPassword(password);
        return vo;
    }

    public static void clearLoginCommitInfo(Context context) {
        SharedPreferences.Editor edit = Tools.getSharePreferences(context, SIGN_INFO).edit();
        edit.remove("password");
        edit.commit();
    }

    /**
     * "pubnotice":1     1=可以发布通知通告      0=不可以
     * "duty":1          1=可以进入值班编辑功能  0=不可以
     */
    public static void saveLoginResultVo(Context context, LoginResultVo vo) {
        if (vo == null) {
            return;
        }
        SharedPreferences sp = Tools.getSharePreferences(context, SIGN_INFO);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("duty",vo.getDuty());
        edit.putString("username",vo.getUsername());
        edit.putString("department",vo.getDepartment());
        edit.commit();
    }

    public static LoginResultVo getLoginResultVo(Context context) {
        SharedPreferences sp = Tools.getSharePreferences(context, SIGN_INFO);
        int pubnotice = sp.getInt("pubnotice",0);
        int duty = sp.getInt("duty",0);
        String username = sp.getString("username", "");
        String department = sp.getString("department", "");
        LoginResultVo vo = new LoginResultVo();
        vo.setDuty(duty);
        vo.setUsername(username);
        vo.setDepartment(department);
        return vo;
    }

    /**
     * 保存登录状态
     * @param context
     * @param isLogin
     */
    public static void saveLoginState (Context context,boolean isLogin){
        SharedPreferences sp = Tools.getSharePreferences(context, SIGN_INFO);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isLogin",isLogin);
        edit.commit();
    }

    /**
     * 获取登录状态
     * @param context
     * @return
     */
    public synchronized static boolean getLoginState(Context context){
        SharedPreferences sp = Tools.getSharePreferences(context, SIGN_INFO);
        boolean isLogin = sp.getBoolean("isLogin", false);
        return isLogin;
    }

    /**
     * 退出登录
     * 改变登录状态
     * 清空登录相关信息
     * @param context
     */
    public static void exitLogin(Context context) {
        saveLoginState(context,false);
        clearLoginCommitInfo(context);
    }
}
