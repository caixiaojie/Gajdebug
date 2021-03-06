package com.fskj.gaj.request;

import android.content.Context;
import android.util.Log;

import com.fskj.gaj.BuildConfig;
import com.fskj.gaj.Remote.BaseListRequest;
import com.fskj.gaj.Remote.HttpUtils;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.vo.RuleListCommitVo;
import com.fskj.gaj.vo.RuleListResultVo;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class RuleListRequest extends BaseListRequest<RuleListCommitVo,RuleListResultVo> {
    public RuleListRequest(Context activeContext, RuleListCommitVo requestData, ResultListInterface<RuleListResultVo> listener) {
        super(activeContext, requestData, listener);
    }

    @Override
    protected ResultTVO<RuleListResultVo> Query_Process() throws IOException, Exception {
        String url = BuildConfig.SERVER_IP + "/mobile/rulelist.do?"+requestData.toString();
        String jsonStr = HttpUtils.get(url);
//        Log.e("RuleListRequest",jsonStr);
        ResultTVO<RuleListResultVo> vo = gson.fromJson(jsonStr,new TypeToken<ResultTVO<RuleListResultVo>>(){}.getType());
        return vo;
    }
}
