package com.fskj.gaj.duty;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.request.DutyRequest;
import com.fskj.gaj.vo.DutyResultVo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DutyFragment extends Fragment {


    private RecyclerView recyclerView;
    private DutyRequest dutyRequest;

    public static DutyFragment getInstance( ){
        DutyFragment f =new DutyFragment();
        Bundle bundle=new Bundle();

        f.setArguments(bundle);

        return f;
    }

    private Activity activity;
    private List<DutyResultVo> dutyList = new ArrayList<>();
    private DutyAdapter adapter;

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
        View v=inflater.inflate(R.layout.fragment_duty, null);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
//声明请求变量和返回结果
        initRequest();
        adapter = new DutyAdapter();
        recyclerView.setAdapter(adapter);
//初始化控件事件
        initWidgetEvent();
        dutyRequest.send();
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT));
        return v;
    }

    //声明请求变量和返回结果
    private void initRequest(){
        dutyRequest = new DutyRequest(activity, "", new ResultListInterface<DutyResultVo>() {
            @Override
            public void success(ResultTVO<DutyResultVo> data) {
                ArrayList<DutyResultVo> dutyResultVos = data.getData();
                if (dutyResultVos != null && dutyResultVos.size() > 0) {
                    dutyList.addAll(dutyResultVos);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(String errmsg) {
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){

        }
    }

    class DutyAdapter extends RecyclerView.Adapter<DutyAdapter.DutyViewHolder> {

        @Override
        public DutyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_duty_layout,parent,false);

            return new DutyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DutyViewHolder holder, final int position) {
            holder.tvTitle.setText(dutyList.get(position).getJob());
            holder.tvName.setText(dutyList.get(position).getName());
            holder.tvPhone.setText(dutyList.get(position).getPhone());
            //打电话
            holder.imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+dutyList.get(position).getPhone()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dutyList.size();
        }

        class DutyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle,tvName,tvPhone;
            ImageView imgCall;
            public DutyViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
                tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
                imgCall = (ImageView) itemView.findViewById(R.id.img_call);
            }
        }
    }

}
