package com.example.lenovo.pizzalist.sevice;


import com.example.lenovo.pizzalist.models.ResultModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Kaushal28 on 5/5/2017.
 */

public interface ProductAPI {

    @GET("/pizza/?format=xml")
    Call<ResultModel> getProducts();

}
