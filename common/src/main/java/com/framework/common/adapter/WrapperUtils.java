package com.framework.common.adapter;

import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by zhy on 16/6/28.
 */
public class WrapperUtils {
    public interface SpanSizeCallback {
        int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position);
    }

    public static GridLayoutManager.SpanSizeLookup onAttachedToRecyclerView(RecyclerView.Adapter innerAdapter, RecyclerView recyclerView, GridLayoutManager.SpanSizeLookup oldLookup, final SpanSizeCallback callback) {
        innerAdapter.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            if (oldLookup == null) {
                oldLookup = gridLayoutManager.getSpanSizeLookup();
            }
            final GridLayoutManager.SpanSizeLookup finalOldLookup = oldLookup;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return callback.getSpanSize(gridLayoutManager, finalOldLookup, position);
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
        return oldLookup;
    }

    public static void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }
}
