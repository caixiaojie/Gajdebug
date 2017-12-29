package com.fskj.gaj.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class ModifyDutyCommitVo {
    /**
     * username  用户名
     password  密码
     name  称谓
     job  职位
     phone  联系电话
     sid   记录id

     */
    private String username;
    private String password;
    private String name;
    private String job;
    private String phone;
    private String sid;
    private String sort = "0";

    public Map<String,String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", getUsername());
        map.put("password",getPassword());
        map.put("name",getName());
        map.put("job",getJob());
        map.put("phone",getPhone());
        map.put("sid",getSid());
        map.put("sort",getSort());
        return map;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
