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

import java.util.ArrayList;
import java.util.List;

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
        boolean intercept = viewDragHelper.shouldInterceptTouchEvent(ev);
        return intercept;
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
            if("move".equals(child.getTag())){
                for(DragListener listener : mListeners){
                    if(listener!=null){
                        listener.startDrag(child);
                    }
                }
                return true;
            }
            return false;
        }

        /**
         * 子控件水平方向位置改变时触发
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.e("zhang","left="+left);
            int maxLeft = getWidth()-child.getWidth();
            return Math.max(Math.min(left, maxLeft), 0);
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
            if("move".equals(child.getTag())){
                return getHeight()-child.getHeight();
            }
            return super.getViewVerticalDragRange(child);
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            if("move".equals(child.getTag())){
                return getWidth()-child.getWidth();
            }
            return super.getViewHorizontalDragRange(child);
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.e("zhang","xvel="+xvel+";yvel="+yvel);
            //先只停在左边
//            viewDragHelper.flingCapturedView(0,0,0,getHeight()-releasedChild.getHeight());
//            if((releasedChild.getLeft()+releasedChild.getWidth()/2) > getWidth()/2){
//                if(viewDragHelper.smoothSlideViewTo(releasedChild,
//                        getWidth()-releasedChild.getWidth(),releasedChild.getTop())){
//                    ViewCompat.postInvalidateOnAnimation(DragView.this);
//                }
//            }else{
                if(viewDragHelper.smoothSlideViewTo(releasedChild,
                        0,releasedChild.getTop())){
                    ViewCompat.postInvalidateOnAnimation(DragView.this);
                    FrameLayout.LayoutParams layoutParams = (LayoutParams) releasedChild.getLayoutParams();
                    layoutParams.topMargin = releasedChild.getTop();
                    layoutParams.leftMargin = 0;
                }
//            }
            for(DragListener listener : mListeners){
                if(listener!=null){
                    listener.endDrag(releasedChild);
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

    public interface DragListener{
        void startDrag(View dragView);
        void endDrag(View dragView);
    }

    private List<DragListener> mListeners = new ArrayList<>();

    public void addViewDragListener(DragListener listener){
        mListeners.add(listener);
    }

    public void removeViewDragListener(DragListener listener){
        mListeners.remove(listener);
    }
}
