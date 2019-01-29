package com.example.egor.litemessenger.Requests;



import com.example.egor.litemessenger.POJO.ServerInformation;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginUser {
    @FormUrlEncoded
    @POST("users/login.php")
    Call<ServerInformation> loginPostCall(@FieldMap HashMap<String, String> postMap);
}
