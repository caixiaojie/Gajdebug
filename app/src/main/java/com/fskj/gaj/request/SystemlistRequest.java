package com.fskj.gaj.request;

import android.content.Context;
import android.util.Log;

import com.fskj.gaj.BuildConfig;
import com.fskj.gaj.Remote.BaseListRequest;
import com.fskj.gaj.Remote.HttpUtils;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.vo.SystemlistResultVo;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

/**
 * Created by Administrator on 2017/9/29 0029.
 */

public class SystemlistRequest extends BaseListRequest<String,SystemlistResultVo> {
    public SystemlistRequest(Context activeContext, String requestData, ResultListInterface<SystemlistResultVo> listener) {
        super(activeContext, requestData, listener);
    }

    @Override
    protected ResultTVO<SystemlistResultVo> Query_Process() throws IOException, Exception {
        String url = BuildConfig.SERVER_IP + "/mobile/systemlist.do";
        String jsonStr = HttpUtils.get(url);
        Log.e("SystemlistRequest",jsonStr);
        ResultTVO<SystemlistResultVo> vo = gson.fromJson(jsonStr,new TypeToken<ResultTVO<SystemlistResultVo>>(){}.getType());
        return vo;
    }
}
