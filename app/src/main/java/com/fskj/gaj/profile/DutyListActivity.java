package com.fskj.gaj.profile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.AppConfig;
import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.request.DutyRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.view.NoDataView;
import com.fskj.gaj.vo.DutyResultVo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DutyListActivity extends AppCompatActivity {

    private int year;
    private int month;
    private int day;
    private Calendar calendar;
    private DutyRequest dutyRequest;
    private List<DutyResultVo> dutyList = new ArrayList<>();
    private DutyListAdapter adapter;
    private BusyView busyView;
    private NoDataView noDataView;

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,DutyListActivity.class);

        activity.startActivity(intent);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),DutyListActivity.class);

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
    private LinearLayout llSelectDate;
    private TextView tvDate;
    private TextView tvAddDuty;
    private ListView listview;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_list);
        activity=DutyListActivity.this;
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
        llSelectDate=(LinearLayout)findViewById(R.id.llSelectDate);
        noDataView = (NoDataView) findViewById(R.id.no_data);
        tvDate=(TextView)findViewById(R.id.tv_date);
        tvAddDuty=(TextView)findViewById(R.id.tv_add_duty);
        if (LoginInfo.getIduty(activity) == 1) {
            tvAddDuty.setVisibility(View.VISIBLE);
        }else {
            tvAddDuty.setVisibility(View.GONE);
        }
        listview=(ListView)findViewById(R.id.listview);
        listview.setEmptyView(noDataView);
        adapter = new DutyListAdapter();
        listview.setAdapter(adapter);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
        tvDate.setText(year +"-"+(++month)+"-"+ day);
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){

        }
        busyView = BusyView.showQuery(activity);
        dutyRequest.setDate(Tools.safeString(tvDate));
        dutyRequest.send();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode) {
                case AppConfig.ADD_DUTY:
                    busyView = BusyView.showQuery(activity);
                    dutyRequest.send();
                    break;
                case AppConfig.CHANGE_DUTY:
                    busyView = BusyView.showQuery(activity);
                    dutyRequest.send();
                    break;
            }
        }
    }

    //声明请求变量和返回结果
    private void initRequest(){
        dutyRequest = new DutyRequest(activity, "", new ResultListInterface<DutyResultVo>() {
            @Override
            public void success(ResultTVO<DutyResultVo> data) {
                busyView.dismiss();
                ArrayList<DutyResultVo> dutyResultVos = data.getData();
                dutyList.clear();
                if (dutyResultVos != null && dutyResultVos.size() > 0) {
                    dutyList.addAll(dutyResultVos);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(String errmsg) {
                busyView.dismiss();
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化控件事件
    private void initWidgetEvent(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DutyResultVo vo = dutyList.get(i);
                if (LoginInfo.getIduty(activity) == 1) {//可以修改值班记录
                    ChangeDutyActivity.gotoActivity(activity, vo.getSid(),vo.getName(),vo.getJob(),vo.getPhone(),vo.getSort());
                }else {
                    Toast.makeText(activity,"权限不足",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //新增值班记录
        tvAddDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginInfo.getIduty(activity) == 1) {
                    AddDutyActivity.gotoActivity(activity);
                }else {
                    Toast.makeText(activity,"权限不足",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //选择日期
        llSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
    }

    public void showDateDialog() {
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                tvDate.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                busyView = BusyView.showQuery(activity);
                dutyRequest.setDate(Tools.safeString(tvDate));
                dutyRequest.send();
            }
        };

        DatePickerDialog dialog=new DatePickerDialog(activity, 0,listener,year,calendar.get(Calendar.MONTH),day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show();
    }

    private void showMsg(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    class DutyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dutyList.size() == 0 ? 0 : dutyList.size();
        }

        @Override
        public Object getItem(int i) {
            return dutyList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(activity).inflate(R.layout.item_duty_list,viewGroup,false);
                holder = new ViewHolder();
                holder.imgEdit = (ImageView) view.findViewById(R.id.img_duty_edit);
                holder.tvName = (TextView) view.findViewById(R.id.tv_user_name);
                holder.tvJob = (TextView) view.findViewById(R.id.tv_zhicheng);
                holder.tvPhone = (TextView) view.findViewById(R.id.tv_phone);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }
            final DutyResultVo vo = dutyList.get(i);
            holder.tvName.setText(vo.getName());
            holder.tvJob.setText(vo.getJob());
            holder.tvPhone.setText(vo.getPhone());

            return view;
        }
        class ViewHolder {
            TextView tvName,tvJob,tvPhone;
            ImageView imgEdit;
        }
    }
}
