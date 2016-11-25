package drsdrs.networkinginandroidlab;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ds on 11/17/16.
 */

public class WalmartItemRVAdapter extends RecyclerView.Adapter<WalmartItemViewHolder> {

    private List<String> mCategoryList;

    public WalmartItemRVAdapter(List<String> categoryList) {
        mCategoryList = categoryList;
    }


    @Override
    public WalmartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new WalmartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WalmartItemViewHolder holder, int position) {
        holder.mTextView.setText(mCategoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }
}
