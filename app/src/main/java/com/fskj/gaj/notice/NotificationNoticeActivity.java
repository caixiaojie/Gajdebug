package com.fskj.gaj.notice;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;


import com.fskj.gaj.R;

public class NotificationNoticeActivity extends AppCompatActivity {

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,NotificationNoticeActivity.class);

        activity.startActivity(intent);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),NotificationNoticeActivity.class);

        fr.startActivity(intent);
    }

    private Toolbar toolBar;
    private ImageView imgBack;
    private RecyclerView recyclerView;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_notice);
        activity=NotificationNoticeActivity.this;

        inflater = LayoutInflater.from(activity);


//界面初始化
        toolBar=(Toolbar)findViewById(R.id.toolBar);
        imgBack=(ImageView)findViewById(R.id.img_back);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

        }
    }

    //声明请求变量和返回结果
    private void initRequest(){

    }

    //初始化控件事件
    private void initWidgetEvent(){

    }

    private void showMsg(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
