package com.example.lenovo.pizzalist.models;

import android.content.Context;
import android.util.Log;


import com.example.lenovo.pizzalist.presenter.BaseControllerListener;
import com.example.lenovo.pizzalist.presenter.OnGetDataListener;
import com.example.lenovo.pizzalist.sevice.ProductAPI;
import com.example.lenovo.pizzalist.utilties.SortListItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import static com.example.lenovo.pizzalist.utilties.Network.isNetworkAvailable;
import static com.example.lenovo.pizzalist.view.MainActivity.BASE_URL;


/**
 * Created by lenovo on 5/16/2018.
 */

public class ModelController extends BaseController {
    private BaseControllerListener controllerListener;
    private OnGetDataListener onGetDataListener;
    private Cache cache;

    public ModelController(BaseControllerListener listener, OnGetDataListener onGetDataListener) {
        super(listener);
        this.controllerListener = listener;
        this.onGetDataListener = onGetDataListener;
    }

    private int cacheSize = 10 * 1024 * 1024; // 10 MiB
    List<String> allProductData = new ArrayList<>();
    private List<Product> allProduct;

    public void getDataFromRetrofit(final Context context, String url) {
        cache = new Cache(context.getCacheDir(), cacheSize);


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {

                    @Override
                    public okhttp3.Response intercept(Chain chain)
                            throws IOException {
                        Request request = chain.request();
                        if (!isNetworkAvailable(context.getApplicationContext())) {
                                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale \
                                request = request
                                        .newBuilder()
                                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                        .build();
                        }
                        return chain.proceed(request);
                    }
                })
                .build();


   Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create());

        Retrofit retrofit = builder.build();
    //    Retrofit retrofit=ApiConnection.retrofitForXML(BASE_URL);

        ProductAPI api = retrofit.create(ProductAPI.class);

        Call<ResultModel> call = api.getProducts();
        call.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                ResultModel xmlResponse = response.body();
                allProduct = xmlResponse.getProducts();
                for (int i = 0; i < allProduct.size(); i++) {
                    allProductData.add(allProduct.get(i).getName());
                }
                Log.d("FetchedData", "Refreshed" + allProduct);
                onGetDataListener.onSuccessRequest("List Size: " + allProduct.size(), allProduct);
             /*   Collections.sort(allProduct, new Comparator<Product>() {
                    @Override
                    public int compare(Product firstProduct, Product secondProduct) {

                        return firstProduct.getName().compareTo(secondProduct.getName());
                    }
                });*/
                SortListItem sortListItem=new SortListItem();
             sortListItem.sortProductList(allProduct);
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                System.out.println(t.getLocalizedMessage());
            }
        });

    }


}

