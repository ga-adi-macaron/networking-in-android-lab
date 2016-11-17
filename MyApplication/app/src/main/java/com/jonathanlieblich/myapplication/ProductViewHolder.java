package com.jonathanlieblich.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jonlieblich on 11/17/16.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public TextView mName;

    public ProductViewHolder(View itemView) {
        super(itemView);

        mName = (TextView)itemView.findViewById(R.id.name);
    }
}
