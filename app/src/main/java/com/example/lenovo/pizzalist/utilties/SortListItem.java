package com.example.lenovo.pizzalist.utilties;


import com.example.lenovo.pizzalist.models.Product;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lenovo on 5/20/2018.
 */

public class SortListItem {
    public List<Product> sortProductList(List<Product> list){

        Collections.sort(list, new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {

                return firstProduct.getName().compareTo(secondProduct.getName());
            }
        });

        return list;
    }
}
