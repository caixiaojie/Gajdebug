package com.fskj.gaj.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.fskj.gaj.LoginInfo;

/**
 * author: Administrator
 * date: 2018/1/30 0030
 * desc:
 */

public class ShuiyinView extends View {
    Paint paint;
    String txt;

    public ShuiyinView(Context context) {
        super(context);
        initPaint(context);
    }

    public ShuiyinView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public ShuiyinView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);

    }

    private void initPaint(Context context) {
        paint = new Paint();
        paint.setColor(Color.parseColor("#0fff0000"));
        paint.setStrokeWidth(20);
        paint.setTextSize(150);

        txt= LoginInfo.getLoginUsername(context);
        if(txt==null||txt.equals("")){
            txt="重庆江北公安";
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(40, getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        for(int i=0;i<8;i++) {
            canvas.drawText(txt, 250, 110+250*i, paint);
        }
    }
}
