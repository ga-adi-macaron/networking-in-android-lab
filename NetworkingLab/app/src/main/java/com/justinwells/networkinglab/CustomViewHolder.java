package com.justinwells.networkinglab;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by justinwells on 11/17/16.
 */

public class CustomViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    public CustomViewHolder(View itemView) {
        super(itemView);
        this.textView = (TextView) itemView.findViewById(R.id.text);
    }
}
