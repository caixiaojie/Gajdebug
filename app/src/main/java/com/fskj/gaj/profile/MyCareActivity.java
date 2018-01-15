package com.fskj.gaj.profile;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.AppConfig;
import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.Util.StatusBarUtil;
import com.fskj.gaj.home.adapter.HomePagerAdapter;
import com.fskj.gaj.home.fragment.Common2Fragment;
import com.fskj.gaj.home.fragment.CommonFragment;
import com.fskj.gaj.home.fragment.RecommandFragment;
import com.fskj.gaj.request.MyAttentionRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.view.NoDataView;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.MyAttentionResultVo;
import com.fskj.gaj.vo.TypeListResultVo;

import java.util.ArrayList;
import java.util.List;

public class MyCareActivity extends AppCompatActivity {

    private LoginCommitVo myAttentionCommitVo;
    private MyAttentionRequest myAttentionRequest;
    private NoDataView noDataView;
    private BusyView busyView;
    private LoginCommitVo loginCommitVo;
    private List<MyAttentionResultVo> attentionList = new ArrayList<>();
    private HomePagerAdapter homePagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> zhTitleList = new ArrayList<>();
    private int curTab = 0;

    public static void gotoActivity(Activity activity ){
        Intent intent=new Intent(activity,MyCareActivity.class);

        activity.startActivity(intent);
    }

    public static void gotoActivity(Fragment fr ){
        Intent intent=new Intent(fr.getActivity(),MyCareActivity.class);

        fr.startActivityForResult(intent, AppConfig.MY_CARE);
        fr.getActivity().overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyCareActivity.this.setResult(RESULT_OK);
        finish();
    }

    private Toolbar toolBar;
    private ImageView imgBack;
    private TextView tvSet;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LayoutInflater inflater;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_care);
        activity=MyCareActivity.this;
        StatusBarUtil.setColor(activity,getResources().getColor(R.color.main_color),0);
        inflater = LayoutInflater.from(activity);


//界面初始化
        toolBar=(Toolbar)findViewById(R.id.toolBar);
        imgBack=(ImageView)findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCareActivity.this.setResult(RESULT_OK);
                finish();
            }
        });
        tvSet=(TextView)findViewById(R.id.tv_set);
        noDataView = (NoDataView) findViewById(R.id.no_data);
        noDataView.setText("暂无关注栏目");
        noDataView.setVisibility(View.GONE);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);


        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(),fragmentList,zhTitleList);
        viewPager.setAdapter(homePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(curTab);




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

            attentionList.clear();
            zhTitleList.clear();
            fragmentList.clear();

            homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(),fragmentList,zhTitleList);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setAdapter(homePagerAdapter);
            viewPager.setCurrentItem(curTab);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    Fragment fragment = homePagerAdapter.getItem(position);
                    if (fragment instanceof  CommonFragment) {
                        ((CommonFragment) fragment).sendMessage();
                    }else if (fragment instanceof  Common2Fragment) {
                        ((Common2Fragment) fragment).sendMessage();
                    }else if (fragment instanceof  RecommandFragment){
                        ((RecommandFragment) fragment).sendMessage();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            myAttentionRequest.send();

        }
    }

    //声明请求变量和返回结果
    private void initRequest(){
        LoginCommitVo loginInfo = LoginInfo.getLoginCommitInfo(activity);
        loginCommitVo = new LoginCommitVo();
        if (loginInfo != null ) {
            loginCommitVo.setUsername(loginInfo.getUsername());
            loginCommitVo.setPassword(loginInfo.getPassword());
        }
        myAttentionRequest = new MyAttentionRequest(activity, loginCommitVo, new ResultListInterface<MyAttentionResultVo>() {
            @Override
            public void success(ResultTVO<MyAttentionResultVo> data) {
                busyView.dismiss();
                ArrayList<MyAttentionResultVo> resultVos = data.getData();
                attentionList.clear();
                zhTitleList.clear();
                fragmentList.clear();

                if (resultVos != null && resultVos.size() > 0) {
                    attentionList.addAll(resultVos);
                }
                if (attentionList.size() > 0) {
                    for (int i = 0; i < attentionList.size(); i++) {
                        MyAttentionResultVo vo = attentionList.get(i);
                        int j = 0;
                        if (vo.getAttention() == 1) {
                            zhTitleList.add(vo.getZh());
                            Fragment ff;
                            if (vo.getType().equals("msg")) {
                                ff = CommonFragment.getInstance(vo.getEn(),j);
                                ((CommonFragment)ff).setTabPos(j);
                            } else if (vo.getType().equals("notice")) {
                                ff = Common2Fragment.getInstance(vo.getEn(),j);
                                ((Common2Fragment)ff).setTabPos(j);
                            } else {
                                ff = RecommandFragment.getInstance(j);
                                ((RecommandFragment)ff).setTabPos(j);
                            }
                            fragmentList.add(ff);
                            j++;
                        }
                    }
                }
                homePagerAdapter.notifyDataSetChanged();
                if (fragmentList.size() == 0) {
                    noDataView.setVisibility(View.VISIBLE);
                }else {
                    noDataView.setVisibility(View.GONE);
                }
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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = homePagerAdapter.getItem(position);
                if (fragment instanceof  CommonFragment) {
                    ((CommonFragment) fragment).sendMessage();
                }else if (fragment instanceof  Common2Fragment) {
                    ((Common2Fragment) fragment).sendMessage();
                }else if (fragment instanceof  RecommandFragment){
                    ((RecommandFragment) fragment).sendMessage();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //关注设置
        tvSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CareSetActivity.gotoActivity(activity);
//                finish();
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


}
