package com.fskj.gaj.profile;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.AppConfig;
import com.fskj.gaj.CallBack;
import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.request.AddAttentionRequest;
import com.fskj.gaj.request.DelAttentionRequest;
import com.fskj.gaj.request.MyAttentionRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.view.NoDataView;
import com.fskj.gaj.vo.AddAttentionCommitVo;
import com.fskj.gaj.vo.DelAttentionCommitVo;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.MyAttentionResultVo;

import java.util.ArrayList;
import java.util.List;

public class CareSetActivity extends AppCompatActivity implements CallBack {

    private LoginCommitVo loginCommitVo;
    private MyAttentionRequest myAttentionRequest;
    private BusyView busyView;
    private List<MyAttentionResultVo> moduleList = new ArrayList<>();
    private CareSetAdapter adapter;
    private NoDataView noDataView;
    private AddAttentionCommitVo addAttentionCommitVo;
    private AddAttentionRequest addAttentionRequest;
    private DelAttentionCommitVo delAttentionCommitVo;
    private DelAttentionRequest delAttentionRequest;
    private MyAttentionResultVo vo;
    private ImageView imgCare;


    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,CareSetActivity.class);

        activity.startActivityForResult(intent, AppConfig.CARE_SET);
        activity.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }



    @Override
    public void finish() {
        super.finish();
        activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
    }

    private Toolbar toolBar;
    private ImageView imgBack;
    private TextView tvFinish;
    private ListView listview;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_set);
        activity=CareSetActivity.this;
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
        tvFinish=(TextView)findViewById(R.id.tv_finish);
        listview=(ListView)findViewById(R.id.listview);
        noDataView = (NoDataView) findViewById(R.id.no_data);
        listview.setEmptyView(noDataView);
        adapter = new CareSetAdapter();
        adapter.setCallBack(this);
        listview.setAdapter(adapter);
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){

        }

        busyView = BusyView.showQuery(activity);
        myAttentionRequest.send();
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
        addAttentionCommitVo = new AddAttentionCommitVo();
        delAttentionCommitVo = new DelAttentionCommitVo();
        if (loginInfo != null ) {
            loginCommitVo.setUsername(loginInfo.getUsername());
            loginCommitVo.setPassword(loginInfo.getPassword());

            addAttentionCommitVo.setUsername(loginInfo.getUsername());
            addAttentionCommitVo.setPassword(loginInfo.getPassword());

            delAttentionCommitVo.setUsername(loginInfo.getUsername());
            delAttentionCommitVo.setPassword(loginInfo.getPassword());
        }
        myAttentionRequest = new MyAttentionRequest(activity, loginCommitVo, new ResultListInterface<MyAttentionResultVo>() {
            @Override
            public void success(ResultTVO<MyAttentionResultVo> data) {
                busyView.dismiss();
                ArrayList<MyAttentionResultVo> resultVos = data.getData();
                moduleList.clear();
                if (resultVos != null && resultVos.size() > 0) {
                    moduleList.addAll(resultVos);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(String errmsg) {
                adapter.notifyDataSetChanged();
                busyView.dismiss();
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });

        //新增关注
        addAttentionRequest = new AddAttentionRequest(activity, addAttentionCommitVo, new ResultObjInterface<String>() {
            @Override
            public void success(ResultVO<String> data) {
                busyView.dismiss();
                vo.turn();
                CareSetActivity.this.setResult(RESULT_OK);
                imgCare.setImageResource(R.mipmap.img_share_on);
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(String errmsg) {
                busyView.dismiss();
                imgCare.setImageResource(R.mipmap.img_share_off);
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });

        //取消关注
        delAttentionRequest = new DelAttentionRequest(activity, delAttentionCommitVo, new ResultObjInterface<String>() {
            @Override
            public void success(ResultVO<String> data) {
                busyView.dismiss();
                vo.turn();
                CareSetActivity.this.setResult(RESULT_OK);
                imgCare.setImageResource(R.mipmap.img_share_off);
            }

            @Override
            public void error(String errmsg) {
                busyView.dismiss();
                imgCare.setImageResource(R.mipmap.img_share_on);
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });
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
    public void click(View v) {
        imgCare = (ImageView) v;
        int i = (int) v.getTag();
        vo = moduleList.get(i);
        if (vo.isFlag()) {
            busyView = BusyView.showText(activity,"正在关注");
            addAttentionCommitVo.setEn(vo.getEn());
            addAttentionCommitVo.setType(vo.getType());
            addAttentionRequest.send();
        }else {
            busyView = BusyView.showText(activity,"取消关注");
            delAttentionCommitVo.setEn(vo.getEn());
            delAttentionCommitVo.setType(vo.getType());
            delAttentionRequest.send();
        }
    }

    class CareSetAdapter extends BaseAdapter implements View.OnClickListener{
        CallBack callBack;

        public void setCallBack(CallBack callBack) {
            this.callBack = callBack;
        }
        @Override
        public int getCount() {
            return moduleList.size();
        }

        @Override
        public Object getItem(int i) {
            return moduleList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(activity).inflate(R.layout.item_care_set,viewGroup,false);
                holder = new ViewHolder();
                holder.imgCare = (ImageView) view.findViewById(R.id.img_care);
                holder.tvModule = (TextView) view.findViewById(R.id.tv_module);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }
            //适配
            final MyAttentionResultVo vo = moduleList.get(i);
            holder.tvModule.setText(vo.getZh());
            if (vo.getAttention() == 0) {
                vo.setFlag(true);
                holder.imgCare.setImageResource(R.mipmap.img_share_off);
            }else {
                vo.setFlag(false);
                holder.imgCare.setImageResource(R.mipmap.img_share_on);
            }

            holder.imgCare.setOnClickListener(this);
            holder.imgCare.setTag(i);

            return view;
        }

        @Override
        public void onClick(View view) {
            callBack.click(view);
        }

        class ViewHolder {
            ImageView imgCare;
            TextView tvModule;
        }
    }

}
