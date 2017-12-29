package com.fskj.gaj.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class ChangePwdCommitVo {
    private String username;
    private String oldpassword;
    private String newpassword;


    public Map<String,String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", getUsername());
        map.put("oldpassword",getOldpassword());
        map.put("newpassword",getNewpassword());
        return map;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }
}
