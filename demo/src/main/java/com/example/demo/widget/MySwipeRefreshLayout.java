package com.example.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @author zhangzhiqiang
 * @date 2019/11/29.
 * description：
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {
    public MySwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public MySwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        View lastView = getChildAt(getChildCount()-1);
//        lastView.measure(MeasureSpec.makeMeasureSpec(
//                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
//                MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(
//                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.AT_MOST));
//        setMeasuredDimension(lastView.getMeasuredWidth(),lastView.getMeasuredHeight());
    }

    @Override
    public boolean canChildScrollUp() {
        return super.canChildScrollUp();
    }
}
