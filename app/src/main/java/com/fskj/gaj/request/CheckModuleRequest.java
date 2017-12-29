package com.fskj.gaj.request;

import android.content.Context;
import android.util.Log;

import com.fskj.gaj.BuildConfig;
import com.fskj.gaj.Remote.BaseListRequest;
import com.fskj.gaj.Remote.HttpUtils;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.vo.CheckModuleResultVo;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.MsgListResultVo;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class CheckModuleRequest extends BaseListRequest<LoginCommitVo,CheckModuleResultVo> {
    public CheckModuleRequest(Context activeContext, LoginCommitVo requestData, ResultListInterface<CheckModuleResultVo> listener) {
        super(activeContext, requestData, listener);
    }

    @Override
    protected ResultTVO<CheckModuleResultVo> Query_Process() throws IOException, Exception {
        String url = BuildConfig.SERVER_IP + "/mobile/checkmodule.do";
        String jsonStr = HttpUtils.post(url,requestData.toMap());
//        Log.e("MsgListRequest",jsonStr);
        ResultTVO<CheckModuleResultVo> vo = gson.fromJson(jsonStr,new TypeToken<ResultTVO<CheckModuleResultVo>>(){}.getType());
        return vo;
    }
}
