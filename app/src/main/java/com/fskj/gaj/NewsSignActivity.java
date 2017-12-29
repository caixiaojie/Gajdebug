package com.fskj.gaj;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.request.SignRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.SignCommitVo;


public class NewsSignActivity extends AppCompatActivity {


    private String id;
    private LoginCommitVo loginCommitVo;

    public static void gotoActivity(Activity activity , String id){
        Intent intent=new Intent(activity,NewsSignActivity.class);
        intent.putExtra("id",id);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),NewsSignActivity.class);

        fr.startActivity(intent);

    }

    private SignCommitVo signCommitVo;
    private SignRequest signRequest;
    private BusyView busyView;
    private TextView tvRecord;
    private Toolbar toolBar;
    private ImageView imgBack;
    private EditText etName;
    private EditText etPwd;
    private LinearLayout llCommit;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_sign);
        activity=NewsSignActivity.this;
        StatusBarUtil.setColor(activity,getResources().getColor(R.color.main_color),0);
        inflater = LayoutInflater.from(activity);


//界面初始化
        toolBar=(Toolbar)findViewById(R.id.toolBar);
        imgBack=(ImageView)findViewById(R.id.img_back);
        etName=(EditText)findViewById(R.id.et_name);
        etPwd=(EditText)findViewById(R.id.et_pwd);
        tvRecord = (TextView) findViewById(R.id.tv_record);
        llCommit=(LinearLayout)findViewById(R.id.llCommit);

        initUser();
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){
            id = bundle.getString("id", "");
            signCommitVo.setMid(id);
        }

    }

    /**
     * 初始化用户名密码（在登录的情况下）
     */
    private void initUser() {
        LoginCommitVo loginInfo = LoginInfo.getLoginCommitInfo(activity);
        if (loginInfo != null) {
            etName.setText(loginInfo.getUsername());
            etPwd.setText(loginInfo.getPassword());
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
        signCommitVo = new SignCommitVo();
        signRequest = new SignRequest(activity, signCommitVo, new ResultObjInterface<String>() {
            @Override
            public void success(ResultVO<String> data) {
                busyView.dismiss();
                //签收成功
                //保存用户名和密码
//                LoginInfo.saveLoginCommitInfo(activity,loginCommitVo);
//                LoginInfo.saveLoginState(activity,true);//保存登录成功的标示

                Toast.makeText(activity,"签收成功",Toast.LENGTH_SHORT).show();

                finish();
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
        //返回
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //点击签收
        llCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName = Tools.safeString(etName);
                if (strName.equals("")) {
                    Toast.makeText(activity,"请输入姓名",Toast.LENGTH_SHORT).show();
                    return;
                }
                String strPwd = Tools.safeString(etPwd);
                if (strPwd.equals("")) {
                    Toast.makeText(activity,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                loginCommitVo = new LoginCommitVo();
                loginCommitVo.setUsername(strName);
                loginCommitVo.setPassword(strPwd);
                //封装请求体
                signCommitVo.setUsername(strName);
                signCommitVo.setPassword(strPwd);
                busyView = BusyView.showText(activity,"正在签收");
                signRequest.send();
            }
        });

        //签收记录
        tvRecord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SignRecordActivity.gotoActivity(activity,id);
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

    @Override
    public void finish() {
        super.finish();
        activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
    }

}
