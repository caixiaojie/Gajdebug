package com.fskj.gaj.home.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View.OnClickListener;


import com.fskj.gaj.NewsDetailActivity;
import com.fskj.gaj.OnItemClickListener;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.Util.TitleTypeSP;
import com.fskj.gaj.notice.adapter.CommonMAdapter;
import com.fskj.gaj.request.NoticeListRequest;
import com.fskj.gaj.view.NoDataView;
import com.fskj.gaj.vo.MsgListResultVo;
import com.fskj.gaj.vo.NoticeListCommitVo;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Common2Fragment extends Fragment {


    private LinearLayoutManager layoutManager;
    private CommonMAdapter adapter;
    private List<MsgListResultVo> msgList = new ArrayList<>();
    private NoticeListCommitVo noticeListCommitVo;
    private NoticeListRequest noticeListRequest;
    private String type;
    private List<String> titleENStr;
    private List<String> titleZHStr;
    private String strTitle;

    public static Common2Fragment getInstance(String type ){
        Common2Fragment f =new Common2Fragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        f.setArguments(bundle);

        return f;
    }

    private Activity activity;

    private NoDataView llNoData;
    private XRecyclerView xRecyclerView;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle=getArguments();
        if(bundle!=null){
            type = bundle.getString("type", "");
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
        View v=inflater.inflate(R.layout.fragment_common2, null);
        llNoData=(NoDataView)v.findViewById(R.id.no_data);
        xRecyclerView=(XRecyclerView)v.findViewById(R.id.xRecyclerView);
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

        //获取title名称
        titleENStr = TitleTypeSP.getENTitleStr(activity);
        titleZHStr = TitleTypeSP.getZHTitleStr(activity);
        if (type.equals("") == false && titleENStr.size() > 0) {
            for (int i = 0; i < titleENStr.size(); i++) {
                if (type.equals(titleENStr.get(i))) {
                    strTitle = titleZHStr.get(i);
                }
            }
        }


//初始化控件事件
        initWidgetEvent();
        noticeListRequest.send();
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT));
        return v;
    }

    //声明请求变量和返回结果
    private void initRequest(){
        noticeListCommitVo = new NoticeListCommitVo();
        if (type != null && type.equals("") == false) {
            noticeListCommitVo.setType(type);
        }
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){

        }
    }


}
