package com.example.egor.smokenet.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerErrorInformation {

    @SerializedName("error")
    @Expose
    private Integer error;


    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }


}
