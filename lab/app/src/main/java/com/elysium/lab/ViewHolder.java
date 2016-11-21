package com.elysium.lab;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jay on 11/17/16.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView mTextVIew;

    public ViewHolder(View itemView) {
        super(itemView);

        mTextVIew = (TextView) itemView.findViewById(R.id.textView);
    }
}
