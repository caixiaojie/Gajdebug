package com.fskj.gaj.home.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fskj.gaj.AppConfig;
import com.fskj.gaj.GajApplication;
import com.fskj.gaj.NewsDetailActivity;
import com.fskj.gaj.OnItemClickListener;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.Util.PageQuery;
import com.fskj.gaj.request.PicNewsListRequest;
import com.fskj.gaj.vo.PicNewsListResultVo;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommandFragment extends Fragment {


    private XRecyclerView xRecyclerView;
    private LinearLayout llNoData;

    public static RecommandFragment getInstance( ){
        RecommandFragment f =new RecommandFragment();
        Bundle bundle=new Bundle();

        f.setArguments(bundle);

        return f;
    }

    private Activity activity;
    private PageQuery pageQuery;
    private PicNewsListRequest picNewsListRequest;
    private List<PicNewsListResultVo> picNewsList = new ArrayList<>();
    private RecommandAdapter adapter;
    private Handler handler = new Handler();

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
        View v=inflater.inflate(R.layout.fragment_recommand, null);
        llNoData = (LinearLayout) v.findViewById(R.id.no_data);
        xRecyclerView = (XRecyclerView) v.findViewById(R.id.xRecyclerView);

        llNoData.setVisibility(View.GONE);
        xRecyclerView.setVisibility(View.GONE);

        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        adapter = new RecommandAdapter();
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageQuery.resetPage();
                picNewsListRequest.send();
            }

            @Override
            public void onLoadMore() {
                pageQuery.nextPage();
                picNewsListRequest.send();
            }
        });

//声明请求变量和返回结果
        initRequest();

//初始化控件事件
        initWidgetEvent();
        picNewsListRequest.send();
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT));
        return v;
    }

    //声明请求变量和返回结果
    private void initRequest(){
        pageQuery = new PageQuery();
        picNewsListRequest = new PicNewsListRequest(activity, pageQuery, new ResultListInterface<PicNewsListResultVo>() {
            @Override
            public void success(ResultTVO<PicNewsListResultVo> data) {

                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();

                ArrayList<PicNewsListResultVo> newsListResultVos = data.getData();
                if (pageQuery.isFirstPage()) {
                    picNewsList.clear();
                }
                if (newsListResultVos != null && newsListResultVos.size() > 0) {
                    picNewsList.addAll(newsListResultVos);
                    adapter.notifyDataSetChanged();
                }

                if (picNewsList.size() > 0) {
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

                if (picNewsList.size() > 0) {
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
                NewsDetailActivity.gotoActivity(RecommandFragment.this,picNewsList.get(position).getPid(),"picNews","图文详情");
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

    class RecommandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnClickListener{
        private OnItemClickListener mOnItemClickListener = null;


        public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_recommend_layout,parent,false);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return new MyViewHoler(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            PicNewsListResultVo vo = picNewsList.get(position);

            ((MyViewHoler) holder).tvTitle.setText(vo.getTitle());
            ((MyViewHoler) holder).tvTime.setText(vo.getCreatetime());
            ((MyViewHoler) holder).tvCount.setText("" + vo.getVisit());
            Glide.with(activity).load(AppConfig.picPath + vo.getImgurl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.img_public_notice)
                    .into(((MyViewHoler) holder).img);
            holder.itemView.setTag(position);

        }

        @Override
        public int getItemCount() {
            return picNewsList == null ? 0 : picNewsList.size();
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(view,(int)view.getTag());
            }
        }

        class MyViewHoler extends RecyclerView.ViewHolder {
            TextView tvTitle,tvTime,tvCount;
            ImageView img;
            public MyViewHoler(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
                tvCount = (TextView) itemView.findViewById(R.id.tv_number);
                img = (ImageView) itemView.findViewById(R.id.img_news);
            }
        }
    }
}
