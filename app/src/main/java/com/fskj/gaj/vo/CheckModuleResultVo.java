package com.fskj.gaj.vo;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class CheckModuleResultVo {
    private String name;//模块名称”
    private String type;//分类en”
    private int module; //“module”:”对应的文章类型0=msg,1=notice,2=picnews”


    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
