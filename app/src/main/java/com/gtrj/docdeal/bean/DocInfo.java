package com.gtrj.docdeal.bean;

import java.io.Serializable;

/**
 * Created by zhang77555 on 2014/12/25.
 */
public class DocInfo implements Serializable{
    private String id;
    private String title;
    private String sender;
    private String date;
    private String type;
    private String returnCode;

    public DocInfo() {
    }

    public DocInfo(String id, String title, String sender, String date, String type, String returnCode) {
        this.id = id;
        this.title = title;
        this.sender = sender;
        this.date = date;
        this.type = type;
        this.returnCode = returnCode;
    }

    public String getSender() {
        return sender;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    @Override
    public String toString() {
        return "DocInfo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", sender='" + sender + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", returnCode='" + returnCode + '\'' +
                '}';
    }
}