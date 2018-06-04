package com.example.lenovo.pizzalist.presenter;

import android.content.Context;

import com.example.lenovo.pizzalist.models.ModelController;
import com.example.lenovo.pizzalist.models.Product;
import com.example.lenovo.pizzalist.view.BasePresenterListener;

import java.util.List;

/**
 * Created by lenovo on 5/16/2018.
 */

public class Presenter extends BasePresenter implements OnGetDataListener {
    ModelController controler;
    BasePresenterListener presenterListener;

    public Presenter(BasePresenterListener listener) {
        super(listener);
        this.controler = new ModelController(this, this);
        this.presenterListener = listener;
    }

    @Override
    public void getDataFromURL(Context context, String url) {
        presenterListener.showProgress();
        controler.getDataFromRetrofit(context, url);
    }

    @Override
    public void onSuccessRequest(String message, List<Product> list) {
        presenterListener.hideProgress();
        presenterListener.onSuccess(message, list);
    }

    @Override
    public void onFailureRequest(String message) {

    }
}
