package com.fskj.gaj.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fskj.gaj.Util.TLog;
import com.tencent.smtt.sdk.TbsReaderView;


/**
 * Created by Administrator on 2017/11/21 0021.
 */

public class SuperFileView2 extends FrameLayout implements TbsReaderView.ReaderCallback {
    private static String TAG = "SuperFileView";
    private TbsReaderView mTbsReaderView;
    private int saveTime = -1;
    private Context context;

    public SuperFileView2(Context context) {
        this(context, null, 0);
    }

    public SuperFileView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperFileView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTbsReaderView = new TbsReaderView(context, this);
        this.addView(mTbsReaderView, new LinearLayout.LayoutParams(-1, -1));
        this.context = context;
    }


    private OnGetFilePathListener mOnGetFilePathListener;


    public void setOnGetFilePathListener(OnGetFilePathListener mOnGetFilePathListener) {
        this.mOnGetFilePathListener = mOnGetFilePathListener;
    }


    private TbsReaderView getTbsReaderView(Context context) {
        return new TbsReaderView(context, this);
    }

    public void displayFile(String temppath, String filepath) {

        if (filepath != null && !TextUtils.isEmpty(filepath )) {


            //加载文件
            Bundle localBundle = new Bundle();

            localBundle.putString("filePath", filepath);
            localBundle.putString("tempPath", temppath);

            if (this.mTbsReaderView == null)
                this.mTbsReaderView = getTbsReaderView(context);
            Log.e("=====",getFileType(filepath));
            try {
                boolean bool = this.mTbsReaderView.preOpen(getFileType(filepath), false);
                if (bool) {
                    this.mTbsReaderView.openFile(localBundle);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            TLog.e("文件路径无效！");
        }

    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            TLog.d(TAG, "paramString---->null");
            return str;
        }
        TLog.d(TAG, "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            TLog.d(TAG, "i <= -1");
            return str;
        }


        str = paramString.substring(i + 1);
        TLog.d(TAG, "paramString.substring(i + 1)------>" + str);
        return str;
    }

    public void show() {
        if(mOnGetFilePathListener!=null){
            mOnGetFilePathListener.onGetFilePath(this);
        }
    }

    /***
     * 将获取File路径的工作，“外包”出去
     */
    public interface OnGetFilePathListener {
        void onGetFilePath(SuperFileView2 mSuperFileView2);
    }


    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {
        TLog.e("****************************************************" + integer);
    }

    public void onStopDisplay() {
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }
}
