package com.fskj.gaj.profile;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.AppConfig;
import com.fskj.gaj.FragmentRefresh;
import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.MainActivity;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.home.HomeFragment;
import com.fskj.gaj.login.LoginActivity;
import com.fskj.gaj.request.AttentionToZeroRequest;
import com.fskj.gaj.request.MyAttentionCountRequest;
import com.fskj.gaj.view.ExitLoginDialog;
import com.fskj.gaj.view.MessageConfirmDialog;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.LoginResultVo;
import com.fskj.gaj.vo.PubnoticeVo;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements FragmentRefresh{


    private TextView tvUserName;
    private TextView tvDepartment;
//    private LinearLayout llGuanzhu;
    private LoginCommitVo loginCommitVo;
    private MyAttentionCountRequest myAttentionCountRequest;
    private TextView tvCount;
    private AttentionToZeroRequest attentionToZeroRequest;
    private String strCount;

    public static ProfileFragment getInstance(String strCount ){
        ProfileFragment f =new ProfileFragment();
        Bundle bundle=new Bundle();
        if (strCount != null) {
            bundle.putString("strCount",strCount);
        }
        f.setArguments(bundle);

        return f;
    }

    private Activity activity;

    private Toolbar toolBar;
    private TextView tvExit;
    private LinearLayout llChangePwd;
    private LinearLayout llPub;
    private LinearLayout llCheckArticle;
    private LinearLayout llWeiHu;
//    private MyBroadcast myBroadcast = new MyBroadcast();
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle=getArguments();
        if(bundle!=null){
            strCount = bundle.getString("strCount");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity=this.getActivity();


//界面初始化
        View v=inflater.inflate(R.layout.fragment_profile, null);
        toolBar=(Toolbar)v.findViewById(R.id.toolBar);
        tvExit=(TextView)v.findViewById(R.id.tv_exit);
        tvCount =(TextView)v.findViewById(R.id.tv_count);
        tvUserName =(TextView)v.findViewById(R.id.tv_user_name);
        tvDepartment =(TextView)v.findViewById(R.id.tv_department);
        tvExit=(TextView)v.findViewById(R.id.tv_exit);
        llChangePwd=(LinearLayout)v.findViewById(R.id.llChangePwd);
        llPub=(LinearLayout)v.findViewById(R.id.llPub);
        llCheckArticle=(LinearLayout)v.findViewById(R.id.llCheckArticle);
        llWeiHu=(LinearLayout)v.findViewById(R.id.llWeiHu);
//        llGuanzhu =(LinearLayout)v.findViewById(R.id.llGuanzhu);

//        setNumber(strCount);

//        //注册广播
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("myBroadcast");
//        activity.registerReceiver(myBroadcast,filter);

//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
        createUI();
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT));
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        activity.unregisterReceiver(myBroadcast);
    }

    /**
     * 获取用户名和密码
     */
    private void createUI() {
        LoginResultVo loginResultVo = LoginInfo.getLoginResultVo(activity);
        if (loginResultVo != null) {
            tvUserName.setText(loginResultVo.getUsername());
            tvDepartment.setText(loginResultVo.getDepartment());
        }
    }


    //声明请求变量和返回结果
    private void initRequest(){
        LoginCommitVo loginInfo = LoginInfo.getLoginCommitInfo(activity);
        loginCommitVo = new LoginCommitVo();
        if (loginInfo != null) {
            loginCommitVo.setUsername(loginInfo.getUsername());
            loginCommitVo.setPassword(loginInfo.getPassword());
        }


        //数量清0

        attentionToZeroRequest = new AttentionToZeroRequest(activity, loginCommitVo, new ResultObjInterface<String>() {
            @Override
            public void success(ResultVO<String> data) {
            }

            @Override
            public void error(String errmsg) {
            }
        });
    }

    //初始化控件事件
    private void initWidgetEvent(){
        //退出登录
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExitLoginDialog.show(activity,"退出当前账号?", "取消", "确认", new ExitLoginDialog.OnConfirmClickListener() {
                    @Override
                    public void onLeft() {

                    }

                    @Override
                    public void onRight() {
                        LoginInfo.exitLogin(activity);
                        LoginActivity.gotoActivity(ProfileFragment.this);
                    }
                },true);

            }
        });
        //修改密码
        llChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePwdActivity.gotoActivity(ProfileFragment.this);
            }
        });
        //发布公示公告
        llPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<PubnoticeVo> pubnoticeVos = LoginInfo.getPubNoticeInfo(activity);
                if (pubnoticeVos == null || pubnoticeVos.size() == 0) {
                    Toast.makeText(activity,"权限不足",Toast.LENGTH_SHORT).show();
                }else {
                    PubNoticeActivity.gotoActivity(ProfileFragment.this);
                }
            }
        });
        //审核文章
        llCheckArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginInfo.getIChecker(activity) == 1) {
                    ArticleModuleActivity.gotoActivity(ProfileFragment.this);
                }else {
                    Toast.makeText(activity,"权限不足",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //值班人员维护
        llWeiHu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //// TODO: 2018/1/4 0004
                if (LoginInfo.getIduty(activity) == 1) {
                    DutyListActivity.gotoActivity(ProfileFragment.this);
                }else {
                    Toast.makeText(activity,"权限不足",Toast.LENGTH_SHORT).show();
                }
            }
        });
//        //我的关注
//        llGuanzhu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tvCount.setVisibility(View.INVISIBLE);
//                ((MainActivity)activity).hidePoint();
//                attentionToZeroRequest.send();
//                MyCareActivity.gotoActivity(ProfileFragment.this);
//            }
//        });
    }

    private void showMsg(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){

        }
    }


    private void setNumber(String count) {
        int iCount =0;
        try{
            iCount=Integer.parseInt(count);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (iCount>0) {
            tvCount.setVisibility(View.VISIBLE);
            if (iCount > 99) {
                count = "99+";
                tvCount.setText(count);
            }else {
                tvCount.setText(count);
            }

        }else {
            tvCount.setVisibility(View.INVISIBLE);
        }
    }

    private class MyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if ("myBroadcast".equals(action)) {
                    Bundle bundle = intent.getExtras();
                    String count = bundle.getString("count", "0");
                    Message msg = handler.obtainMessage();
                    msg.obj = count;
                    msg.sendToTarget();
                }
            }
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String count = (String) msg.obj;
            setNumber(count);

        }
    };


    @Override
    public void focusRefresh() {
        //do nothing
    }
}
