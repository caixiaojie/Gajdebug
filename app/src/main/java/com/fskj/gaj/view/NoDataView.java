package com.fskj.gaj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fskj.gaj.R;


/**
 * Created by Administrator on 2017/8/21 0021.
 */

public class NoDataView extends LinearLayout {


    private TextView textView;

    public void setText(String str) {
        if (textView != null && str != null){
            textView.setText(str);
        }
    }

    public NoDataView(Context context) {
        super(context);
        init(context);
    }

    public NoDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View v = LayoutInflater.from(context).inflate(R.layout.no_data_layout, null);
        textView = (TextView) v.findViewById(R.id.tv_msg);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.MATCH_PARENT));
        this.addView(v);
    }
}
