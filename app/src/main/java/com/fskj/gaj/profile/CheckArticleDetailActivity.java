package com.fskj.gaj.profile;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.AppConfig;
import com.fskj.gaj.BuildConfig;
import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.request.CheckMsgRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.view.MessageConfirmDialog;
import com.fskj.gaj.view.MessageConfirmDialogCopy;
import com.fskj.gaj.vo.CheckMsgCommitVo;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.PubNoticeCommitVo;

public class CheckArticleDetailActivity extends AppCompatActivity {

    private CheckMsgCommitVo checkListCommitVo;
    private CheckMsgRequest checkListRequest;
    private BusyView busyView;
    private String path;

    public static void gotoActivity(Activity activity ,int module,String mid,String type,String title){
        Intent intent=new Intent(activity,CheckArticleDetailActivity.class);
        intent.putExtra("module",module);
        intent.putExtra("mid",mid);
        intent.putExtra("type",type);
        intent.putExtra("title",title);
        activity.startActivityForResult(intent, AppConfig.CHECK_ARTICLE);
        activity.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),CheckArticleDetailActivity.class);

        fr.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
    }

    private ProgressBar progressBar;
    private Toolbar toolBar;
    private ImageView imgBack;
    private WebView webView;
    private TextView tvPass;
    private TextView tvRefuse;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_article_detail);
        activity=CheckArticleDetailActivity.this;
        StatusBarUtil.setColor(activity,getResources().getColor(R.color.main_color),0);
        inflater = LayoutInflater.from(activity);


//界面初始化
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        toolBar=(Toolbar)findViewById(R.id.toolBar);
        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        webView=(WebView)findViewById(R.id.webView);
        tvPass=(TextView)findViewById(R.id.tv_pass);
        tvRefuse=(TextView)findViewById(R.id.tv_refuse);
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){
            String id = bundle.getString("mid");
            String type = bundle.getString("type");
            String title = bundle.getString("title");
            checkListCommitVo.setType(type);
            checkListCommitVo.setMid(id);
            checkListCommitVo.setTitle(title);
            int module = bundle.getInt("module", 0);//0=msg,1=notice,2=picnews”
            if (module == 2) {//代表图片新闻详情
                path = "/mobile/cpicnewsdetail.do?id="+ id;
            }else if (module == 1) {//代表通知通报详情
                path = "/mobile/cnoticedetail.do?id="+ id;
            }else if (module == 0) {
                path = "/mobile/cmsgdetail.do?id="+ id;
            }
        }

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
        // 设置setWebChromeClient对象
        webView.setWebChromeClient(wvcc);
        // 创建WebViewClient对象
        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                webView.loadUrl(url);
                // 消耗掉这个事件。Android中返回True的即到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
                return true;
            }
        };
        webView.setWebViewClient(wvc);
        webView.loadUrl(BuildConfig.SERVER_IP + path);

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
        checkListCommitVo = new CheckMsgCommitVo();
        if (loginInfo != null) {
            checkListCommitVo.setUsername(loginInfo.getUsername());
            checkListCommitVo.setPassword(loginInfo.getPassword());
        }
        checkListRequest = new CheckMsgRequest(activity, checkListCommitVo, new ResultObjInterface<String>() {
            @Override
            public void success(ResultVO<String> data) {
                busyView.dismiss();
                CheckArticleDetailActivity.this.setResult(RESULT_OK);
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
        //通过审核
        tvPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageConfirmDialog.show(activity, "审核", "确认通过审核吗?", "取消", "确认", new MessageConfirmDialog.OnConfirmClickListener() {
                    @Override
                    public void onLeft() {

                    }

                    @Override
                    public void onRight() {
                        busyView = BusyView.showCommit(activity);
                        checkListCommitVo.setReason("");//拒绝原因 ，如果为空或者空串，说明过审
                        checkListRequest.send();
                    }
                },true);
            }
        });
        //拒绝审核
        tvRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageConfirmDialogCopy.show(activity, "审核", "取消", "确定", new MessageConfirmDialogCopy.OnConfirmClickListener() {
                    @Override
                    public void onLeft(EditText editText) {

                    }

                    @Override
                    public void onRight(EditText editText) {
                        String strContent = Tools.safeString(editText);
                        if (strContent.equals("")) {
                            Toast.makeText(activity,"请输入拒绝原因",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        busyView = BusyView.showCommit(activity);
                        checkListCommitVo.setReason(strContent);
                        checkListRequest.send();
                    }
                },true);
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
