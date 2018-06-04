package com.example.lenovo.pizzalist.presenter;


import com.example.lenovo.pizzalist.models.BaseController;
import com.example.lenovo.pizzalist.view.BasePresenterListener;

public abstract class BasePresenter<T extends BaseController,
        E extends BasePresenterListener>
        implements BaseControllerListener {

    protected E listener;
    protected T controller;
    public BasePresenter(E listener) {
        this.listener = listener;
    }
}