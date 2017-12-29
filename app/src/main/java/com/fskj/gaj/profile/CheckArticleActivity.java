package com.fskj.gaj.profile;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.AppConfig;
import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.OnItemClickListener;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.home.fragment.CommonFragment;
import com.fskj.gaj.request.NotCheckListRequest;
import com.fskj.gaj.view.NoDataView;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.MsgListResultVo;
import com.fskj.gaj.vo.NotCheckListCommitVo;
import com.fskj.gaj.vo.NotCheckListResultVo;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CheckArticleActivity extends AppCompatActivity {

    private NotCheckListCommitVo notCheckListCommitVo;
    private NotCheckListRequest notCheckListRequest;
    private List<NotCheckListResultVo> msgList = new ArrayList<>();
    private NotCheckArticleAdapter adapter;
    private int module;
    private String type;
    private TextView tvTitle;

    public static void gotoActivity(Activity activity ,String type,int module,String name){
        Intent intent=new Intent(activity,CheckArticleActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("module",module);
        intent.putExtra("name",name);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),CheckArticleActivity.class);

        fr.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
    }

    private Toolbar toolBar;
    private ImageView imgBack;
    private NoDataView noData;
    private XRecyclerView xRecyclerView;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_article);
        activity=CheckArticleActivity.this;
        StatusBarUtil.setColor(activity,getResources().getColor(R.color.main_color),0);
        inflater = LayoutInflater.from(activity);


//界面初始化
        toolBar=(Toolbar)findViewById(R.id.toolBar);
        getSupportActionBar();
        tvTitle = (TextView) findViewById(R.id.tv_title);
        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        noData=(NoDataView)findViewById(R.id.no_data);
        xRecyclerView=(XRecyclerView)findViewById(R.id.xRecyclerView);
        noData.setVisibility(View.GONE);

        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        adapter = new NotCheckArticleAdapter();
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                notCheckListCommitVo.resetPage();
                notCheckListRequest.send();
            }

            @Override
            public void onLoadMore() {
                notCheckListCommitVo.nextPage();
                notCheckListRequest.send();
            }
        });
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){
            type = bundle.getString("type");
            String strName = bundle.getString("name","");
            tvTitle.setText("审核"+strName);
            module = bundle.getInt("module",0);
            notCheckListCommitVo.setType(type);
        }
        notCheckListRequest.send();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode) {
                case AppConfig.CHECK_ARTICLE:
                    notCheckListCommitVo.resetPage();
                    notCheckListRequest.send();
                    break;
            }
        }
    }

    //声明请求变量和返回结果
    private void initRequest(){
        LoginCommitVo loginInfo = LoginInfo.getLoginCommitInfo(activity);
        notCheckListCommitVo = new NotCheckListCommitVo();
        if (loginInfo != null) {
            notCheckListCommitVo.setUsername(loginInfo.getUsername());
            notCheckListCommitVo.setPassword(loginInfo.getPassword());
        }
        notCheckListRequest = new NotCheckListRequest(activity, notCheckListCommitVo, new ResultListInterface<NotCheckListResultVo>() {
            @Override
            public void success(ResultTVO<NotCheckListResultVo> data) {
                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();
                ArrayList<NotCheckListResultVo> resultVos = data.getData();

                if (notCheckListCommitVo.isFirstPage()) {
                    msgList.clear();
                }
                if (resultVos != null && resultVos.size() > 0) {
                    msgList.addAll(resultVos);
                }
                //更新适配器
                adapter.notifyDataSetChanged();
                if (msgList.size() > 0) {
                    xRecyclerView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                }else {
                    xRecyclerView.setEmptyView(noData);
                }
            }

            @Override
            public void error(String errmsg) {
                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化控件事件
    private void initWidgetEvent(){
        adapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CheckArticleDetailActivity.gotoActivity(activity,module,msgList.get(position).getMid(),type,msgList.get(position).getTitle());
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

    class NotCheckArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private OnItemClickListener mOnItemClickListener = null;


        public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_not_check_article, parent, false);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return new CommonViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            NotCheckListResultVo vo = msgList.get(position);
            (( CommonViewHolder)holder).tvTitle.setText(vo.getTitle());
            (( CommonViewHolder)holder).tvTime.setText(vo.getCreatetime());
            (( CommonViewHolder)holder).tvUserName.setText(vo.getUsername());
            (( CommonViewHolder)holder).itemView.setTag(position);
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
            TextView tvTime,tvUserName,tvTitle;
            public CommonViewHolder(View itemView) {
                super(itemView);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
                tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            }
        }
    }
}
