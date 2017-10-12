package com.fskj.gaj.notice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fskj.gaj.OnItemClickListener;
import com.fskj.gaj.R;
import com.fskj.gaj.home.fragment.CommonFragment;
import com.fskj.gaj.vo.MsgListResultVo;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class CommonMAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context context;
    private List<MsgListResultVo> msgList;


    private OnItemClickListener mOnItemClickListener = null;


    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public CommonMAdapter(Context context, List<MsgListResultVo> msgList) {
        this.context = context;
        this.msgList = msgList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_common_layout, parent, false);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return new CommonViewHoler(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MsgListResultVo vo = msgList.get(position);
        ((CommonViewHoler)holder).tvTitle.setText(vo.getTitle());
        ((CommonViewHoler)holder).tvTime.setText(vo.getCreatetime());
        ((CommonViewHoler)holder).tvVisit.setText(vo.getVisit()+"");
        ((CommonViewHoler)holder).itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }
    class CommonViewHoler extends RecyclerView.ViewHolder{
        TextView tvTitle,tvTime,tvVisit;
        ImageView img;
        public CommonViewHoler(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvVisit = (TextView) itemView.findViewById(R.id.tv_count);
        }
    }
}
