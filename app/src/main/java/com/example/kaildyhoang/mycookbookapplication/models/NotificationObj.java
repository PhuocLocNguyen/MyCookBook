package com.example.kaildyhoang.mycookbookapplication.models;

/**
 * Created by Microsoft Windows on 16/07/2017.
 */

public class NotificationObj {
    private String notiMsg;
    private String notiId;
    private String avatar;
    private String name;
    private int readed;
    private long notiAt;

    public NotificationObj() {
    }

    public NotificationObj(String notiMsg, String notiId, String avatar, String name, int readed, long notiAt) {
        this.notiMsg = notiMsg;
        this.notiId = notiId;
        this.avatar = avatar;
        this.name = name;
        this.readed = readed;
        this.notiAt = notiAt;
    }

    public long getNotiAt() {
        return notiAt;
    }

    public void setNotiAt(long notiAt) {
        this.notiAt = notiAt;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getNotiMsg() {
        return notiMsg;
    }

    public void setNotiMsg(String notiMsg) {
        this.notiMsg = notiMsg;
    }

    public String getNotiId() {
        return notiId;
    }

    public void setNotiId(String notiId) {
        this.notiId = notiId;
    }

    public int getReaded() {
        return readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
    }
}
