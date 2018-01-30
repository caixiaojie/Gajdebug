package com.fskj.gaj;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.Util.DownFile;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.attention.AttentionFragment;
import com.fskj.gaj.duty.DutyFragment;
import com.fskj.gaj.home.HomeFragment;
import com.fskj.gaj.login.LoginActivity;
import com.fskj.gaj.notice.NoticeFragment;
import com.fskj.gaj.profile.ProfileFragment;
import com.fskj.gaj.receiver.MyBroadcastReceiver;
import com.fskj.gaj.request.AttentionToZeroRequest;
import com.fskj.gaj.request.GetAppVersionCode;
import com.fskj.gaj.service.MyService;
import com.fskj.gaj.system.RoomFragment;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.view.ExitLoginDialog;
import com.fskj.gaj.view.MessageConfirmDialog;
import com.fskj.gaj.vo.GetAppVersionCommitVo;
import com.fskj.gaj.vo.LoginCommitVo;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends FragmentActivity {


//    private TextView tvCircle;
    private String strCount;
    private GetAppVersionCommitVo getAppVersionCommitVo;
    private GetAppVersionCode getAppVersionCode;
    private ProgressDialog pd;
    private LinearLayout llMenuAttention;
    private ImageView imgAttention;
    private TextView tvAttention;
    private TextView tvCount;
    private LoginCommitVo loginCommitVo;
    private AttentionToZeroRequest attentionToZeroRequest;
    private Intent intentService;

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        fr.startActivity(intent);
    }

    private RelativeLayout realLayout;
    private LinearLayout llMenuHome;
    private ImageView imgHome;
    private TextView tvHomePage;
//    private LinearLayout llMenuNotice;
//    private ImageView imgNotice;
//    private TextView tvNotice;
    private LinearLayout llMenuDuty;
    private ImageView imgDuty;
    private TextView tvDuty;
    private LinearLayout llMenuRoom;
    private ImageView imgRoom;
    private TextView tvRoom;
    private LayoutInflater inflater;
    private Activity activity;
    private int lastpos;
    private boolean isExit = false;
    private int selectpos;//记录选中的项，用于登录后的返回
    private FragmentManager fm;

    HomeFragment homeFragment ;
    AttentionFragment attentionFragment;
    DutyFragment dutyFragment ;
    ProfileFragment profileFragment ;
    FragmentRefresh fragmentRefresh;

    private BusyView busyView;

    private MyBroadcast myBroadcast = new MyBroadcast();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=MainActivity.this;
        StatusBarUtil.setColor(activity,getResources().getColor(R.color.main_color),0);
        inflater = LayoutInflater.from(activity);


//界面初始化
        realLayout=(RelativeLayout)findViewById(R.id.real_layout);
        llMenuHome=(LinearLayout)findViewById(R.id.llMenuHome);
        imgHome=(ImageView)findViewById(R.id.img_home);
        tvHomePage=(TextView)findViewById(R.id.tv_home_page);
//        llMenuNotice=(LinearLayout)findViewById(R.id.llMenuNotice);
//        imgNotice=(ImageView)findViewById(R.id.img_notice);
//        tvNotice=(TextView)findViewById(R.id.tv_notice);
        llMenuAttention =(LinearLayout)findViewById(R.id.llMenuAttention);
        imgAttention =(ImageView)findViewById(R.id.img_attention);
        tvAttention =(TextView)findViewById(R.id.tv_attention);
        llMenuDuty=(LinearLayout)findViewById(R.id.llMenuDuty);
        imgDuty=(ImageView)findViewById(R.id.img_duty);
        tvDuty=(TextView)findViewById(R.id.tv_duty);
        llMenuRoom=(LinearLayout)findViewById(R.id.llMenuRoom);
        imgRoom=(ImageView)findViewById(R.id.img_room);
        tvRoom=(TextView)findViewById(R.id.tv_room);
