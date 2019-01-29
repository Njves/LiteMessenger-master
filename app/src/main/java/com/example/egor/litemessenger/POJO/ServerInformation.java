package com.example.egor.litemessenger.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerInformation {
    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


}
