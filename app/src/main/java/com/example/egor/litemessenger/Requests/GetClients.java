package com.example.egor.litemessenger.Requests;



import com.example.egor.litemessenger.Models.Client;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;

public interface GetClients {
    @GET("/getListClients.php")
    Call<List<Client>> getClients();
}
