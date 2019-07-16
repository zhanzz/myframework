package com.framework.model.demo;

/**
 * @author zhangzhiqiang
 * @date 2019/6/28.
 * description：
 */
public class TestDiffBean {
    public TestDiffBean(int id, String content) {
        this.id = id;
        this.content = content;
    }

    int id;
    String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
