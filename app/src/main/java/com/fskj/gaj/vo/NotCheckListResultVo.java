package com.fskj.gaj.vo;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class NotCheckListResultVo {
    /**
     * "mid": "记录id",
     "title": "标题",
     "createtime": "创建时间",
     “username”:“发布人”,
     “uptime”:“提交审核时间”,
     “reason”:”上次拒绝原因”

     */
    private String mid;
    private String title;
    private String createtime;
    private String username;
    private String uptime;
    private String reason;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
