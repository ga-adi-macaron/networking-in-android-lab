package generalassembly.yuliyakaleda.solution_code;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by charlie on 11/18/16.
 */

public class WalmartItemRecyclerView
        extends RecyclerView.Adapter<WalmartItemRecyclerView.WalmartItemViewHolder> {

    private List<String> mWalmartItemNames;

    public WalmartItemRecyclerView(List<String> walmartItemNames) {
        mWalmartItemNames = walmartItemNames;
    }

    @Override
    public WalmartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new WalmartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WalmartItemViewHolder holder, int position) {
        holder.mTextView.setText(mWalmartItemNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mWalmartItemNames.size();
    }

    public class WalmartItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public WalmartItemViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
