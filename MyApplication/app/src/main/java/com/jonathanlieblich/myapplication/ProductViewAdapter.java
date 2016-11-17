package com.jonathanlieblich.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by jonlieblich on 11/17/16.
 */

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private List<String> mProducts;

    public ProductViewAdapter(List<String> list) {
        mProducts = list;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.product, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.mName.setText(mProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }
}
