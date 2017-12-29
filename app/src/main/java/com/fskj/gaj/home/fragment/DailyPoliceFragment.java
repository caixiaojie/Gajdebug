package com.fskj.gaj.home.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;


import com.fskj.gaj.OnItemClickListener;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.Util.PageQuery;
import com.fskj.gaj.home.DailyPoliceWebActivity;
import com.fskj.gaj.notice.adapter.CommonMAdapter;
import com.fskj.gaj.request.DailyPoliceRequest;
import com.fskj.gaj.view.NoDataView;
import com.fskj.gaj.vo.MsgListResultVo;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyPoliceFragment extends Fragment {


    private LinearLayoutManager layoutManager;
    private PageQuery pageQuery;
    private DailyPoliceRequest dailyPoliceRequest;


    private boolean IS_LOADED = false;
    private static int mSerial=0;
    private int mTabPos=0;
    private boolean isFirst = true;

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(!IS_LOADED){
                IS_LOADED = true;
                //这里执行加载数据的操作
                dailyPoliceRequest.send();
            }
            return;
        };
    };

    public void sendMessage(){
        Message message = handler.obtainMessage();
        message.sendToTarget();
    }

    public void setTabPos(int mTabPos) {
        this.mTabPos = mTabPos;
    }

    public static DailyPoliceFragment getInstance(int serial ){
        DailyPoliceFragment f =new DailyPoliceFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("mSerial",serial);
        f.setArguments(bundle);

        return f;
    }

    private Activity activity;
    private CommonMAdapter adapter;
    private List<MsgListResultVo> msgList = new ArrayList<>();
    private NoDataView noData;
    private XRecyclerView xRecyclerView;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle=getArguments();
        if(bundle!=null){
            mSerial = bundle.getInt("mSerial", 0);
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
        View v=inflater.inflate(R.layout.fragment_daily_police, null);
        noData=(NoDataView)v.findViewById(R.id.no_data);
        xRecyclerView=(XRecyclerView)v.findViewById(R.id.xRecyclerView);



        layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
        adapter = new CommonMAdapter();
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageQuery.resetPage();
                dailyPoliceRequest.send();
            }

            @Override
            public void onLoadMore() {
                pageQuery.nextPage();
                dailyPoliceRequest.send();
            }
        });
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//        dailyPoliceRequest.send();
        if (isFirst && mTabPos==mSerial) {
            isFirst = false;
            sendMessage();
        }
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT));
        return v;
    }

    //声明请求变量和返回结果
    private void initRequest(){
        pageQuery = new PageQuery();
        dailyPoliceRequest = new DailyPoliceRequest(activity, pageQuery, new ResultListInterface<MsgListResultVo>() {
            @Override
            public void success(ResultTVO<MsgListResultVo> data) {
                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();
                ArrayList<MsgListResultVo> msgListResultVos = data.getData();

                if (pageQuery.isFirstPage()) {
                    msgList.clear();
                }

                if (msgListResultVos != null && msgListResultVos.size() > 0) {
                    msgList.addAll(msgListResultVos);
                }
                adapter.notifyDataSetChanged();
                if (msgList.size() > 0) {
                    xRecyclerView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                }else {
                    xRecyclerView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void error(String errmsg) {
                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();
                if (msgList.size() > 0) {
                    xRecyclerView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                }else {
                    xRecyclerView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
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

                DailyPoliceWebActivity.gotoActivity(DailyPoliceFragment.this, msgList.get(position).getMid());
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

    class CommonMAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
//        private Context context;
//        private List<MsgListResultVo> msgList;


        private OnItemClickListener mOnItemClickListener = null;


        public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

//        public CommonMAdapter(Context context, List<MsgListResultVo> msgList) {
//            this.context = context;
//            this.msgList = msgList;
//        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_dailypolice_layout, parent, false);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return new CommonViewHoler(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MsgListResultVo vo = msgList.get(position);
            ((CommonViewHoler)holder).tvTitle.setText(vo.getTitle());
            ((CommonViewHoler)holder).tvTime.setText(vo.getCreatetime());
            ((CommonViewHoler)holder).itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return msgList.size();
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(view,(int)view.getTag());
            }
        }
        class CommonViewHoler extends RecyclerView.ViewHolder{
            TextView tvTitle,tvTime;
            public CommonViewHoler(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            }
        }
    }
}
