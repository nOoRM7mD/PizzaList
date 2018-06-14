package com.example.lenovo.pizzalist.sevice;


import com.example.lenovo.pizzalist.models.ResultModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductAPI {
    String BASE_URL = "https://api.androidhive.info/";

    @GET("/pizza/?format=xml")
    Call<ResultModel> getProducts();
}
