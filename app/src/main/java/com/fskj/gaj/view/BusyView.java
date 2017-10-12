package com.fskj.gaj.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.fskj.gaj.R;


/**
 * 功能说明:
 * 作    者:zhengwei
 * 创建日期:2016/8/10 13:20
 * 所属项目:fireeye_test
 */
public class BusyView extends Dialog {
    public BusyView(Context context) {
        super(context);
    }

    public BusyView(Context context, int theme) {
        super(context, theme);
    }


    public void onWindowFocusChanged(boolean hasFocus){
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        imageView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_busyview));

    }

    public void setMessage(CharSequence message) {
        if(message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView)findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    public static BusyView showQuery(Context context) {
        return show(context,"正在查询", false,null);
    }

    public static BusyView showCommit(Context context) {
        return show(context,"正在提交", false,null);
    }

    public static BusyView showText(Context context, String txt) {
        return show(context,txt, false,null);
    }
    public static BusyView show(Context context, CharSequence message, boolean cancelable,
                                OnCancelListener cancelListener) {
        BusyView dialog = new BusyView(context,R.style.ProgressHUD);
        dialog.setTitle("");
        dialog.setContentView(R.layout.busydialog);
        if(message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView)dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity= Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.2f;
        dialog.getWindow().setAttributes(lp);
        //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }
}
