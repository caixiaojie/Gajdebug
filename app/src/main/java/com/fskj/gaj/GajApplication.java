package com.fskj.gaj;

import android.app.Application;
import android.content.Context;

import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class GajApplication extends Application {
    private static GajApplication instance;
    private static Context context;
    public int position = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this;
        //初始化内核
        QbSdk.initX5Environment(instance,null);
    }
    public static GajApplication getInstance() {
        return instance;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
