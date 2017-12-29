package com.fskj.gaj.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateTime {
	public static String getTime(long t) {
		if(t==0){
			return "";
		}else {
			Calendar c = Calendar.getInstance(Locale.CHINA);
			c.setTimeInMillis(t * 1000);

			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);

			return String.format("%d-%02d-%02d", year, month, day);
		}
	}
	
	public static String getFormatTime(long second){
		long _second=second/1000;
		long h=_second/3600;
		long m=(_second%3600)/60;
		long s=_second%60;
			String re= String.format("%02d:%02d:%02d", h,m,s);
		 return re;
	}
	
	
	public static String getDateTime() {
		Calendar c = Calendar.getInstance(Locale.CHINA);
	 	
		int year=c.get(Calendar.YEAR);
		int month=c.get(Calendar.MONTH)+1;
		int day=c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		 int second=c.get(Calendar.SECOND);
		return String.format("%d-%02d-%02d %02d:%02d:%02d",year,month,day, hour,min,second);
		 
	}

	public static String formatDate(long t){
		long now= System.currentTimeMillis();
		long dt=now-t;
		if(dt < 86400000){
			//当天
			if( dt <=60000){
				return "刚刚";
			}else if(dt >60000&&dt<3600000){
				long m1=dt/60000;
				return String.format("%d分钟前",m1);
			}else if(dt>=3600000&&dt<43200000){
				long hour=dt/3600000;
				return String.format("%d小时前",hour);
			}else {
				Calendar c = Calendar.getInstance(Locale.CHINA);
				c.setTimeInMillis(t);
				int month=c.get(Calendar.MONTH)+1;
				int day=c.get(Calendar.DAY_OF_MONTH);
	  			return String.format("%02d-%02d",month,day);
			}
		}else{
			Calendar c = Calendar.getInstance(Locale.CHINA);
			c.setTimeInMillis(t);
			int month=c.get(Calendar.MONTH)+1;
			int day=c.get(Calendar.DAY_OF_MONTH);
			return String.format("%02d-%02d",month,day);
		}
	}

	/**
	 * 获取当前时间戳
	 * @param t
	 * @return
     */
	public static String getCurrentTime(long t){
		return String.valueOf(t);
	}
	public static String getCurrentFormatTime () {
		Date date = new Date(System.currentTimeMillis());
		String strDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return strDate;
	}

	//保存时间
	public static void saveSendSuccessTime(Context context,String dateTime) {
		SharedPreferences sp = Tools.getSharePreferences(context, "DATE_TIME");
		SharedPreferences.Editor edit = sp.edit();
//		String dateTime = getDateTime();
		edit.putString("dateTime",dateTime);
		edit.commit();
	}
	//获取上次请求成功的时间
	public static String getSendSuccessTime(Context context) {
		SharedPreferences sp = Tools.getSharePreferences(context, "DATE_TIME");
		String dateTime = sp.getString("dateTime", "");
		return dateTime;
	}
}
