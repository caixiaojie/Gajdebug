package com.fskj.gaj;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.Util.DownFile;
import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.Util.YFileManager;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.view.SuperFileView2;

import java.io.IOException;

public class FileDetailActivity extends AppCompatActivity {

    public static void gotoActivity(Activity activity ,String url){
        Intent intent=new Intent(activity,FileDetailActivity.class);
        intent.putExtra("url",url);
        activity.startActivity(intent);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),FileDetailActivity.class);

        fr.startActivity(intent);
    }

    private Toolbar toolBar;
    private ImageView imgBack;
    private TextView tvTitle;
    private SuperFileView2 superFileView;
    private LayoutInflater inflater;
    private Activity activity;
    private BusyView busyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        activity=FileDetailActivity.this;

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
        tvTitle=(TextView)findViewById(R.id.tv_title);
        superFileView=(SuperFileView2)findViewById(R.id.superFileView);
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){
            String url = bundle.getString("url");
            int iIndex = url.lastIndexOf(".");
            String type = url.substring(iIndex+1,url.length());
            busyView = BusyView.showQuery(activity);
            download(url,type);
        }

    }

    private void download(String url,String type) {

        DownFile df=new DownFile(new DownFile.DownFileListener() {
            @Override
            public void success(String filepath) {
                Log.e("filepath",filepath);
                superFileView.displayFile(YFileManager.getCachePath(), filepath);
                busyView.dismiss();
            }

            @Override
            public void errror(String errmsg) {
                busyView.dismiss();
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }


        });
        Log.e("filepath","jjjjjj");
        df.down(url,type);


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
    protected void onDestroy() {
        super.onDestroy();
        if (superFileView != null) {
            superFileView.onStopDisplay();
            try {
                Tools.deleteFilesByDirectory(YFileManager.getCachDirectory());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
