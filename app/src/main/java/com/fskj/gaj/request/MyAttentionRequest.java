package com.fskj.gaj.request;

import android.content.Context;
import android.util.Log;

import com.fskj.gaj.BuildConfig;
import com.fskj.gaj.Remote.BaseListRequest;
import com.fskj.gaj.Remote.HttpUtils;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.MsgListCommitVo;
import com.fskj.gaj.vo.MsgListResultVo;
import com.fskj.gaj.vo.MyAttentionResultVo;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class MyAttentionRequest extends BaseListRequest<LoginCommitVo,MyAttentionResultVo> {
    public MyAttentionRequest(Context activeContext, LoginCommitVo requestData, ResultListInterface<MyAttentionResultVo> listener) {
        super(activeContext, requestData, listener);
    }

    @Override
    protected ResultTVO<MyAttentionResultVo> Query_Process() throws IOException, Exception {
        String url = BuildConfig.SERVER_IP + "/mobile/myattention.do?";
        String jsonStr = HttpUtils.post(url,requestData.toMap());
//        Log.e("MyAttentionRequest",jsonStr);
        ResultTVO<MyAttentionResultVo> vo = gson.fromJson(jsonStr,new TypeToken<ResultTVO<MyAttentionResultVo>>(){}.getType());
        return vo;
    }
}
