package com.example.demo.product_detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class MyScrollView extends NestedScrollView {
    public MyScrollView(@NonNull Context context) {
        super(context);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int computeVerticalScrollRange() {
        final int count = getChildCount();
        final int parentSpace = getHeight() - getPaddingBottom() - getPaddingTop();
        if (count == 0) {
            return parentSpace;
        }

        View child = getChildAt(0);
        NestedScrollView.LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int scrollRange = child.getBottom() + lp.bottomMargin;
        final int scrollY = getScrollY();
        final int overscrollBottom = Math.max(0, scrollRange - parentSpace);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom;
        }

        return scrollRange;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public int computeVerticalScrollExtent() {
        super.computeVerticalScrollExtent();
        return getHeight();
    }

    @Override
    public boolean canScrollVertically(int direction) {
        final int offset = Math.max(0, getScrollY());
        final int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        Log.e("zhang",String.format("offset=%s;computeVerticalScrollRange()=%s;computeVerticalScrollExtent=%s;range=%s",offset,computeVerticalScrollRange(),
                computeVerticalScrollExtent(),range));
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 2;
        }
    }
}
