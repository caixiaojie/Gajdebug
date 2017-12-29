package com.fskj.gaj.request;

import android.content.Context;
import android.util.Log;

import com.fskj.gaj.BuildConfig;
import com.fskj.gaj.Remote.BaseObjRequest;
import com.fskj.gaj.Remote.HttpUtils;
import com.fskj.gaj.Remote.ResultObjInterface;
import com.fskj.gaj.Remote.ResultVO;
import com.fskj.gaj.vo.LoginCommitVo;
import com.fskj.gaj.vo.LoginResultVo;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class LoginRequest extends BaseObjRequest<LoginCommitVo,LoginResultVo> {
    public LoginRequest(Context activeContext, LoginCommitVo requestData, ResultObjInterface<LoginResultVo> listener) {
        super(activeContext, requestData, listener);
    }

    @Override
    protected ResultVO<LoginResultVo> Query_Process() throws IOException, Exception {
        String url = BuildConfig.SERVER_IP + "/mobile/login.do";
        String jsonStr = HttpUtils.post(url,requestData.toMap());
//        Log.e("LoginRequest",jsonStr);
        ResultVO<LoginResultVo> vo = gson.fromJson(jsonStr,new TypeToken<ResultVO<LoginResultVo>>(){}.getType());
        return vo;
    }
}
