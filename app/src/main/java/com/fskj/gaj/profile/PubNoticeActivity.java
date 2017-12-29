package com.fskj.gaj.profile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.request.PubNoticeRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.PubNoticeCommitVo;
import com.fskj.gaj.vo.PubnoticeVo;

import java.util.ArrayList;
import java.util.List;


public class PubNoticeActivity extends AppCompatActivity {

    private PubNoticeCommitVo pubNoticeCommitVo;
    private PubNoticeRequest pubNoticeRequest;
    private BusyView busyView;
//    private AppCompatSpinner spinner;

    private List<PubnoticeVo> pubnoticeVos;
    private LinearLayout llSelectArticleType;
    private TextView tvType;

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,PubNoticeActivity.class);

        activity.startActivity(intent);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),PubNoticeActivity.class);

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
    private EditText etTitle;
    private EditText etContent;
    private LinearLayout llPub;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_notice);
        activity=PubNoticeActivity.this;
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
        etTitle=(EditText)findViewById(R.id.et_title);
        etContent=(EditText)findViewById(R.id.et_content);
        llPub=(LinearLayout)findViewById(R.id.llPub);
        llSelectArticleType =(LinearLayout)findViewById(R.id.llSelectArticleType);
        tvType = (TextView) findViewById(R.id.tv_type);

        Tools.showSoftInputFromWindow(activity,etTitle);


//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){

        }
        //初始化spinner
        initSpinner();

    }

    private void initSpinner() {
        pubnoticeVos = LoginInfo.getPubNoticeInfo(activity);
        if (pubnoticeVos != null && pubnoticeVos.size() > 0) {
            tvType.setText(pubnoticeVos.get(0).getName());
            pubNoticeCommitVo.setType(pubnoticeVos.get(0).getType());
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
        pubNoticeCommitVo = new PubNoticeCommitVo();

        pubNoticeRequest = new PubNoticeRequest(activity, pubNoticeCommitVo, new ResultObjInterface<String>() {
            @Override
            public void success(ResultVO<String> data) {
                busyView.dismiss();
                Toast.makeText(activity,"发布成功",Toast.LENGTH_SHORT).show();
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
        //选择类型
        llSelectArticleType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoice( );
            }
        });

        //点击发布
        llPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTitle = Tools.safeString(etTitle);
                if (strTitle.equals("")) {
                    Toast.makeText(activity,"请输入文章标题",Toast.LENGTH_SHORT).show();
                    return;
                }
                String strType = Tools.safeString(tvType);
                if (strType.equals("") || strType.equals("请选择文章类型")) {
                    Toast.makeText(activity,"请选择文章类型",Toast.LENGTH_SHORT).show();
                    return;
                }
                String strContent = Tools.safeString(etContent);
                if (strContent.equals("")) {
                    Toast.makeText(activity,"请输入文章内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                //封装实体
                LoginCommitVo loginInfo = LoginInfo.getLoginCommitInfo(activity);
                pubNoticeCommitVo.setUsername(loginInfo.getUsername());
                pubNoticeCommitVo.setPassword(loginInfo.getPassword());
                pubNoticeCommitVo.setTitle(strTitle);
                pubNoticeCommitVo.setContent(strContent);
                busyView = BusyView.showText(activity,"发布中...");
                pubNoticeRequest.send();
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


    /**
     * 单选
     */
    private void dialogChoice( ) {
        if (pubnoticeVos == null || pubnoticeVos.size() == 0) {
            return;
        }

        String items[] = new String[pubnoticeVos.size()];
            for (int i = 0; i < pubnoticeVos.size(); i++) {
                PubnoticeVo pubnoticeVo = pubnoticeVos.get(i);
                items[i]=pubnoticeVo.getName();
            }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PubnoticeVo pubnoticeVo = pubnoticeVos.get(i);
                pubNoticeCommitVo.setType(pubnoticeVo.getType());
                tvType.setText(pubnoticeVo.getName());
            }
        }).create().show();

    }


}
