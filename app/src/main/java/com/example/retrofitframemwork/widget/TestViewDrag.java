package com.example.retrofitframemwork.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.customview.widget.ViewDragHelper;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.framework.common.utils.UIHelper;

/**
 * @author zhangzhiqiang
 * @date 2019/8/30.
 * description：
 */
public class TestViewDrag extends LinearLayout {
    private View one,two;
    private ViewDragHelper mDragHelper;
    public TestViewDrag(Context context) {
        super(context);
        init();
    }

    public TestViewDrag(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestViewDrag(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestViewDrag(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, 0.5f, new TestViewDrag.DragHelperCallback());

    }

    final class  DragHelperCallback extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            return left;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return 0;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for(int i=0,count=getChildCount();i<count;i++){
            switch (i){
                case 0:
                    one = getChildAt(i);
                    break;
                case 1:
                    two = getChildAt(i);
                    break;
                case 2:
                    break;
            }
        }
    }
}
