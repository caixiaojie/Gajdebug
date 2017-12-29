package com.fskj.gaj.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class LoginCommitVo {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Map<String,String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", getUsername());
        map.put("password",getPassword());
        return map;
    }

}
