package com.fskj.gaj.vo;

import com.fskj.gaj.Util.PageQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class NotCheckListCommitVo extends PageQuery{
    private String username;
    private String type;
    private String password;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Map<String,String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("page",page+"");
        map.put("pagesize",pagesize+"");
        map.put("username", getUsername());
        map.put("password", getPassword());
        map.put("type",getType());
        return map;
    }
}
