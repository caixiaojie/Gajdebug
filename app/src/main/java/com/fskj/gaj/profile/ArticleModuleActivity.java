package com.fskj.gaj.profile;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.request.CheckModuleRequest;
import com.fskj.gaj.vo.CheckModuleResultVo;
import com.fskj.gaj.vo.LoginCommitVo;

import java.util.ArrayList;
import java.util.List;

public class ArticleModuleActivity extends AppCompatActivity {

    private LoginCommitVo loginCommitVo;
    private CheckModuleRequest checkModuleRequest;
    private List<CheckModuleResultVo> articleList = new ArrayList<>();
    private ArticleModuleAdapter adapter;

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,ArticleModuleActivity.class);

        activity.startActivity(intent);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),ArticleModuleActivity.class);

        fr.startActivity(intent);
        fr.getActivity().overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
    }

    private Toolbar toolBar;
    private ImageView imgBack;
    private ListView listview;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_module);
        activity=ArticleModuleActivity.this;
        StatusBarUtil.setColor(activity,getResources().getColor(R.color.main_color),0);
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
        listview=(ListView)findViewById(R.id.listview);
        adapter = new ArticleModuleAdapter();
        listview.setAdapter(adapter);
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){

        }

        checkModuleRequest.send();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

        }
    }

    //声明请求变量和返回结果
    private void initRequest(){
        LoginCommitVo loginInfo = LoginInfo.getLoginCommitInfo(activity);
        loginCommitVo = new LoginCommitVo();
        if (loginInfo != null) {
            loginCommitVo.setUsername(loginInfo.getUsername());
            loginCommitVo.setPassword(loginInfo.getPassword());
        }
        checkModuleRequest = new CheckModuleRequest(activity, loginCommitVo, new ResultListInterface<CheckModuleResultVo>() {
            @Override
            public void success(ResultTVO<CheckModuleResultVo> data) {
                ArrayList<CheckModuleResultVo> resultVos = data.getData();
                if (resultVos != null && resultVos.size() > 0) {
                    articleList.addAll(resultVos);
                }
                adapter.notifyDataSetChanged();
                if (articleList.size() == 0) {
                    View emptyView = LayoutInflater.from(activity).inflate(R.layout.no_data_layout, null);
                    listview.setEmptyView(emptyView);
                }
            }

            @Override
            public void error(String errmsg) {
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化控件事件
    private void initWidgetEvent(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckArticleActivity.gotoActivity(activity,articleList.get(i).getType(),articleList.get(i).getModule(),articleList.get(i).getName());
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

    class ArticleModuleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return articleList.size() == 0 ? 0 : articleList.size();
        }

        @Override
        public Object getItem(int i) {
            return articleList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(activity).inflate(R.layout.item_article_module,viewGroup,false);
                holder = new ViewHolder();
                holder.tvModule = (TextView) view.findViewById(R.id.tv_module);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }
            CheckModuleResultVo vo = articleList.get(i);
            holder.tvModule.setText(vo.getName());
            return view;
        }
        class ViewHolder {
            TextView tvModule;
        }
    }
}
