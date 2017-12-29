package com.fskj.gaj.request;

import android.content.Context;
import android.util.Log;

import com.fskj.gaj.BuildConfig;
import com.fskj.gaj.Remote.BaseListRequest;
import com.fskj.gaj.Remote.HttpUtils;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.vo.MsgListCommitVo;
import com.fskj.gaj.vo.MsgListResultVo;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class MsgListRequest extends BaseListRequest<MsgListCommitVo,MsgListResultVo> {
    public MsgListRequest(Context activeContext, MsgListCommitVo requestData, ResultListInterface<MsgListResultVo> listener) {
        super(activeContext, requestData, listener);
    }

    @Override
    protected ResultTVO<MsgListResultVo> Query_Process() throws IOException, Exception {
        String url = BuildConfig.SERVER_IP + "/mobile/msglist.do?"+requestData.toString();
        String jsonStr = HttpUtils.get(url);
//        Log.e("MsgListRequest",jsonStr);
        ResultTVO<MsgListResultVo> vo = gson.fromJson(jsonStr,new TypeToken<ResultTVO<MsgListResultVo>>(){}.getType());
        return vo;
    }
}