//        tvCircle =(TextView)findViewById(R.id.tv_circle);
        tvCount =(TextView)findViewById(R.id.tv_count);
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){

        }
        fm = getSupportFragmentManager();
        lastpos = -1;
        setSelected(0);

        //绑定服务
        intentService = new Intent(activity, MyService.class);
        startService(intentService);

        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("myBroadcast");
        registerReceiver(myBroadcast,filter);

        //检查版本号
        getAppVersionCode.send();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcast);
    }

    public void setSelected(int p) {
        if (p == lastpos) {
            return;
        }
        selectpos = p;
            FragmentTransaction transaction = fm.beginTransaction();
            switch (p) {
                case 0:
                    tvHomePage.setTextColor(getResources().getColor(R.color.main_color));
                    tvAttention.setTextColor(getResources().getColor(R.color.txt_menu_gray));
                    tvDuty.setTextColor(getResources().getColor(R.color.txt_menu_gray));
                    tvRoom.setTextColor(getResources().getColor(R.color.txt_menu_gray));

                    imgHome.setImageResource(R.mipmap.img_menu_home_on);
                    imgAttention.setImageResource(R.mipmap.img_attetntion_off);
                    imgDuty.setImageResource(R.mipmap.img_menu_duty_off);
                    imgRoom.setImageResource(R.mipmap.img_profile_off);
                     lastpos = p;

                    if (homeFragment == null) {
                        homeFragment = HomeFragment.getInstance();
                        transaction.add(R.id.real_layout,homeFragment);
                    }
                    //隐藏所有fragment
                    hideFragment(transaction);
                    //显示需要显示的fragment
                    transaction.show(homeFragment);
                    transaction.commit();
                    fragmentRefresh=homeFragment;
                    break;

                case 1:

                    tvHomePage.setTextColor(getResources().getColor(R.color.txt_menu_gray));
//                    tvNotice.setTextColor(getResources().getColor(R.color.main_color));
                    tvAttention.setTextColor(getResources().getColor(R.color.main_color));
                    tvDuty.setTextColor(getResources().getColor(R.color.txt_menu_gray));
                    tvRoom.setTextColor(getResources().getColor(R.color.txt_menu_gray));

                    imgHome.setImageResource(R.mipmap.img_menu_home_off);
                    imgAttention.setImageResource(R.mipmap.img_attention_on);
                    imgDuty.setImageResource(R.mipmap.img_menu_duty_off);
                    imgRoom.setImageResource(R.mipmap.img_profile_off);

                    if (attentionFragment == null) {
                        attentionFragment = AttentionFragment.getInstance();
                        transaction.add(R.id.real_layout,attentionFragment);
                    }


                    //隐藏所有fragment
                    hideFragment(transaction);
                    //显示需要显示的fragment
                    transaction.show(attentionFragment);
                    transaction.commit();
                    lastpos = p;
                    fragmentRefresh=attentionFragment;
                    break;
                case 2:

                    tvHomePage.setTextColor(getResources().getColor(R.color.txt_menu_gray));
                    tvAttention.setTextColor(getResources().getColor(R.color.txt_menu_gray));
                    tvDuty.setTextColor(getResources().getColor(R.color.main_color));
                    tvRoom.setTextColor(getResources().getColor(R.color.txt_menu_gray));

                    imgHome.setImageResource(R.mipmap.img_menu_home_off);
                    imgAttention.setImageResource(R.mipmap.img_attetntion_off);
                    imgDuty.setImageResource(R.mipmap.img_menu_duty_on);
                    imgRoom.setImageResource(R.mipmap.img_profile_off);

                    hideFragment(transaction);

                    if (dutyFragment == null) {
                        dutyFragment = DutyFragment.getInstance();
                        transaction.add(R.id.real_layout, dutyFragment);
                    }
                    //显示需要显示的fragment
                    transaction.show(dutyFragment);
                    transaction.commit();
                    lastpos = p;
                    fragmentRefresh=dutyFragment;
                    break;
                case 3:

                        tvHomePage.setTextColor(getResources().getColor(R.color.txt_menu_gray));
                        tvAttention.setTextColor(getResources().getColor(R.color.txt_menu_gray));
                        tvDuty.setTextColor(getResources().getColor(R.color.txt_menu_gray));
                        tvRoom.setTextColor(getResources().getColor(R.color.main_color));

                        imgHome.setImageResource(R.mipmap.img_menu_home_off);
                        imgAttention.setImageResource(R.mipmap.img_attetntion_off);
                        imgDuty.setImageResource(R.mipmap.img_menu_duty_off);
                        imgRoom.setImageResource(R.mipmap.img_profile_on);

                        if (profileFragment == null) {
                            profileFragment = ProfileFragment.getInstance(strCount);
                            transaction.add(R.id.real_layout, profileFragment);
                        }
                        //隐藏所有fragment
                        hideFragment(transaction);
                        //显示需要显示的fragment
                        transaction.show(profileFragment);
                        transaction.commit();
                        lastpos = p;
                    fragmentRefresh=profileFragment;
                    break;
            }
            if(fragmentRefresh!=null){
                fragmentRefresh.focusRefresh();
            }
    }
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (homeFragment == null && fragment instanceof HomeFragment)
            homeFragment = (HomeFragment) fragment;
        if (attentionFragment == null && fragment instanceof AttentionFragment)
            attentionFragment = (AttentionFragment) fragment;
        if (dutyFragment == null && fragment instanceof DutyFragment)
            dutyFragment = (DutyFragment) fragment;
        if (profileFragment == null && fragment instanceof ProfileFragment)
            profileFragment = (ProfileFragment) fragment;
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction){
        if(homeFragment != null){
            transaction.hide(homeFragment);
        }
        if(attentionFragment != null){
            transaction.hide(attentionFragment);
        }
        if(dutyFragment != null){
            transaction.hide(dutyFragment);
        }
        if(profileFragment != null){
            transaction.hide(profileFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

        }
    }

    public void goHome() {
        setSelected(0);
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


        getAppVersionCommitVo = new GetAppVersionCommitVo();
        getAppVersionCommitVo.setVid(""+Tools.getVersionCode(activity));
        getAppVersionCode = new GetAppVersionCode(activity, getAppVersionCommitVo, new ResultObjInterface<String>() {
            @Override
            public void success(ResultVO<String> data) {
                String url = data.getData();
                if (url != null && url.equals("") == false) {
                    showDialogUpdate(url);
                }
            }

            @Override
            public void error(String errmsg) {
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openUrl(String url){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 提示版本更新的对话框
     */
    private void showDialogUpdate(final String url) {
        ExitLoginDialog.show(activity, "有新版本可用,是否升级版本", "取消", "确认", new ExitLoginDialog.OnConfirmClickListener() {
            @Override
            public void onLeft() {

            }

            @Override
            public void onRight() {
//                busyView=BusyView.showText(activity,"正在更新");
//                downFile.down(BuildConfig.PIC_PATH+url,"apk");//下载最新的版本程序
                openUrl(BuildConfig.PIC_PATH+url);
            }
        },false);

      /*  MessageConfirmDialog.show(activity, "版本升级", "发现新版本！请及时更新", "取消", "确定", new MessageConfirmDialog.OnConfirmClickListener() {
            @Override
            public void onLeft() {

            }

            @Override
            public void onRight() {
                busyView=BusyView.showText(activity,"正在更新");
                downFile.down(BuildConfig.PIC_PATH+url,"apk");//下载最新的版本程序
            }
        },false);
*/
    }

    DownFile downFile = new DownFile(new DownFile.DownFileListener() {
        @Override
        public void success(String filepath) {
            //安装
            busyView.dismiss();
            installApk(filepath);
        }



        @Override
        public void errror(String errmsg) {
            busyView.dismiss();
            Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
        }

    });

    private void installApk(String filepath) {
        File file = new File(filepath);
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }


    //初始化控件事件
    private void initWidgetEvent(){
        //点击首页
        llMenuHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(0);
            }
        });
//        //点击通知公告
//        llMenuNotice.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setSelected(1);
//            }
//        });
        //点击我的关注
        llMenuAttention.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hidePoint();
                setSelected(1);
                attentionToZeroRequest.send();
            }
        });
        //点击今日值班
        llMenuDuty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(2);
            }
        });
        //点击个人中心
        llMenuRoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(3);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            Timer tExit = null;
            if (isExit == false) {
                isExit = true; // 准备退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false; // 取消退出
                    }
                }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

            } else {
                if (intentService != null) {
                    stopService(intentService);//停止服务
                }
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private class MyBroadcast extends BroadcastReceiver{

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
//            strCount = count;
            if (count != null && !"".equals(count) && !count.equals("0")) {
//                tvCircle.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.VISIBLE);
                tvCount.setText(count);
            }else {
//                tvCircle.setVisibility(View.INVISIBLE);
                tvCount.setVisibility(View.INVISIBLE);
            }
        }
    };

    public void hidePoint() {
//        tvCircle.setVisibility(View.INVISIBLE);
        tvCount.setVisibility(View.INVISIBLE);
    }
}
