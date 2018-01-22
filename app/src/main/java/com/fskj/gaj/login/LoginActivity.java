package com.fskj.gaj.login;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.MainActivity;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.Util.YFileManager;
import com.fskj.gaj.request.LoginRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.LoginResultVo;
import com.fskj.gaj.vo.PubnoticeVo;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private LoginCommitVo loginCommitVo;
    private LoginRequest loginRequest;
    private BusyView busyView;

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        fr.startActivity(intent);
        fr.getActivity().overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
    }

    private ImageView imgLogo;
    private EditText etName;
    private EditText etPwd;
    private LinearLayout llLogin;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=LoginActivity.this;
        //已登录直接跳主页
        if (LoginInfo.getLoginState(activity)) {
            MainActivity.gotoActivity(activity);
        }

        try{
            YFileManager.createDirectory();
        }catch (Exception e){

        }
        setContentView(R.layout.activity_login);
        StatusBarUtil.setFullSreen(activity);
        inflater = LayoutInflater.from(activity);


//界面初始化
        imgLogo=(ImageView)findViewById(R.id.img_logo);
        etName=(EditText)findViewById(R.id.et_name);
        etPwd=(EditText)findViewById(R.id.et_pwd);
        llLogin=(LinearLayout)findViewById(R.id.llLogin);

        //回显账号
        LoginCommitVo loginInfo = LoginInfo.getLoginCommitInfo(activity);
        if (loginInfo != null) {
            etName.setText(loginInfo.getUsername());
            etName.setSelection(loginInfo.getUsername().length());
        }
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
        loginCommitVo = new LoginCommitVo();
        loginRequest = new LoginRequest(activity, loginCommitVo, new ResultObjInterface<LoginResultVo>() {
            @Override
            public void success(ResultVO<LoginResultVo> data) {
                busyView.dismiss();
                LoginResultVo resultVo = data.getData();
                List<PubnoticeVo> pubnoticeVoList = resultVo.getPubnotice();
                int duty = resultVo.getDuty();
                int checker = resultVo.getChecker();
                //登录成功
                if (pubnoticeVoList != null && pubnoticeVoList.size() > 0) {
                    LoginInfo.savePubNoticeInfo(activity,pubnoticeVoList);
                }
                LoginInfo.saveIDuty(activity,duty);
                LoginInfo.saveIChecker(activity,checker);
                LoginInfo.saveLoginState(activity,true);
                LoginInfo.saveLoginCommitInfo(activity,loginCommitVo);
                LoginInfo.saveLoginResultVo(activity,resultVo);

//                Toast.makeText(activity,"登录成功",Toast.LENGTH_SHORT).show();
                MainActivity.gotoActivity(activity);
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
        //点击登录
        llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName = Tools.safeString(etName);
                if (strName.equals("")) {
                    Toast.makeText(activity,"请输入账号",Toast.LENGTH_SHORT).show();
                    return;
                }
                String strPwd = Tools.safeString(etPwd);
                if (strPwd.equals("")) {
                    Toast.makeText(activity,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                //封装实体
                loginCommitVo.setUsername(strName);
                loginCommitVo.setPassword(strPwd);
                busyView = BusyView.showText(activity,"登录中...");
                loginRequest.send();
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
