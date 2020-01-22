package com.example.demo;

/**
 * @author zhangzhiqiang
 * @date 2020/1/9.
 * description：
 */
public enum ReportFormType {
    Management_total("经营总表",0);

    private String content;
    private int id;
    ReportFormType(String content, int id){
        this.content = content;
        this.id=id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
