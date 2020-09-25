package com.example.demo.product_detail;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.example.demo.R;

public class DragView extends FrameLayout {
    private ViewDragHelper viewDragHelper;
    public DragView(@NonNull Context context) {
        super(context);
        init();
    }

    public DragView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DragView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new DraggableViewCallback());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    /**
     * Created by 小嵩 on 2017/8/30.
     */

    public class DraggableViewCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
//            if(child.getId()== R.id.v_red_box){
//                return true;
//            }
            return false;
        }

        /**
         * 子控件水平方向位置改变时触发
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.e("zhang","left="+left);
            //屏蔽掉水平方向
            return 0;
        }

        /**
         * 子控件竖直方向位置改变时触发
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.e("zhang","top="+top);
            //不能滑出顶部
            int maxTop = getHeight()-child.getHeight();
            return Math.max(Math.min(top, maxTop), 0);
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
//            if(child.getId()== R.id.v_red_box){
//                return getHeight()-child.getHeight();
//            }
            return super.getViewVerticalDragRange(child);
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.e("zhang","xvel="+xvel+";yvel="+yvel);
//            viewDragHelper.flingCapturedView(0,0,0,getHeight()-releasedChild.getHeight());
            if((releasedChild.getTop()+releasedChild.getHeight()/2) > getHeight()/2){
                if(viewDragHelper.smoothSlideViewTo(releasedChild,
                        0,getHeight()-releasedChild.getHeight())){
                    ViewCompat.postInvalidateOnAnimation(DragView.this);
                }
            }else{
                if(viewDragHelper.smoothSlideViewTo(releasedChild,
                        0,0)){
                    ViewCompat.postInvalidateOnAnimation(DragView.this);
                }

            }
        }
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
