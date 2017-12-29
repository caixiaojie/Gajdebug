package com.fskj.gaj.vo;

import java.util.List;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class LoginResultVo {
    /**
     * pubnotice : [{"name":"模块名称","type":"模块英文"},{"name":"模块名称","type":"模块英文"}]
     * duty : 1
     */

    private int duty;
    private List<PubnoticeVo> pubnotice;
    private String username;
    private String department;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getDuty() {
        return duty;
    }

    public void setDuty(int duty) {
        this.duty = duty;
    }

    public List<PubnoticeVo> getPubnotice() {
        return pubnotice;
    }

    public void setPubnotice(List<PubnoticeVo> pubnotice) {
        this.pubnotice = pubnotice;
    }
//    private int pubnotice;//1=可以发布通知通告      0=不可以
//    private int duty;//1=可以进入值班编辑功能  0=不可以

}
