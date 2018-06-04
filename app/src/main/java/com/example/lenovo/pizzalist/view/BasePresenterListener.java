package com.example.lenovo.pizzalist.view;


import com.example.lenovo.pizzalist.models.Product;

import java.util.List;

public interface BasePresenterListener {

    void showProgress();

    void hideProgress();


        void onSuccess(String message, List<Product> list);

        void onFailure(String message);
}
