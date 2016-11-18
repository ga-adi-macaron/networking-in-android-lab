package net.serkanbal.wallmartlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Serkan on 17/11/16.
 */

public class SearchResultViewHolder extends RecyclerView.ViewHolder {
    TextView mItemName;

    public SearchResultViewHolder(View itemView) {
        super(itemView);
        mItemName = (TextView) itemView.findViewById(R.id.recyclerview_ItemNameText);
    }
}
