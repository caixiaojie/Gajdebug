package com.fskj.gaj.profile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.AppConfig;
import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.Util.Tools;
import com.fskj.gaj.request.DelDutyRequest;
import com.fskj.gaj.request.ModifyDutyRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.view.MessageConfirmDialog;
import com.fskj.gaj.vo.DelDutyCommitVo;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.ModifyDutyCommitVo;

import java.util.Calendar;

public class ChangeDutyActivity extends AppCompatActivity {

    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private ModifyDutyCommitVo modifyDutyCommitVo;
    private ModifyDutyRequest modifyDutyRequest;
    private BusyView busyView;
    private DelDutyCommitVo delDutyCommitVo;
    private DelDutyRequest delDutyRequest;
    private AppCompatSpinner spinner;
    private String[] spinnerItems;
    private LinearLayout llSort;

    public static void gotoActivity(Activity activity ,String sid ,String name,String job,String phone,String sort){
        Intent intent=new Intent(activity,ChangeDutyActivity.class);
        intent.putExtra("sid",sid);
        intent.putExtra("name",name);
        intent.putExtra("job",job);
        intent.putExtra("phone",phone);
        intent.putExtra("sort",sort);
        activity.startActivityForResult(intent, AppConfig.CHANGE_DUTY);
        activity.overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),ChangeDutyActivity.class);

        fr.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
    }

    private Toolbar toolBar;
    private ImageView imgBack;
    private TextView tvDelete;
//    private LinearLayout llSelectDate;
//    private TextView tvDate;
    private EditText etName;
    private EditText etJob;
    private EditText etPhone;
    private LinearLayout llCommit;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_duty);
        activity=ChangeDutyActivity.this;
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
        tvDelete=(TextView)findViewById(R.id.tv_delete);
//        llSelectDate=(LinearLayout)findViewById(R.id.llSelectDate);
//        tvDate=(TextView)findViewById(R.id.tv_date);
        etName=(EditText)findViewById(R.id.et_name);
        etJob=(EditText)findViewById(R.id.et_job);
        etPhone=(EditText)findViewById(R.id.et_phone);
        llCommit=(LinearLayout)findViewById(R.id.llCommit);
        spinner = (AppCompatSpinner) findViewById(R.id.spinner);
        llSort = (LinearLayout) findViewById(R.id.llSort);
        initSpinner();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
//        tvDate.setText(year +"-"+(++month)+"-"+ day);

//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
//传入参数
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){
            String sid = bundle.getString("sid");
            String name = bundle.getString("name");
            String job = bundle.getString("job");
            String phone = bundle.getString("phone");
            String sort = bundle.getString("sort","0");
//            tvDate.setText(date);
            etName.setText(name);
            etJob.setText(job);
            etPhone.setText(phone);
            modifyDutyCommitVo.setSid(sid);
            delDutyCommitVo.setSid(sid);
            for (int i = 0; i < spinnerItems.length; i++) {
                if (sort.equals(spinnerItems[i])) {
                    spinner.setSelection(i);
                    modifyDutyCommitVo.setSort(spinnerItems[i]);
                }
            }
        }

    }

    private void initSpinner() {
        spinnerItems = new String[]{"0","1","2","3","4","5","6","7","8","9","10"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity,R.layout.simple_list_item, spinnerItems);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
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
        //修改值班记录请求
        modifyDutyCommitVo = new ModifyDutyCommitVo();
        if (loginInfo != null) {
            modifyDutyCommitVo.setUsername(loginInfo.getUsername());
            modifyDutyCommitVo.setPassword(loginInfo.getPassword());
        }
        modifyDutyRequest = new ModifyDutyRequest(activity, modifyDutyCommitVo, new ResultObjInterface<String>() {
            @Override
            public void success(ResultVO<String> data) {
                busyView.dismiss();
                ChangeDutyActivity.this.setResult(RESULT_OK);
                finish();
            }

            @Override
            public void error(String errmsg) {
                busyView.dismiss();
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });

        //删除值班记录
        delDutyCommitVo = new DelDutyCommitVo();
        if (loginInfo != null) {
            delDutyCommitVo.setUsername(loginInfo.getUsername());
            delDutyCommitVo.setPassword(loginInfo.getPassword());
        }
        delDutyRequest = new DelDutyRequest(activity, delDutyCommitVo, new ResultObjInterface<String>() {
            @Override
            public void success(ResultVO<String> data) {
                busyView.dismiss();
                ChangeDutyActivity.this.setResult(RESULT_OK);
                finish();
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
        llSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

      //排序
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                modifyDutyCommitVo.setSort(spinnerItems[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //修改值班记录
        llCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName = Tools.safeString(etName);
                if (strName.equals("")) {
                    Toast.makeText(activity,"请输入值班人员姓名",Toast.LENGTH_SHORT).show();
                    return;
                }
                String strJob = Tools.safeString(etJob);
                if (strJob.equals("")) {
                    Toast.makeText(activity,"请输入值班人员职位",Toast.LENGTH_SHORT).show();
                    return;
                }
                String strPhone = Tools.safeString(etPhone);
                if (strPhone.equals("")) {
                    Toast.makeText(activity,"请输入值班人员电话",Toast.LENGTH_SHORT).show();
                    return;
                }
                LoginCommitVo loginInfo = LoginInfo.getLoginCommitInfo(activity);

                //封装实体
                modifyDutyCommitVo.setUsername(loginInfo.getUsername());
                modifyDutyCommitVo.setPassword(loginInfo.getPassword());
                modifyDutyCommitVo.setName(strName);
                modifyDutyCommitVo.setJob(strJob);
                modifyDutyCommitVo.setPhone(strPhone);
                busyView = BusyView.showCommit(activity);
                modifyDutyRequest.send();
            }
        });

        //删除值班记录
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageConfirmDialog.show(activity, "提示", "确认删除值班记录吗?", "取消", "确认", new MessageConfirmDialog.OnConfirmClickListener() {
                    @Override
                    public void onLeft() {

                    }

                    @Override
                    public void onRight() {
                        busyView = BusyView.showText(activity,"正在删除");
                        delDutyRequest.send();
                    }
                },true);
            }
        });
    }

   /* public void showDateDialog() {
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                tvDate.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
            }
        };

        DatePickerDialog dialog=new DatePickerDialog(activity, 0,listener, year, calendar.get(Calendar.MONTH), day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show();
    }*/

    private void showMsg(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
