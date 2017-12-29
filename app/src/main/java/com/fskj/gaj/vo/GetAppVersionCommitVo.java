package com.fskj.gaj.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class GetAppVersionCommitVo {
    private String vid;

    public Map<String,String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("vid",getVid());
        return map;
    }
    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
