package com.fskj.gaj.request;

import android.content.Context;
import android.util.Log;

import com.fskj.gaj.BuildConfig;
import com.fskj.gaj.Remote.BaseListRequest;
import com.fskj.gaj.Remote.HttpUtils;
import com.fskj.gaj.Remote.ResultListInterface;
import com.fskj.gaj.Remote.ResultTVO;
import com.fskj.gaj.vo.MsgListResultVo;
import com.fskj.gaj.vo.NotCheckListCommitVo;
import com.fskj.gaj.vo.NotCheckListResultVo;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class NotCheckListRequest extends BaseListRequest<NotCheckListCommitVo,NotCheckListResultVo> {
    public NotCheckListRequest(Context activeContext, NotCheckListCommitVo requestData, ResultListInterface<NotCheckListResultVo> listener) {
        super(activeContext, requestData, listener);
    }

    @Override
    protected ResultTVO<NotCheckListResultVo> Query_Process() throws IOException, Exception {
        String url = BuildConfig.SERVER_IP + "/mobile/notchecklist.do?";
        String jsonStr = HttpUtils.post(url,requestData.toMap());
//        Log.e("NotCheckListRequest",jsonStr);
//        Log.e("NotCheckListRequest",requestData.toMap().toString());
        ResultTVO<NotCheckListResultVo> vo = gson.fromJson(jsonStr,new TypeToken<ResultTVO<NotCheckListResultVo>>(){}.getType());
        return vo;
    }
}
