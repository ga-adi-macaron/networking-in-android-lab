package net.serkanbal.wallmartlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Serkan on 17/11/16.
 */

public class SearchResultRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
    List<ResultItem> mList;

    public SearchResultRecyclerViewAdapter(List<ResultItem> list) {
        mList = list;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_layout, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        String itemName = mList.get(position).getItemName();
        holder.mItemName.setText(itemName);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
