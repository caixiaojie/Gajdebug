package com.fskj.gaj.system;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.request.SystemlistRequest;
import com.fskj.gaj.vo.SystemlistResultVo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomFragment extends Fragment {


    private ListView listView;
    private ArrayAdapter<SystemlistResultVo> adapter;
    private List<SystemlistResultVo> systemList = new ArrayList<>();

    private SystemlistRequest systemlistRequest;

    public static RoomFragment getInstance( ){
        RoomFragment f =new RoomFragment();
        Bundle bundle=new Bundle();

        f.setArguments(bundle);

        return f;
    }

    private Activity activity;

    private Toolbar toolBar;
    private LinearLayout llSelectDate;
    private TextView tvDate;
    private LinearLayout llRoomItem;
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
        View v=inflater.inflate(R.layout.fragment_room, null);
        toolBar=(Toolbar)v.findViewById(R.id.toolBar);
//        llSelectDate=(LinearLayout)v.findViewById(R.id.llSelectDate);
//        tvDate=(TextView)v.findViewById(R.id.tv_date);
//        llRoomItem=(LinearLayout)v.findViewById(R.id.llRoomItem);
        listView = (ListView) v.findViewById(R.id.listview);
        adapter = new ArrayAdapter<SystemlistResultVo>(activity,android.R.layout.simple_list_item_1,systemList);
        listView.setAdapter(adapter);
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
        systemlistRequest.send();
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT));
        return v;
    }

    //声明请求变量和返回结果
    private void initRequest(){
        systemlistRequest = new SystemlistRequest(activity, "", new ResultListInterface<SystemlistResultVo>() {
            @Override
            public void success(ResultTVO<SystemlistResultVo> data) {
                ArrayList<SystemlistResultVo> resultVos = data.getData();

                if (resultVos != null && resultVos.size() > 0) {
                    systemList.addAll(resultVos);
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
        //选择日期
       /* llSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });*/

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               SystemlistResultVo vo = systemList.get(i);
               if (vo.getUrl() != null && vo.getUrl().equals("") == false) {
                   String url = vo.getUrl();
                   if (url.startsWith("http://") == false) {
                       url = "http://" + url;
                   }
                   try {
                       Intent intent= new Intent();
                       intent.setAction("android.intent.action.VIEW");
                       Uri content_url = Uri.parse(url);
                       intent.setData(content_url);
                       startActivity(intent);
                   }catch (Exception e) {
                       e.printStackTrace();
                        Toast.makeText(activity,"没有系统浏览器",Toast.LENGTH_SHORT).show();
                   }
               }else {
                   Toast.makeText(activity,"还没有配置url",Toast.LENGTH_SHORT).show();
               }
           }
       });
    }

    public void showDateDialog() {
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                tvDate.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
            }
        };
        DatePickerDialog dialog=new DatePickerDialog(activity, 0,listener,2017,8,18);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show();
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



}
