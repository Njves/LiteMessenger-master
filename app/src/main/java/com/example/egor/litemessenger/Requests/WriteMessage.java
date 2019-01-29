package com.example.egor.litemessenger.Requests;

import com.example.egor.litemessenger.POJO.Message;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WriteMessage {
    @FormUrlEncoded
    @POST("users/message_engine.php")
    Call<Message> writeMessage(@FieldMap HashMap<String, String> postMap);
}
