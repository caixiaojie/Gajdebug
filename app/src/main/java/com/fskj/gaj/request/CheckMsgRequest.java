package com.fskj.gaj.request;

import android.content.Context;
import android.util.Log;

import com.fskj.gaj.BuildConfig;
import com.fskj.gaj.Remote.BaseObjRequest;
import com.fskj.gaj.Remote.HttpUtils;
import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.vo.CheckMsgCommitVo;
import com.fskj.gaj.vo.LoginResultVo;
import com.fskj.gaj.vo.PubNoticeCommitVo;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

/**
 * 提交审核
 * Created by Administrator on 2017/12/13 0013.
 */

public class CheckMsgRequest extends BaseObjRequest<CheckMsgCommitVo,String>{
    public CheckMsgRequest(Context activeContext, CheckMsgCommitVo requestData, ResultObjInterface<String> listener) {
        super(activeContext, requestData, listener);
    }

    @Override
    protected ResultVO<String> Query_Process() throws IOException, Exception {
        String url = BuildConfig.SERVER_IP + "/mobile/checkmsg.do";
        String jsonStr = HttpUtils.post(url,requestData.toMap());
//        Log.e("CheckMsgRequest",jsonStr);
        ResultVO<String> vo = gson.fromJson(jsonStr,new TypeToken<ResultVO<String>>(){}.getType());
        return vo;
    }
}
