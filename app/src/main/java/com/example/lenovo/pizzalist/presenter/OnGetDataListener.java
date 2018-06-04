package com.example.lenovo.pizzalist.presenter;


import com.example.lenovo.pizzalist.models.Product;

import java.util.List;

/**
 * Created by lenovo on 5/16/2018.
 */

public interface OnGetDataListener {
    void onSuccessRequest(String message, List<Product> list);

    void onFailureRequest(String message);
}
