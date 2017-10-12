package com.fskj.gaj.home.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;


import com.fskj.gaj.NewsDetailActivity;
import com.fskj.gaj.OnItemClickListener;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.Util.TitleTypeSP;
import com.fskj.gaj.notice.adapter.CommonMAdapter;
import com.fskj.gaj.request.MsgListRequest;
import com.fskj.gaj.vo.MsgListCommitVo;
import com.fskj.gaj.vo.MsgListResultVo;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommonFragment extends Fragment {


    private MsgListCommitVo msgListCommitVo;
    private String type;
    private MsgListRequest msgListRequest;
    private List<MsgListResultVo> msgList = new ArrayList<>();
    private CommonAdapter adapter;
    private Handler handler = new Handler();
    private List<String> titleENStr = new ArrayList<>();
    private List<String> titleZHStr = new ArrayList<>();
    private String strTitle;
    private LinearLayoutManager layoutManager;
    private XRecyclerView xRecyclerView;
    private LinearLayout llNoData;

    public static CommonFragment getInstance(String type){
        CommonFragment f =new CommonFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        f.setArguments(bundle);

        return f;
    }

    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle=getArguments();
        if(bundle!=null){
            type = bundle.getString("type", "");
        }
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
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
        View v=inflater.inflate(R.layout.fragment_common, null);
        llNoData = (LinearLayout) v.findViewById(R.id.no_data);
        xRecyclerView = (XRecyclerView) v.findViewById(R.id.xRecyclerView);

        llNoData.setVisibility(View.GONE);
        xRecyclerView.setVisibility(View.GONE);

        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
        layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        xRecyclerView.setLayoutManager(layoutManager);
        adapter = new CommonAdapter();
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                msgListCommitVo.resetPage();
                msgListRequest.send();
            }

            @Override
            public void onLoadMore() {
                msgListCommitVo.nextPage();
                msgListRequest.send();
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
        msgListRequest.send();
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT));
        return v;
    }

    //声明请求变量和返回结果
    private void initRequest(){
        msgListCommitVo = new MsgListCommitVo();
        if (type != null && type.equals("") == false) {
            msgListCommitVo.setType(type);
        }
        msgListRequest = new MsgListRequest(activity, msgListCommitVo, new ResultListInterface<MsgListResultVo>() {
            @Override
            public void success(ResultTVO<MsgListResultVo> data) {
                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();
                ArrayList<MsgListResultVo> msgListResultVos = data.getData();
                if (msgListCommitVo.isFirstPage()) {
                    msgList.clear();
                }
                if (msgListResultVos != null && msgListResultVos.size() > 0) {
                    msgList.addAll(msgListResultVos);
                    adapter.notifyDataSetChanged();
                }

                if (msgList.size() > 0) {
                    llNoData.setVisibility(View.GONE);
                    xRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    llNoData.setVisibility(View.VISIBLE);
                    xRecyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void error(String errmsg) {
                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();

                if (msgList.size() > 0) {
                    llNoData.setVisibility(View.GONE);
                    xRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    llNoData.setVisibility(View.VISIBLE);
                    xRecyclerView.setVisibility(View.GONE);
                }
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化控件事件
    private void initWidgetEvent(){

        //item点击事件
        adapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsDetailActivity.gotoActivity(CommonFragment.this,msgList.get(position).getMid(),"msg",strTitle);
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

    class CommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnClickListener{

        private OnItemClickListener mOnItemClickListener = null;


        public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_common_layout, parent, false);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return new CommonViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MsgListResultVo vo = msgList.get(position);
            ((CommonViewHolder)holder).tvTitle.setText(vo.getTitle());
            ((CommonViewHolder)holder).tvTime.setText(vo.getCreatetime());
            ((CommonViewHolder)holder).tvVisit.setText(vo.getVisit()+"");
            ((CommonViewHolder)holder).itemView.setTag(position);
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

        class CommonViewHolder extends RecyclerView.ViewHolder{
            TextView tvTime,tvVisit,tvTitle;
            public CommonViewHolder(View itemView) {
                super(itemView);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
                tvVisit = (TextView) itemView.findViewById(R.id.tv_count);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            }
        }
    }
}
