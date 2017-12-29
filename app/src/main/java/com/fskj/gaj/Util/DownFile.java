package com.fskj.gaj.Util;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 功能说明:
 * 作    者:zhengwei
 * 创建日期:2017/6/16
 * 所属项目:MaoayiWorker
 */
public class DownFile {
    public interface DownFileListener {
        void success(String filepath);

        void errror(String errmsg);
    }


    private DownFileListener listener;
    private File updateFile = null;

    public DownFile(DownFileListener listener) {

        this.listener = listener;
    }


    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (listener != null) {
                switch (msg.what) {
                    case 1:
                        listener.success(msg.obj.toString());
                        break;
                    case 0:
                        // 下载失败
                        listener.errror(msg.obj.toString());
                        break;


                }
            }
        }
    };

    public  void down(final String fileurl, String type){
        Message message = updateHandler.obtainMessage();
        message.what=0;


        try{
            updateFile = YFileManager.getCacheFile(System.currentTimeMillis() + "."+type);
        }catch(Exception e){
            message.obj="创建文件失败";
            message.sendToTarget();
            return;
        }
        if(updateFile.exists()) {
            updateFile.delete();
        }

        new Thread(){
            @Override
            public void run() {
                Message message = updateHandler.obtainMessage();
                try {

                    HttpURLConnection httpConnection = null;
                    InputStream is = null;
                    FileOutputStream fos = null;

                    URL urls = new URL(fileurl);
                    httpConnection = (HttpURLConnection) urls.openConnection();
                    httpConnection.setRequestProperty("User-Agent",
                            "PacificHttpClient");
                    httpConnection.setConnectTimeout(10000);
                    httpConnection.setReadTimeout(20000);

                    if (httpConnection.getResponseCode() == 404) {
                        throw new Exception("fail!");
                    }
                    is = httpConnection.getInputStream();
                    long total=is.available();
                    fos = new FileOutputStream(updateFile, false);
                    byte buffer[] = new byte[4096];
                    int readsize = 0;
                    long sum = 0;
                    while ((readsize = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, readsize);
//                        sum+=readsize;
//                        int progress=(int)(sum*1.0f/total*100);
                        //下载中

                    }
                    httpConnection.disconnect();
                    fos.flush();
                    fos.close();
                    is.close();
                    message.what = 1;
                    message.obj=updateFile.getPath();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    message.obj = "文件下载失败";
                }
                updateHandler.sendMessage(message);
            }
        }.start();
    }
}
