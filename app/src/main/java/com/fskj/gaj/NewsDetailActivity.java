package com.fskj.gaj;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.content.Intent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.Util.StatusBarUtils;


public class NewsDetailActivity extends AppCompatActivity {



    public static void gotoActivity(Activity activity , String mid, String type ,String title){
        Intent intent=new Intent(activity,NewsDetailActivity.class);
        intent.putExtra("mid",mid);
        intent.putExtra("type",type);
        intent.putExtra("title",title);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    public static void gotoActivity(Fragment fr , String mid, String type ,String title){
        Intent intent=new Intent(fr.getActivity(),NewsDetailActivity.class);
        intent.putExtra("mid",mid);
        intent.putExtra("type",type);
        intent.putExtra("title",title);
        fr.startActivity(intent);
        fr.getActivity().overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }
    private TextView tvTitle;
    private String id;
    private TextView tvSign;
    private String path = "";
    private Toolbar toolBar;
    private ImageView imgBack;
    private ProgressBar progressBar;
    private WebView webView;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        activity=NewsDetailActivity.this;
        StatusBarUtil.setColor(activity,getResources().getColor(R.color.main_color),0);

        inflater = LayoutInflater.from(activity);


//界面初始化
        toolBar=(Toolbar)findViewById(R.id.toolBar);
        imgBack=(ImageView)findViewById(R.id.img_back);
        tvSign = (TextView) findViewById(R.id.tv_sign);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        webView=(WebView)findViewById(R.id.webView);
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){
            id = bundle.getString("mid", "");
            String type = bundle.getString("type", "");
            String title = bundle.getString("title", "");
            tvTitle.setText(title);
//            if (type.equals("picNews")) {//代表图片新闻详情
//                path = "/mobile/picnewsdetail.do?id="+ id;
//            }else if (type.equals("notice")) {//代表通知通报详情
//                path = "/mobile/noticedetail.do?id="+ id;
//            }else if (type.equals("msg")) {
//                path = "/mobile/msgdetail.do?id="+ id;
//            }
            if (type.equals("2")) {//代表图片新闻详情
                path = "/mobile/picnewsdetail.do?id="+ id;
            }else if (type.equals("1")) {//代表通知通报详情
                path = "/mobile/noticedetail.do?id="+ id;
            }else if (type.equals("0")) {
                path = "/mobile/msgdetail.do?id="+ id;
            }
        }

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

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
                Log.e("url",url);
//                webView.loadUrl(url);
                openUrl(url);
                // 消耗掉这个事件。Android中返回True的即到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
                return true;
            }
        };
        webView.setWebViewClient(wvc);
        webView.loadUrl(BuildConfig.SERVER_IP + path);
    }

    private void openUrl(String url){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
        //返回
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsSignActivity.gotoActivity(activity,id);
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
