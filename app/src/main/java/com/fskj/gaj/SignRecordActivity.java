package com.fskj.gaj;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.request.SignListRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.vo.SignListCommitVo;
import com.fskj.gaj.vo.SignListResultVo;

import java.util.ArrayList;
import java.util.List;


public class SignRecordActivity extends AppCompatActivity {

    private SignListCommitVo signListCommitVo;
    private SignListRequest signListRequest;
    private BusyView busyView;
    private LinearLayout llNoData;

    public static void gotoActivity(Activity activity, String id){
        Intent intent=new Intent(activity,SignRecordActivity.class);
        intent.putExtra("id",id);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),SignRecordActivity.class);

        fr.startActivity(intent);
    }
    private SignRecordAdapter adapter;
    private List<SignListResultVo> signList = new ArrayList<>();
    private Toolbar toolBar;
    private ImageView imgBack;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_record);
        activity=SignRecordActivity.this;

        inflater = LayoutInflater.from(activity);


//界面初始化
        toolBar=(Toolbar)findViewById(R.id.toolBar);
        imgBack=(ImageView)findViewById(R.id.img_back);
        tvTitle=(TextView)findViewById(R.id.tv_title);
        llNoData = (LinearLayout) findViewById(R.id.no_data);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        llNoData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
//声明请求变量和返回结果
        initRequest();
        adapter = new SignRecordAdapter();
        recyclerView.setAdapter(adapter);
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){
            String id = bundle.getString("id", "");
            signListCommitVo.setMid(id);
        }
        busyView = BusyView.showQuery(activity);
        signListRequest.send();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

        }
    }

    //声明请求变量和返回结果
    private void initRequest(){
        signListCommitVo = new SignListCommitVo();
        signListRequest = new SignListRequest(activity, signListCommitVo, new ResultListInterface<SignListResultVo>() {
            @Override
            public void success(ResultTVO<SignListResultVo> data) {
                busyView.dismiss();
                ArrayList<SignListResultVo> signListResultVos = data.getData();
                if (signListResultVos != null && signListResultVos.size() > 0) {
                    signList.addAll(signListResultVos);
                }
                adapter.notifyDataSetChanged();

                if (signList.size() > 0) {
                    llNoData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }else {
                    llNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void error(String errmsg) {
                busyView.dismiss();
                if (signList.size() > 0) {
                    llNoData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }else {
                    llNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化控件事件
    private void initWidgetEvent(){
        //
        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    class SignRecordAdapter extends RecyclerView.Adapter<SignRecordAdapter.SignViewHolder> {
        @Override
        public SignViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_signrecord_layout,parent,false);
            return new SignViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SignViewHolder holder, int position) {
            SignListResultVo vo = signList.get(position);
            holder.tvPerson.setText(vo.getName());
            holder.tvTime.setText(vo.getTime());
            holder.tvDepart.setText(vo.getDepart());
        }

        @Override
        public int getItemCount() {
            return signList.size();
        }

        class SignViewHolder extends RecyclerView.ViewHolder {
            TextView tvPerson,tvTime,tvDepart;
            public SignViewHolder(View itemView) {
                super(itemView);
                tvPerson = (TextView) itemView.findViewById(R.id.tv_person);
                tvTime = (TextView) itemView.findViewById(R.id.tv_time);
                tvDepart = (TextView) itemView.findViewById(R.id.tv_depart);
            }
        }
    }
}
