package com.example.lenovo.pizzalist.models;

import android.content.Context;
import android.util.Log;


import com.example.lenovo.pizzalist.MyApp;
import com.example.lenovo.pizzalist.presenter.BaseControllerListener;
import com.example.lenovo.pizzalist.presenter.OnGetDataListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by lenovo on 5/16/2018.
 */

public class ModelController extends BaseController {
    private BaseControllerListener controllerListener;
    private OnGetDataListener onGetDataListener;


    public ModelController(BaseControllerListener listener, OnGetDataListener onGetDataListener) {
        super(listener);
        this.controllerListener = listener;
        this.onGetDataListener = onGetDataListener;
    }

    List<String> allProductData = new ArrayList<>();
    private List<Product> allProduct;

    public void getDataFromRetrofit(final Context context) {

        ((MyApp) context.getApplicationContext()).getApiService().getProducts()
                .enqueue(new Callback<ResultModel>() {
                    @Override
                    public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                        if (response.isSuccessful()) {
                            ResultModel xmlResponse = response.body();
                            allProduct = xmlResponse.getProducts();
                            for (int i = 0; i < allProduct.size(); i++) {
                                allProductData.add(allProduct.get(i).getName());
                            }
                            Log.d("FetchedData", "Refreshed" + allProduct);
                            onGetDataListener.onSuccessRequest("List Size: " + allProduct.size(), allProduct);
                            Collections.sort(allProduct, new Comparator<Product>() {
                                @Override
                                public int compare(Product firstProduct, Product secondProduct) {

                                    return firstProduct.getName().compareTo(secondProduct.getName());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultModel> call, Throwable t) {
                        System.out.println(t.getLocalizedMessage());
                    }
                });
    }

}

