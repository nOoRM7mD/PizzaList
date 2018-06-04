package com.example.lenovo.pizzalist.models;


import com.example.lenovo.pizzalist.presenter.BaseControllerListener;

public class BaseController<T extends BaseControllerListener>{

    protected T listener;

    public BaseController(T listener) {

        this.listener = listener;
    }

}
