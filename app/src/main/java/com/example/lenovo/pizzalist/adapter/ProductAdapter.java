package com.example.lenovo.pizzalist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.pizzalist.R;
import com.example.lenovo.pizzalist.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 5/14/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CustomViewHolder> {

    ArrayList<Product> pizzaList;

    public ArrayList<Product> getPizzaList() {
        return pizzaList;
    }

    public ProductAdapter(ArrayList<Product> arrayList, Context context) {
        this.pizzaList = arrayList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view,parent,false);
        CustomViewHolder viewHolder=new CustomViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        Product pizzaItem = pizzaList.get(position);
        holder.name.setText(pizzaItem.getName());
        holder.cost.setText(pizzaItem.getCost());
        holder.desc.setText(pizzaItem.getDescription());
    }

    @Override
    public int getItemCount() {
        int size = pizzaList == null ? 0 : pizzaList.size();
        return size;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView cost;
        TextView desc;
        public CustomViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.text_id);
            cost = (TextView) itemView.findViewById(R.id.text_price);
            desc = (TextView) itemView.findViewById(R.id.text_desc);

        }
    }
    public void setData(List<Product> data) {
        if (pizzaList != null) {
            pizzaList.clear();
            pizzaList.addAll(data);
            notifyDataSetChanged();
        }
    }
}
