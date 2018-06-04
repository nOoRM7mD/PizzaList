package com.example.lenovo.pizzalist.sevice;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lenovo on 5/18/2018.
 */

class XMLInterceptor implements Interceptor {
    Context context;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
     //   if (!Network.isNetworkAvailable(context.getApplicationContext())) {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale \
            request = request
                    .newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
       // }
        return chain.proceed(request);
    }
}
