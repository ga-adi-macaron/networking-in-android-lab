package com.colinbradley.networkinginandroidlab;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by colinbradley on 11/17/16.
 */

public class Holder extends RecyclerView.ViewHolder{

    TextView mText;

    public Holder(View itemView) {
        super(itemView);

        mText = (TextView)itemView.findViewById(R.id.text);
    }
}
