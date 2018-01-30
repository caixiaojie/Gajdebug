package com.fskj.gaj.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fskj.gaj.BuildConfig;
import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.Remote.HttpUtils;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.Util.DateTime;
import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.MsgSearchResultVo;
import com.fskj.gaj.vo.NewsToneCommitVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class MyService extends Service {
    private final static String TAG = "wzj";
    private Thread thread;
    private Context context;
    private Gson gson;
//    private PowerManager.WakeLock wakeLock;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        gson=new Gson();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        if(thread==null||thread.isAlive()==false ||thread.isInterrupted()){
            if(thread!=null){
                try{
                    thread.interrupt();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            thread=new Thread(){
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            if(Tools.checkConnection(context)==false){
                                Thread.sleep(60000);
//                                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//                                wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"com.fskj.gaj.service.MyService");
//                                wakeLock.acquire();
//                                Log.i(TAG,"网络连接失败");
                                continue;
                            }

                            if (LoginInfo.getLoginState(context)) {//登录的情况下才去请求

                                LoginCommitVo loginCommitVo = new LoginCommitVo();
                                NewsToneCommitVo newsToneCommitVo = new NewsToneCommitVo();
                                LoginCommitVo loginInfo = LoginInfo.getLoginCommitInfo(context);

                                    loginCommitVo.setUsername(loginInfo.getUsername());
                                    loginCommitVo.setPassword(loginInfo.getPassword());

                                    newsToneCommitVo.setUsername(loginInfo.getUsername());
                                    newsToneCommitVo.setPassword(loginInfo.getPassword());
//                                    newsToneCommitVo.setDatetime("");
                                    newsToneCommitVo.setDatetime(DateTime.getSendSuccessTime(context));
//                                    if (null == DateTime.getSendSuccessTime(context) || "".equals(DateTime.getSendSuccessTime(context))) {
//                                        newsToneCommitVo.setDatetime("");
//                                    }else {
//                                        newsToneCommitVo.setDatetime(DateTime.getSendSuccessTime(context));
//                                    }


                                try {
                                    String url = BuildConfig.SERVER_IP + "/mobile/myattentioncount.do";
                                    String jsonStr = HttpUtils.post(url, loginCommitVo.toMap());
//                                    Log.e("myattentioncount", jsonStr);
//                                    Log.e("myattentioncount", loginCommitVo.toMap().toString());
                                    ResultVO<String> vo = gson.fromJson(jsonStr, new TypeToken<ResultVO<String>>() {
                                    }.getType());
                                    Message msg = mhander.obtainMessage();
                                    msg.what = 1;
                                    msg.obj = vo.getData();
                                    mhander.sendMessage(msg);


                                    url = BuildConfig.SERVER_IP + "/mobile/newestone.do";
                                    jsonStr = HttpUtils.post(url, newsToneCommitVo.toMap());
//                                    Log.e("NewsToneRequest", jsonStr);
//                                    Log.e("NewsToneRequest", newsToneCommitVo.toMap().toString());
                                    ResultVO<MsgSearchResultVo> vo1 = gson.fromJson(jsonStr, new TypeToken<ResultVO<MsgSearchResultVo>>() {
                                    }.getType());
                                    if (vo1.success()) {//请求成功保存时间
                                        DateTime.saveSendSuccessTime(context, DateTime.getDateTime());
                                    }
                                    if (vo1.getData() != null) {
                                        Message msg1 = mhander.obtainMessage();
                                        msg1.what = 2;
                                        msg1.obj = vo1.getData();
                                        mhander.sendMessage(msg1);
                                    }
                                }catch (Exception e){

                                }
                            }
                            Thread.sleep(60000);
                        }
                    } catch (InterruptedException e) {
                        System.out.println(this.getName()+"因InterruptedException停止");
                    }
                }
            };
            thread.start();

        }
        return START_STICKY_COMPATIBILITY;
    }

    private Handler mhander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String count = (String) msg.obj;
                    Intent intent = new Intent();
                    intent.setAction("myBroadcast");
                    intent.putExtra("count",count);
                    context.sendBroadcast(intent);
                    break;
                case 2:
                    MsgSearchResultVo  vo = ( MsgSearchResultVo ) msg.obj;
                    Tools.showNewsNotify(getApplicationContext(),vo.getMid(),vo.getTitle(),vo.getReason(),vo.getType());
                    break;
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    // IBinder是远程对象的基本接口，是为高性能而设计的轻量级远程调用机制的核心部分。但它不仅用于远程
    // 调用，也用于进程内调用。这个接口定义了与远程对象交互的协议。
    // 不要直接实现这个接口，而应该从Binder派生。
    // Binder类已实现了IBinder接口
    public class MyBinder extends Binder {
        /** * 获取Service的方法 * @return 返回PlayerService */
        public MyService getService(){
            return MyService.this;
        }
    }


}
