package com.fskj.gaj.profile;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.request.ChangePwdRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.vo.ChangePwdCommitVo;
import com.fskj.gaj.vo.LoginCommitVo;

public class ChangePwdActivity extends AppCompatActivity {

    private ChangePwdCommitVo changePwdCommitVo;
    private ChangePwdRequest changePwdRequest;

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,ChangePwdActivity.class);

        activity.startActivity(intent);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),ChangePwdActivity.class);

        fr.startActivity(intent);
        fr.getActivity().overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
    }

    private Toolbar toolBar;
    private ImageView imgBack;
    private EditText etOldPwd;
    private EditText etNewPwd;
    private EditText etConfirmPwd;
    private LinearLayout llCommit;
    private LayoutInflater inflater;
    private Activity activity;
    private BusyView busyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        activity=ChangePwdActivity.this;
        StatusBarUtil.setColor(activity,getResources().getColor(R.color.main_color),0);
        inflater = LayoutInflater.from(activity);


//界面初始化
        toolBar=(Toolbar)findViewById(R.id.toolBar);
        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        etOldPwd=(EditText)findViewById(R.id.et_old_pwd);
        etNewPwd=(EditText)findViewById(R.id.et_new_pwd);
        etConfirmPwd=(EditText)findViewById(R.id.et_confirm_pwd);
        llCommit=(LinearLayout)findViewById(R.id.llCommit);
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
        LoginCommitVo loginInfo = LoginInfo.getLoginCommitInfo(activity);

        changePwdCommitVo = new ChangePwdCommitVo();
        if (loginInfo != null){
            changePwdCommitVo.setUsername(loginInfo.getUsername());
        }
        changePwdRequest = new ChangePwdRequest(activity, changePwdCommitVo, new ResultObjInterface<String>() {
            @Override
            public void success(ResultVO<String> data) {
                busyView.dismiss();
                Toast.makeText(activity,"密码修改成功",Toast.LENGTH_SHORT).show();
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
        //点击提交
        llCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strOldPwd = Tools.safeString(etOldPwd);
                if (strOldPwd.equals("")) {
                    Toast.makeText(activity,"请输入旧密码以验证身份",Toast.LENGTH_SHORT).show();
                    return;
                }
                String strNewPwd = Tools.safeString(etNewPwd);
                if (strNewPwd.equals("")) {
                    Toast.makeText(activity,"请输入新密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                String strConfirmPwd = Tools.safeString(etConfirmPwd);
                if (strConfirmPwd.equals("")) {
                    Toast.makeText(activity,"请再次输入新密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                //两次密码不一致
                if (strConfirmPwd.equals(strNewPwd) == false) {
                    Toast.makeText(activity,"两次密码不一致,请重新输入",Toast.LENGTH_SHORT).show();
                    etConfirmPwd.setText("");
                    return;
                }
                //封装实体
                changePwdCommitVo.setOldpassword(strOldPwd);
                changePwdCommitVo.setNewpassword(strConfirmPwd);
                busyView = BusyView.showCommit(activity);
                //发送请求
                changePwdRequest.send();
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
