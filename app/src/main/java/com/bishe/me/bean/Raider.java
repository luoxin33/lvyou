package com.bishe.me.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Raider extends LitePalSupport implements Serializable {

    int id;
    String name;
    String content;
    String time;
    String Url;
    String author;
    /**
     * 0-待审核 1-审核通过 2-审核不通过
     */
    int status;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
