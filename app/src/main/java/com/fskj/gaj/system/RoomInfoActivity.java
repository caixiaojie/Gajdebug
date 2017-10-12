package com.fskj.gaj.system;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.fskj.gaj.R;

public class RoomInfoActivity extends AppCompatActivity {

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,RoomInfoActivity.class);

        activity.startActivity(intent);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),RoomInfoActivity.class);

        fr.startActivity(intent);
    }

    private Toolbar toolBar;
    private ImageView imgBack;
    private TextView tvRoomName;
    private TextView tvMeetingDate;
    private TextView tvMeetingTime;
    private TextView tvApplyStatus;
    private TextView tvMeetingName;
    private TextView tvUserName;
    private TextView tvUserPhone;
    private TextView tvUserMember;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_info);
        activity=RoomInfoActivity.this;

        inflater = LayoutInflater.from(activity);


//界面初始化
        toolBar=(Toolbar)findViewById(R.id.toolBar);
        imgBack=(ImageView)findViewById(R.id.img_back);
        tvRoomName=(TextView)findViewById(R.id.tv_room_name);
        tvMeetingDate=(TextView)findViewById(R.id.tv_meeting_date);
        tvMeetingTime=(TextView)findViewById(R.id.tv_meeting_time);
        tvApplyStatus=(TextView)findViewById(R.id.tv_apply_status);
        tvMeetingName=(TextView)findViewById(R.id.tv_meeting_name);
        tvUserName=(TextView)findViewById(R.id.tv_user_name);
        tvUserPhone=(TextView)findViewById(R.id.tv_user_phone);
        tvUserMember=(TextView)findViewById(R.id.tv_user_member);
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
