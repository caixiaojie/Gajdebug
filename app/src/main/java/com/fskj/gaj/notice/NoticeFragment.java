package com.fskj.gaj.notice;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fskj.gaj.NewsDetailActivity;
import com.fskj.gaj.OnItemClickListener;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.notice.adapter.CommonMAdapter;
import com.fskj.gaj.request.NoticeListRequest;
import com.fskj.gaj.vo.MsgListResultVo;
import com.fskj.gaj.vo.NoticeListCommitVo;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeFragment extends Fragment {


    private LinearLayout llMeetingNotice;
    private LinearLayout llPublicNotice;
    private LinearLayout llNotificationNotice;
    private XRecyclerView xRecyclerView;
    private LinearLayout llNoData;
    private LinearLayoutManager layoutManager;
    private CommonMAdapter adapter;
    private List<MsgListResultVo> msgList = new ArrayList<>();
    private NoticeListCommitVo noticeListCommitVo;
    private NoticeListRequest noticeListRequest;

    public static NoticeFragment getInstance( ){
        NoticeFragment f =new NoticeFragment();
        Bundle bundle=new Bundle();

        f.setArguments(bundle);

        return f;
    }

    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle=getArguments();
        if(bundle!=null){

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
        View v=inflater.inflate(R.layout.fragment_notice, null);
//        llMeetingNotice = (LinearLayout) v.findViewById(R.id.llMeetingNotice);
//        llPublicNotice = (LinearLayout) v.findViewById(R.id.llPublicNotice);
//        llNotificationNotice = (LinearLayout) v.findViewById(R.id.llNotificationNotice);
        xRecyclerView =(XRecyclerView)v.findViewById(R.id.xRecyclerView);
        llNoData =(LinearLayout)v.findViewById(R.id.no_data);
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
        noticeListRequest.send();
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT));
        return v;
    }

    //声明请求变量和返回结果
    private void initRequest(){
        noticeListCommitVo = new NoticeListCommitVo();
        noticeListCommitVo.setType("gonggao");
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
        /*//会议通知
        llMeetingNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeetingNoticeActivity.gotoActivity(NoticeFragment.this,"huiyi");
            }
        });
        //公示公告
        llPublicNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeetingNoticeActivity.gotoActivity(NoticeFragment.this,"gonggao");
            }
        });
        //通知通报
        llNotificationNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeetingNoticeActivity.gotoActivity(NoticeFragment.this,"tongbao");
            }
        });*/

        //点击返回
        /*imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
        //点击item
        adapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsDetailActivity.gotoActivity(activity,msgList.get(position).getMid(),"notice","公示公告");
            }
        });

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


}
