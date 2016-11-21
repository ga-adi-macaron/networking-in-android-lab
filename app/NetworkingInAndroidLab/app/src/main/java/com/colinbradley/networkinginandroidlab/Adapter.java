package com.colinbradley.networkinginandroidlab;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by colinbradley on 11/17/16.
 */

public class Adapter extends RecyclerView.Adapter<Holder>{

    private List<String> mResultsList;

    public Adapter(List<String> resultsList){
        mResultsList = resultsList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_layout, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.mText.setText(mResultsList.get(position));

    }

    @Override
    public int getItemCount() {
        return mResultsList.size();
    }
}
