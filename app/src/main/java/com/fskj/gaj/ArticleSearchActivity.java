package com.fskj.gaj;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.content.Intent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.home.fragment.CommonFragment;
import com.fskj.gaj.home.fragment.RecommandFragment;
import com.fskj.gaj.notice.adapter.CommonMAdapter;
import com.fskj.gaj.request.MsgSearchRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.vo.MsgListResultVo;
import com.fskj.gaj.vo.MsgSearchCommitVo;
import com.fskj.gaj.vo.MsgSearchResultVo;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ArticleSearchActivity extends AppCompatActivity {

    private MsgSearchCommitVo msgSearchCommitVo;
    private MsgSearchRequest msgSearchRequest;
    private List<MsgSearchResultVo> msgSearchList = new ArrayList<>();
    private ArticleSearchAdapter adapter;
    private BusyView busyView;
    private EditText etSearch;
    private LinearLayout llSearchResult;
    private LinearLayoutManager layoutManager;
    private Handler handler = new Handler();
    private LinearLayout llNoData;
    private XRecyclerView xRecyclerView;

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,ArticleSearchActivity.class);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),ArticleSearchActivity.class);

        fr.startActivity(intent);
        fr.getActivity().overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    private Toolbar toolBar;
    private ImageView imgBack;
    private LinearLayout llSearch;
    private ImageView imgSearch;
//    private RecyclerView recyclerView;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_search);
        activity=ArticleSearchActivity.this;

        inflater = LayoutInflater.from(activity);


//界面初始化
        toolBar=(Toolbar)findViewById(R.id.toolBar);
        imgBack=(ImageView)findViewById(R.id.img_back);
        llSearch=(LinearLayout)findViewById(R.id.llSearch);
        llSearchResult =(LinearLayout)findViewById(R.id.llSearchResult);
        imgSearch=(ImageView)findViewById(R.id.img_search);
        etSearch = (EditText) findViewById(R.id.et_search);
        xRecyclerView =(XRecyclerView)findViewById(R.id.xRecyclerView);
        llNoData =(LinearLayout)findViewById(R.id.no_data);
        llNoData.setVisibility(View.GONE);
        xRecyclerView.setVisibility(View.GONE);
        llSearchResult.setVisibility(View.GONE);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) etSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etSearch, 0);
            }

        }, 500);

        layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
        adapter = new ArticleSearchAdapter();
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                msgSearchCommitVo.resetPage();
                msgSearchRequest.send();
            }

            @Override
            public void onLoadMore() {
                msgSearchCommitVo.nextPage();
                msgSearchRequest.send();
            }
        });
//声明请求变量和返回结果
        initRequest();

//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

        }
    }

    //声明请求变量和返回结果
    private void initRequest(){
        msgSearchCommitVo = new MsgSearchCommitVo();
        msgSearchRequest = new MsgSearchRequest(activity, msgSearchCommitVo, new ResultListInterface<MsgSearchResultVo>() {
            @Override
            public void success(ResultTVO<MsgSearchResultVo> data) {
                busyView.dismiss();
                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();
                ArrayList<MsgSearchResultVo> msgSearchResultVos = data.getData();
                if (msgSearchCommitVo.isFirstPage()) {
                    msgSearchList.clear();
                }
                if (msgSearchResultVos != null && msgSearchResultVos.size() > 0) {
                    msgSearchList.addAll(msgSearchResultVos);
                    adapter.notifyDataSetChanged();
                }

                if (msgSearchList.size() > 0) {
                    xRecyclerView.setVisibility(View.VISIBLE);
                    llSearchResult.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                }else {
                    xRecyclerView.setVisibility(View.GONE);
                    llSearchResult.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void error(String errmsg) {
                busyView.dismiss();
                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();
                if (msgSearchList.size() > 0) {
                    xRecyclerView.setVisibility(View.VISIBLE);
                    llSearchResult.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                }else {
                    xRecyclerView.setVisibility(View.GONE);
                    llSearchResult.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.VISIBLE);
                }
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });
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
        //点击搜索图标
        imgSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchWord = Tools.safeString(etSearch);
                if (searchWord.equals("")) {
                    Toast.makeText(activity,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                Tools.closeInputWindow(activity);
                msgSearchCommitVo.setKeys(searchWord);
                msgSearchCommitVo.resetPage();
                busyView = BusyView.showQuery(activity);
                msgSearchRequest.send();
            }
        });
        //点击软键盘搜索
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH ) {
                    Tools.closeInputWindow(activity);
                    String searchWord = Tools.safeString(etSearch);
                    if (searchWord.equals("")) {
                        Toast.makeText(activity,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    msgSearchCommitVo.setKeys(searchWord);
                    msgSearchCommitVo.resetPage();
                    busyView = BusyView.showQuery(activity);
                    msgSearchRequest.send();
                }
                return false;
            }
        });
        //item点击事件
        adapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MsgSearchResultVo vo = msgSearchList.get(position);
                if (vo.getReason().equals("0")) {//图片新闻
                    NewsDetailActivity.gotoActivity(activity,vo.getMid(),"picNews",vo.getType());
                }else if (vo.getReason().equals("1")) {//文章
                    NewsDetailActivity.gotoActivity(activity,vo.getMid(),"msg",vo.getType());
                }else {
                    NewsDetailActivity.gotoActivity(activity,vo.getMid(),"notice",vo.getType());
                }
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

    class ArticleSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnClickListener{

        private OnItemClickListener mOnItemClickListener = null;



        public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_msgsearch_layout, parent, false);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return new CommonViewHolder(view);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MsgSearchResultVo vo = msgSearchList.get(position);
            ((CommonViewHolder)holder).tvTitle.setText(vo.getTitle());
            ((CommonViewHolder)holder).tvTime.setText(vo.getCreatetime());
            ((CommonViewHolder)holder).tvVisit.setText(vo.getVisit() + "");
            ((CommonViewHolder)holder).tvType.setText(vo.getType());
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return msgSearchList.size();
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(view,(int)view.getTag());
            }
        }

        class CommonViewHolder extends RecyclerView.ViewHolder{
            TextView tvTime,tvVisit,tvTitle,tvType;
            public CommonViewHolder(View itemView) {
                super(itemView);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
                tvVisit = (TextView) itemView.findViewById(R.id.tv_count);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvType = (TextView) itemView.findViewById(R.id.tv_type);
            }
        }
    }
}
