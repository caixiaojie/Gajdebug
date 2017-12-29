package com.fskj.gaj.vo;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class MyAttentionResultVo {

    /**
     * en : 分类参数
     * zh : 中文名称
     * type : msg或者notice或者picnews
     * attention : 1=是我关注的 0=我没有关注
     */

    private String en;
    private String zh;
    private String type;
    private int attention;
    private boolean flag;

    public  void turn(){
        flag=!flag;
    }
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }
}
