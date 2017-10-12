package com.fskj.gaj.system;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;


import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.request.AddRuleRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.vo.AddRuleCommitVo;

public class RoomApplyActivity extends AppCompatActivity {

    private AddRuleCommitVo addRuleCommitVo;
    private AddRuleRequest addRuleRequest;
    private BusyView busyView;

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,RoomApplyActivity.class);

        activity.startActivity(intent);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),RoomApplyActivity.class);

        fr.startActivity(intent);
    }

    private Toolbar toolBar;
    private ImageView imgBack;
    private TextView tvCommit;
    private TextView tvRoomName;
    private TextView tvMeetingDate;
    private TextView tvMeetingTime;
    private EditText etMeetingName;
    private EditText etUserName;
    private EditText etUserPhone;
    private EditText etUserMember;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_apply);
        activity=RoomApplyActivity.this;

        inflater = LayoutInflater.from(activity);


//界面初始化
        toolBar=(Toolbar)findViewById(R.id.toolBar);
        imgBack=(ImageView)findViewById(R.id.img_back);
        tvCommit=(TextView)findViewById(R.id.tv_commit);
        tvRoomName=(TextView)findViewById(R.id.tv_room_name);
        tvMeetingDate=(TextView)findViewById(R.id.tv_meeting_date);
        tvMeetingTime=(TextView)findViewById(R.id.tv_meeting_time);
        etMeetingName=(EditText)findViewById(R.id.et_meeting_name);
        etUserName=(EditText)findViewById(R.id.et_user_name);
        etUserPhone=(EditText)findViewById(R.id.et_user_phone);
        etUserMember=(EditText)findViewById(R.id.et_user_member);
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
        addRuleCommitVo = new AddRuleCommitVo();
        addRuleRequest = new AddRuleRequest(activity, addRuleCommitVo, new ResultObjInterface<String>() {
            @Override
            public void success(ResultVO<String> data) {
                busyView.dismiss();
            }

            @Override
            public void error(String errmsg) {
                busyView.dismiss();
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化控件事件
    private void initWidgetEvent(){
        //点击提交
        tvCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.closeInputWindow(activity);
                String strMeetingName = Tools.safeString(etMeetingName);
                if (strMeetingName.equals("")) {
                    Toast.makeText(activity,"请输入会议名称",Toast.LENGTH_SHORT).show();
                    return;
                }
                String strUserName = Tools.safeString(etUserName);
                if (strUserName.equals("")) {
                    Toast.makeText(activity,"请输入姓名",Toast.LENGTH_SHORT).show();
                    return;
                }
                String strUserPhone = Tools.safeString(etUserPhone);
                if (strUserPhone.equals("")) {
                    Toast.makeText(activity,"请输入电话号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                //封装实体
                addRuleCommitVo.setRname(strMeetingName);//会议室名称
                addRuleCommitVo.setName(strUserName);//申请人姓名
                addRuleCommitVo.setPhone(strUserPhone);//申请人电话

                busyView = BusyView.showCommit(activity);
                addRuleRequest.send();
            }
        });
    }

    private void showMsg(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
