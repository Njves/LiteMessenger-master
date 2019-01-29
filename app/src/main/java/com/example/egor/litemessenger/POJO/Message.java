package com.example.egor.litemessenger.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("unique_id")
    @Expose
    private String uniqueId;
    @SerializedName("user_sender")
    @Expose
    private String userSender;
    @SerializedName("user_reciver")
    @Expose
    private String userReciver;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("flags")
    @Expose
    private Integer flags;
    @SerializedName("time")
    @Expose
    private Integer time;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUserSender() {
        return userSender;
    }

    public void setUserSender(String userSender) {
        this.userSender = userSender;
    }

    public String getUserReciver() {
        return userReciver;
    }

    public void setUserReciver(String userReciver) {
        this.userReciver = userReciver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

}
