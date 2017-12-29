package com.fskj.gaj.request;

import android.content.Context;
import android.util.Log;

import com.fskj.gaj.BuildConfig;
import com.fskj.gaj.Remote.BaseObjRequest;
import com.fskj.gaj.Remote.HttpUtils;
import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.vo.AddDutyCommitVo;
import com.fskj.gaj.vo.ChangePwdCommitVo;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class ChangePwdRequest extends BaseObjRequest<ChangePwdCommitVo,String> {
    public ChangePwdRequest(Context activeContext, ChangePwdCommitVo requestData, ResultObjInterface<String> listener) {
        super(activeContext, requestData, listener);
    }

    @Override
    protected ResultVO<String> Query_Process() throws IOException, Exception {
        String url = BuildConfig.SERVER_IP + "/mobile/changepwd.do";
        String jsonStr = HttpUtils.post(url,requestData.toMap());
//        Log.e("ChangePwdRequest",jsonStr);
        ResultVO<String> vo = gson.fromJson(jsonStr,new TypeToken<ResultVO<String>>(){}.getType());
        return vo;
    }
}
