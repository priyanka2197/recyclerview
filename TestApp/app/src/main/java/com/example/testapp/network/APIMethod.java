package com.example.testapp.network;

import com.example.testapp.model.ResponseDataItem;
import com.example.testapp.model.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIMethod {
    @GET("/developers")
    Call<List<ResponseDataItem>> getUserData();
}
