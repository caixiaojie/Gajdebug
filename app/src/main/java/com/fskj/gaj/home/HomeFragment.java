package com.fskj.gaj.home;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fskj.gaj.ArticleSearchActivity;
import com.fskj.gaj.R;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.Util.TitleTypeSP;
import com.fskj.gaj.home.adapter.HomePagerAdapter;
import com.fskj.gaj.home.fragment.Common2Fragment;
import com.fskj.gaj.home.fragment.CommonFragment;
import com.fskj.gaj.home.fragment.DailyPoliceFragment;
import com.fskj.gaj.home.fragment.RecommandFragment;
import com.fskj.gaj.request.TypeListRequest;
import com.fskj.gaj.vo.TypeListResultVo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private ImageView imgSearch;

    public static HomeFragment getInstance( ){
        HomeFragment f =new HomeFragment();
        Bundle bundle=new Bundle();

        f.setArguments(bundle);

        return f;
    }

    private Activity activity;
    private List<String> enTitleList = new ArrayList<>();
    private List<String> zhTitleList = new ArrayList<>();
    private List<TypeListResultVo> typeList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private TypeListRequest typeListRequest;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HomePagerAdapter homePagerAdapter;

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
        View v=inflater.inflate(R.layout.fragment_home, null);
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        imgSearch = (ImageView) v.findViewById(R.id.img_search);
//声明请求变量和返回结果
        initRequest();
//初始化控件事件
        initWidgetEvent();
        typeListRequest.send();
        homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(),fragmentList,zhTitleList);
        viewPager.setAdapter(homePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT));
        return v;
    }

    //声明请求变量和返回结果
    private void initRequest(){
        //查文章类型
        typeListRequest = new TypeListRequest(activity, "", new ResultListInterface<TypeListResultVo>() {
            @Override
            public void success(ResultTVO<TypeListResultVo> data) {
                ArrayList<TypeListResultVo> typeListResultVos = data.getData();
                if (typeListResultVos != null && typeListResultVos.size() > 0) {
                    typeList.addAll(typeListResultVos);
                }


                for (int i = 0; i < typeList.size(); i++) {
                    TypeListResultVo vo = typeList.get(i);
                    zhTitleList.add(i,vo.getZh());
                    enTitleList.add(i,vo.getEn());
                }

                //第一项是不变的
                enTitleList.add(0,"");
                zhTitleList.add(0,"推荐");
                fragmentList.add(0, RecommandFragment.getInstance());
                //第二项是不变的
                enTitleList.add(1,"");
                zhTitleList.add(1,"每日警情");
                fragmentList.add(1, DailyPoliceFragment.getInstance());

                //将中文title存起来
                TitleTypeSP.saveENTitleStr(activity,enTitleList);
                TitleTypeSP.saveZHTitleStr(activity,zhTitleList);

                for (int i = 0; i < typeList.size(); i++) {
                    Fragment ff;
                    if(typeList.get(i).getType().equals("msg")){
                        ff=CommonFragment.getInstance(typeList.get(i).getEn());
                    }else{
                        ff = Common2Fragment.getInstance(typeList.get(i).getEn());
                    }
                    fragmentList.add(ff);
                }

                //更新适配器
                homePagerAdapter.notifyDataSetChanged();

            }

            @Override
            public void error(String errmsg) {
                Toast.makeText(activity,errmsg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化控件事件
    private void initWidgetEvent(){
        //
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArticleSearchActivity.gotoActivity(HomeFragment.this);
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

        }
    }


}
