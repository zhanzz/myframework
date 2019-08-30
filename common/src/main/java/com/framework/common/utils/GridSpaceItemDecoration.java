package com.framework.common.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int horizontalSpace, verticalSpace,padding;

    private int eachItemWidth,dividerItemWidth;

    public GridSpaceItemDecoration(int horizontalSpace, int verticalSpace, int padding) {
        this.verticalSpace = verticalSpace;
        this.horizontalSpace = horizontalSpace;
        this.padding = padding;//左右padding
        dividerItemWidth = horizontalSpace;//item之间的间隔
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getLayoutManager() instanceof GridLayoutManager){
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            int spanIndex = lp.getSpanIndex();
            final int spanCount = layoutManager.getSpanCount();
            int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            if (lp.getSpanSize() != spanCount) {
                eachItemWidth = ((spanCount-1)*dividerItemWidth+2*padding)/spanCount;//平均每一个item含有的间隔
                outRect.left = spanIndex * (dividerItemWidth - eachItemWidth) + padding;
                outRect.right = eachItemWidth - outRect.left;

                int count = 0;
                if(parent.getAdapter()!=null){
                    count = parent.getAdapter().getItemCount();
                }
                int line = count/spanCount + (count%spanCount==0 ? 0:1);
                int currentLine = itemPosition/spanCount + (itemPosition%spanCount==0 ? 0:1);
                if(currentLine==line){//最后一行
                    outRect.bottom = 0;
                }else{
                    outRect.bottom = verticalSpace;
                }
            }else if(itemPosition ==0){
                outRect.bottom = verticalSpace;
            }
        }
    }
}
