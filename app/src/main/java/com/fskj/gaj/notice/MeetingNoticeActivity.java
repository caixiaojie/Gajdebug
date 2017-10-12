package com.fskj.gaj.notice;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;


import com.fskj.gaj.NewsDetailActivity;
import com.fskj.gaj.OnItemClickListener;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.home.fragment.RecommandFragment;
import com.fskj.gaj.notice.adapter.CommonMAdapter;
import com.fskj.gaj.request.NoticeListRequest;
import com.fskj.gaj.vo.MsgListResultVo;
import com.fskj.gaj.vo.NoticeListCommitVo;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MeetingNoticeActivity extends AppCompatActivity {

    private NoticeListCommitVo noticeListCommitVo;
    private NoticeListRequest noticeListRequest;
    private TextView tvTitle;
    private String type;
    private String strTitle;
    private Handler handler = new Handler();
    private LinearLayoutManager layoutManager;
    private XRecyclerView xRecyclerView;
    private LinearLayout llNoData;

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,MeetingNoticeActivity.class);

        activity.startActivity(intent);
    }

    public static void gotoActivity(Fragment fr ,String type){
        Intent intent=new Intent(fr.getActivity(),MeetingNoticeActivity.class);
        intent.putExtra("type",type);
        fr.startActivity(intent);
        fr.getActivity().overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }
    private CommonMAdapter adapter;
    private List<MsgListResultVo> msgList = new ArrayList<>();
    private Toolbar toolBar;
    private ImageView imgBack;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_notice);
        activity=MeetingNoticeActivity.this;

        inflater = LayoutInflater.from(activity);


//界面初始化
        toolBar=(Toolbar)findViewById(R.id.toolBar);
        imgBack=(ImageView)findViewById(R.id.img_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        xRecyclerView =(XRecyclerView)findViewById(R.id.xRecyclerView);
        llNoData =(LinearLayout)findViewById(R.id.no_data);
        llNoData.setVisibility(View.GONE);
        xRecyclerView.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
        adapter = new CommonMAdapter(activity,msgList);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                noticeListCommitVo.resetPage();
                noticeListRequest.send();
            }

            @Override
            public void onLoadMore() {
                noticeListCommitVo.nextPage();
                noticeListRequest.send();
            }
        });
//声明请求变量和返回结果
        initRequest();

//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){
            type = bundle.getString("type", "");
            if (type.equals("huiyi")) {
                tvTitle.setText("会议通知");
                strTitle = new String("会议通知");
            }else if (type.equals("gonggao")) {
                tvTitle.setText("公示公告");
                strTitle = new String("公示公告");
            }else if (type.equals("tongbao")){
                tvTitle.setText("通知通报");
                strTitle = new String("通知通报");
            }else {
                tvTitle.setText("");
                strTitle = "";
            }
            noticeListCommitVo.setType(type);
        }

        noticeListRequest.send();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

        }
    }

    //声明请求变量和返回结果
    private void initRequest(){
        noticeListCommitVo = new NoticeListCommitVo();
        noticeListRequest = new NoticeListRequest(activity, noticeListCommitVo, new ResultListInterface<MsgListResultVo>() {
            @Override
            public void success(ResultTVO<MsgListResultVo> data) {
                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();
                ArrayList<MsgListResultVo> msgListResultVos = data.getData();
                if (msgListResultVos != null && msgListResultVos.size() > 0) {
                    msgList.addAll(msgListResultVos);
                    adapter.notifyDataSetChanged();
                }
                if (msgList.size() > 0) {
                    xRecyclerView.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                }else {
                    xRecyclerView.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void error(String errmsg) {
                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();
                if (msgList.size() > 0) {
                    xRecyclerView.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                }else {
                    xRecyclerView.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                }
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化控件事件
    private void initWidgetEvent(){
        //点击返回
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //点击item
        adapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsDetailActivity.gotoActivity(activity,msgList.get(position).getMid(),"notice",strTitle);
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
