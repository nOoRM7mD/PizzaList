package com.example.lenovo.pizzalist.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by lenovo on 5/14/2018.
 */


    @Root(name = "menu", strict = false)
    public class ResultModel {

        @ElementList(inline = true)
        public List<Product> products;

    public List<Product> getProducts() {
        return products;
    }
}
