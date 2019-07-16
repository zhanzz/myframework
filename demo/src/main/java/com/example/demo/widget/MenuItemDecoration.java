package com.example.demo.widget;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
/**
 * @author zhangzhiqiang
 * @date 2019/7/8.
 * description：
 */
public class MenuItemDecoration extends RecyclerView.ItemDecoration {
    private int mColSpace, mRowSpace;

    public MenuItemDecoration(int colSpace, int rowSpace) {
        mColSpace = colSpace;
        mRowSpace = rowSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        int spanCount = layoutManager.getSpanCount();
        int layoutPosition = lp.getViewLayoutPosition();

        if (layoutPosition < spanCount) { //第一行
            outRect.top = 0;
        }else {
            outRect.top = mRowSpace;
        }
        if(lp.getSpanIndex()!=(spanCount-1)){//不是最后一列
            outRect.right = mColSpace;
        }
    }
}
