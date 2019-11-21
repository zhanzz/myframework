package com.example.demo.db;

/**
 * @author zhangzhiqiang
 * @date 2019/10/11.
 * description：
 */
public class DbBean {
    private long id;
    private String addtime;
    private String content;

    public DbBean(String addtime, String content) {
        this.addtime = addtime;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
