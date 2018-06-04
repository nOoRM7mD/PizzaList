package com.example.lenovo.pizzalist.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by lenovo on 5/14/2018.
 */

@Root(name = "item")
public class Product {

    //  private int id;z
    @Element(name = "id")
    private String id;
    @Element(name = "name")
    private String name;
    @Element(name = "cost")
    private String cost;
    @Element(name = "description")
    private String description;

    public Product(String name) {
        this.name = name;
    }

    public Product() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
