package com.framework.common.item_decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class StaggeredGridHSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int verticalSpace,padding;
    private int eachItemWidth,dividerItemWidth;

    public StaggeredGridHSpaceItemDecoration(int horizontalSpace, int verticalSpace, int padding) {
        this.verticalSpace = verticalSpace;
        this.padding = padding;//左右padding
        dividerItemWidth = horizontalSpace;//item之间的间隔
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getLayoutManager() instanceof StaggeredGridLayoutManager){
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            int spanIndex = lp.getSpanIndex();
            final int spanCount = layoutManager.getSpanCount();
            int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            if (!lp.isFullSpan()) {
                eachItemWidth = ((spanCount-1)*dividerItemWidth+2*padding)/spanCount;//平均每一个item含有的间隔
                outRect.bottom = spanIndex * (dividerItemWidth - eachItemWidth) + padding;
                outRect.top = eachItemWidth - outRect.bottom;

                int count = 0;
                if(parent.getAdapter()!=null){
                    count = parent.getAdapter().getItemCount();
                }
                int line = count/spanCount + (count%spanCount==0 ? 0:1);
                int currentLine = itemPosition/spanCount + (itemPosition%spanCount==0 ? 0:1);
                if(currentLine==line){//最后一行
                    outRect.right = 0;
                }else{
                    outRect.right = verticalSpace;
                }
            }else if(itemPosition ==0){
                outRect.top = outRect.bottom = padding;
                outRect.right = verticalSpace;
            }
        }
    }
}
