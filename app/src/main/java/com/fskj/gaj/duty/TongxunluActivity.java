package com.fskj.gaj.duty;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.BuildConfig;
import com.fskj.gaj.R;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.home.DailyPoliceWebActivity;

public class TongxunluActivity extends AppCompatActivity {

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,TongxunluActivity.class);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    private ImageView imgBack;
    private WebView webView;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongxunlu);
        activity=TongxunluActivity.this;
        StatusBarUtil.setColor(activity,getResources().getColor(R.color.main_color),0);
        inflater = LayoutInflater.from(activity);


//界面初始化
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("机关通讯录");
        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        webView=(WebView)findViewById(R.id.webView);
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){
        }
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);


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
        String url = "http://10.155.20.78/other/jbtxl.mht";
        webView.setWebViewClient(wvc);
        webView.loadUrl(url);
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

    @Override
    public void finish() {
        super.finish();
        activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
    }
}
