package com.fskj.gaj.attention;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fskj.gaj.LoginInfo;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.home.adapter.HomePagerAdapter;
import com.fskj.gaj.home.fragment.Common2Fragment;
import com.fskj.gaj.home.fragment.CommonFragment;
import com.fskj.gaj.home.fragment.RecommandFragment;
import com.fskj.gaj.profile.CareSetActivity;
import com.fskj.gaj.request.MyAttentionRequest;
import com.fskj.gaj.view.BusyView;
import com.fskj.gaj.view.NoDataView;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.MyAttentionResultVo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttentionFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HomePagerAdapter homePagerAdapter;
    private int curTab = 0;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> zhTitleList = new ArrayList<>();
    private List<MyAttentionResultVo> attentionList = new ArrayList<>();
    private LoginCommitVo loginCommitVo;
    private MyAttentionRequest myAttentionRequest;
    private BusyView busyView;

    public static AttentionFragment getInstance( ){
        AttentionFragment f =new AttentionFragment();
        Bundle bundle=new Bundle();

        f.setArguments(bundle);

        return f;
    }

    private Activity activity;

    private Toolbar toolBar;
    private TextView tvSet;
    private NoDataView noDataView;
    int j = 0;
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
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh".equals(msg)){
                    refresh();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
    }

    private void refresh() {
        attentionList.clear();
        zhTitleList.clear();
        fragmentList.clear();

        homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(),fragmentList,zhTitleList);
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
                if (fragment instanceof CommonFragment) {
                    ((CommonFragment) fragment).sendMessage();
                }else if (fragment instanceof Common2Fragment) {
                    ((Common2Fragment) fragment).sendMessage();
                }else if (fragment instanceof RecommandFragment){
                    ((RecommandFragment) fragment).sendMessage();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        myAttentionRequest.send();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity=this.getActivity();


//界面初始化
        View v=inflater.inflate(R.layout.fragment_attention, null);
        toolBar=(Toolbar)v.findViewById(R.id.toolBar);
        tvSet=(TextView)v.findViewById(R.id.tv_set);
        noDataView=(NoDataView)v.findViewById(R.id.no_data);

        noDataView.setText("暂无关注栏目");
        noDataView.setVisibility(View.GONE);
        tabLayout =(TabLayout)v.findViewById(R.id.tabLayout);
        viewPager =(ViewPager)v.findViewById(R.id.viewPager);


        homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(),fragmentList,zhTitleList);
        viewPager.setAdapter(homePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(curTab);

//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT));
        busyView = BusyView.showQuery(activity);
        myAttentionRequest.send();
        return v;
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
                CareSetActivity.gotoActivity(AttentionFragment.this);
            }
        });
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
            Log.e("====","------");

        }
    }


}
