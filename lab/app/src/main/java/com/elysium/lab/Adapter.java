package com.elysium.lab;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by jay on 11/17/16.
 */

public class Adapter extends RecyclerView.Adapter<ViewHolder>{

    private List<String> mResultsList;

    public Adapter(List<String> resultsList){
        mResultsList = resultsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.mTextVIew.setText(mResultsList.get(position));

    }

    @Override
    public int getItemCount() {
        return mResultsList.size();
    }
}
